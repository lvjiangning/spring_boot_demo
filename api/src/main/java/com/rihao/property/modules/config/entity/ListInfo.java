package com.rihao.property.modules.config.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  列表明细
 * </p>
 *
 * @author wangyu
 * @since 2021-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_list_info")
public class ListInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 列表名称 一般是url
     */
    @TableField("list_name")
    private String listName;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 列表详情
     */
    @TableField("list_info")
    private String listInfo;


}
