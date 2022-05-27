package com.rihao.property.shiro.vo;

import com.rihao.property.common.enums.Gender;
import com.rihao.property.common.vo.KeyValueVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author gaoy
 * 2020/2/29/029
 */
@ApiModel
@Data
public class CurrentUserVo {
    private Long id;

    @ApiModelProperty("账号")
    private String username;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("照片")
    private String photo;

    @ApiModelProperty("性别")
    private Gender gender;

    /**
     * 籍贯
     */
    @ApiModelProperty("籍贯")
    private String nativePlace;

    @ApiModelProperty("出生日期")
    private LocalDate birthday;

    @ApiModelProperty("联系方式")
    private String telephone;
    /**
     * 级别
     */
    @ApiModelProperty("级别")
    private String level;

    private Long establishId;

    private String establishName;

    private Long roleId;
}
