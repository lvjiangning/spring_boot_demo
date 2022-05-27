package com.rihao.property.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangyu
 * @since 2021-09-01
 */
@Data
@Accessors(chain = true)
@TableName("tb_task_queue")
public class TaskQueue implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("command_key")
    private String commandKey;

    @TableField("params")
    private String params;


}
