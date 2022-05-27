package com.rihao.property.common;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Optional;

/**
 * @author gaoy
 * 2020/3/8/008
 */
@Data
@Accessors(chain = true)
public class TreeEntity extends BaseEntity {
    @TableField("parent_ids")
    private String parentIds;

    @TableField("parent_id")
    private Long parentId;

    public String makeSelfAsParentsIds() {
        return Optional.ofNullable(getParentIds()).orElse("") + getId() + "/";
    }
}
