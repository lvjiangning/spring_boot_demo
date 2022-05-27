package com.rihao.property.modules.ent.entity;

import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author wangyu
 * @since 2021-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_ent_partner")
public class EntPartner extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 占股比例
     */
    @TableField("proportion")
    private String proportion;

    /**
     * 企业ID
     */
    private Long entId;

}
