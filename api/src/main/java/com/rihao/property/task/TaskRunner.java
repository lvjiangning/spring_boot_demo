package com.rihao.property.task;

import com.rihao.property.modules.system.entity.TaskQueue;
import com.rihao.property.modules.system.service.ITaskQueueService;
import com.rihao.property.task.execute.AbstractCommand;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class TaskRunner implements Runnable {
    //并发队列
    private final LinkedBlockingQueue<Long> queue;
    //任务队列 保存在数据库中
    private final ITaskQueueService taskQueueService;
    private final Map<String, AbstractCommand<?>> commandMap;

    public TaskRunner(LinkedBlockingQueue<Long> queue, ITaskQueueService taskQueueService, Map<String, AbstractCommand<?>> commandMap) {

        this.queue = queue;
        this.taskQueueService = taskQueueService;
        this.commandMap = commandMap;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //队列为空时，阻塞等待
                //队列不为空，从队首获取并移除一个元素，如果消费后还有元素在队列中，继续唤醒下一个消费线程进行元素移除。
                // 如果放之前队列是满元素的情况，移除完后要唤醒生产线程进行添加元素。
                Long taskId = this.queue.take();
                //根据任务id去查任务数据表
                TaskQueue task = this.taskQueueService.getById(taskId);
                if (task != null) {
                    if (commandMap.containsKey(task.getCommandKey())) {
                        //执行任务
                        boolean result = commandMap.get(task.getCommandKey()).execute(task.getParams());
                        if (result) {
                            //任务执行完成后，删除数据库中的任务详情
                            this.taskQueueService.removeById(taskId);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("queue take error", e);
            }
        }
    }
}
