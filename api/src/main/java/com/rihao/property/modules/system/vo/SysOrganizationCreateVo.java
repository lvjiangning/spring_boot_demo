package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Ken
 * @date 2020/5/18
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SysOrganizationCreateVo implements Serializable {

    @NotNull
    @ApiModelProperty("单位名称")
    private String name;

    @NotNull
    @ApiModelProperty("单位全称")
    private String fullName;

    @ApiModelProperty("单位编码")
    private String code;

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("单位类型")
    private Long type;

    @ApiModelProperty("单位级别")
    private Long level;

    @ApiModelProperty("建制人数")
    private Integer establishmentNumber;

    @ApiModelProperty("邮政编码")
    private String postalCode;

    @ApiModelProperty("联系人")
    private String contacts;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("通信地址")
    private String address;

    @ApiModelProperty("排序值")
    private Integer orders;

}
