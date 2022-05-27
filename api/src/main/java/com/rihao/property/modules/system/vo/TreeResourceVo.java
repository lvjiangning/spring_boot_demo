package com.rihao.property.modules.system.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
@Accessors(chain = true)
public class TreeResourceVo implements Serializable {
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("text")
    private String text;

    private String icon;

    @JsonProperty("path")
    private String url;

    @ApiModelProperty("menu | button | other")
    private String type;

    @JsonProperty("code")
    private String code;

    @JsonProperty("pids")
    private String parentIds;

    private List<TreeResourceVo> children;

    public List<TreeResourceVo> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<TreeResourceVo> children) {
        this.children = children;
    }

    /**
     * @return
     */
    public boolean isHasChildren() {
        return !getChildren().isEmpty();
    }
}
