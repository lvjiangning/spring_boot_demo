package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Ken
 * @date 2020/5/18
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SysOrganizationListVo implements Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("单位名称")
    private String name;

    @ApiModelProperty("单位全称")
    private String fullName;

    @ApiModelProperty("单位编码")
    private String code;

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("上级单位")
    private String parentName;

    @ApiModelProperty("单位类型")
    private String type;

    @ApiModelProperty("单位级别")
    private String level;

    @ApiModelProperty("建制人数")
    private Integer establishmentNumber;

    @ApiModelProperty("实有人数")
    private Long currentNumber;

}
