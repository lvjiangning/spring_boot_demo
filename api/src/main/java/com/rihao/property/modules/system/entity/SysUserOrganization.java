package com.rihao.property.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户单位关联表
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_sys_user_organization")
public class SysUserOrganization implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId("user_id")
    private Long userId;

    /**
     * 单位id集合
     */
    @TableField("organization_ids")
    private String organizationIds;


}
