package com.rihao.property.task;

import com.alibaba.fastjson.JSON;
import com.rihao.property.modules.system.entity.TaskQueue;
import com.rihao.property.modules.system.service.ITaskQueueService;
import com.rihao.property.task.execute.AbstractCommand;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 任务分发器
 */
@Component
@Slf4j
public class TaskDispatcher implements InitializingBean {
    public static final String SELECT_ID_FROM_TB_TASK_QUEUE_LIMIT_3 = "select id from tb_task_queue order by id limit 3";
    private ITaskQueueService taskQueueService;
    private JdbcTemplate jdbcTemplate;
    private LinkedBlockingQueue<Long> queue = new LinkedBlockingQueue<>();
    private Map<String, AbstractCommand<?>> commandMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new TaskRunner(this.queue, this.taskQueueService, this.commandMap)).start();
    }
    //上一次开始执行时间点之后30秒再执行
    @Scheduled(fixedRate = 30000)
    public void execute() {
        //如果队列中存在大于3条，则先处理队列的
        if (this.queue.size() >= 3) {
            return;
        }
        //查询数据库的待执行任务
        List<Long> tasks = this.findTasks();
        if (CollectionUtils.isEmpty(tasks)) {
            log.info("no task to schedule");
            return;
        }
        for (Long task : tasks) {
            if (this.queue.contains(task)) {
                continue;
            }
            try {
                //存入队列
                this.queue.put(task);
                log.info("put in queue by Schedule,queue size {},task:{}", this.queue.size(), task);
            } catch (InterruptedException e) {
                log.error("存入队列失败", e);
            }
        }


    }

    public List<Long> findTasks() {
        return this.jdbcTemplate.query(SELECT_ID_FROM_TB_TASK_QUEUE_LIMIT_3, new SingleColumnRowMapper<>(Long.TYPE));
    }

    public <T> void addTask(TaskEntity<T> taskEntity) {
        TaskQueue taskQueue = new TaskQueue().setCommandKey(taskEntity.commandKey)
                .setParams(JSON.toJSONString(taskEntity.param));
        this.taskQueueService.save(taskQueue);
        if (this.queue.contains(taskQueue.getId())) {
            return;
        }
        try {
            this.queue.put(taskQueue.getId());
        } catch (InterruptedException e) {
            log.error("存入队列失败", e);
        }
    }

    @Autowired
    public void setTaskQueueService(ITaskQueueService taskQueueService) {
        this.taskQueueService = taskQueueService;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired(required = false)
    public void setCommandMap(Map<String, AbstractCommand<?>> commandMap) {
        this.commandMap = commandMap;
    }

    @Data
    public static class TaskEntity<T> {
        private String commandKey;
        private T param;
    }
}
