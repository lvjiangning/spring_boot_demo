package com.rihao.property.modules.inspection.vo;

import com.rihao.property.modules.inspection.entity.InspectionFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
@Accessors(chain = true)
public class InspectionCreateVo implements Serializable {

    @NotNull
    @ApiModelProperty("巡查人")
    private String inspector;

    @NotNull
    @ApiModelProperty("标题")
    private String title;

    @NotNull
    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("单位ID")
    private Long parkId;

    private String inspectionTime;

    private List<InspectionFile> files;
}
