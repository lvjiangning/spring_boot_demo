package com.rihao.property.modules.system.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ken
 * @date 2020/5/25
 * @description
 */
@ApiModel
@Data
@Accessors(chain = true)
public class TreeOrganizationVo implements Serializable {

    @JsonProperty("value")
    private Long id;

    @JsonProperty("title")
    private String name;

    private boolean disabled = false;

    @JsonProperty("children")
    private List<TreeOrganizationVo> children;

    public List<TreeOrganizationVo> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<TreeOrganizationVo> children) {
        this.children = children;
    }

    /**
     * @return
     */
    public boolean isHasChildren() {
        return !getChildren().isEmpty();
    }
}
