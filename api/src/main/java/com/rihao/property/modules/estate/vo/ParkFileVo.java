package com.rihao.property.modules.estate.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @date 2021-06-11
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ParkFileVo extends ParkFileCreateVo {

    private Long id;
    private String parkName;
    private String fileUrl;
    private String createBy;
    private LocalDateTime createTime;
}