package com.rihao.property.modules.system.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.system.entity.TaskQueue;
import com.rihao.property.modules.system.vo.TaskQueueVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-09-01
 * @description
 */
@Mapper
public interface TaskQueueConvert {

    TaskQueueConvert INSTANCE = Mappers.getMapper(TaskQueueConvert.class);

    List<TaskQueueVo> entity2ListItemBatch(List<TaskQueue> list);
}