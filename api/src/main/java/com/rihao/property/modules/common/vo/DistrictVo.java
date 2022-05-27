package com.rihao.property.modules.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Ken
 * @date 2020/5/19
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class DistrictVo implements Serializable {

    private Long id;

    /**
     * 行政区等级 1:省 2:市 3:区
     */
    @ApiModelProperty("level")
    private Integer level;

    /**
     * 行政区全称
     */
    @ApiModelProperty("full_name")
    private String fullName;

    /**
     * 组合全称
     */
    @ApiModelProperty("merge_name")
    private String mergeName;

    /**
     * 父级行政区编码
     */
    @ApiModelProperty("parent_id")
    private Long parentId;

    /**
     * 父级行政区路径
     */
    @ApiModelProperty("parent_ids")
    private String parentIds;


}
