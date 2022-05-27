package com.rihao.property.modules.ent.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rihao.property.modules.config.entity.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @description
 * @since 2021-03-29
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EntMattersVo implements Serializable {

    private Long id;

    @ApiModelProperty("事项名称")
    private String title;

    @ApiModelProperty("事项内容")
    private String content;

    private String entId;

    private String entName;

    private String createTime;
    private String happenTime;

    private Long fileId;

    private SysFile file; //展示使用
}