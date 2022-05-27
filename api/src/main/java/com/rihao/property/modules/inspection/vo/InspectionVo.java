package com.rihao.property.modules.inspection.vo;

import com.rihao.property.modules.inspection.entity.InspectionFile;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @description
 * @since 2021-03-27
 */
@Data
@ApiModel
@Accessors(chain = true)
public class InspectionVo implements Serializable {

    private Long id;
    private String location;
    private String title;
    private String content;
    private String inspector;
    private Long parkId;
    private String parkName;
    private String inspectionTime;
    private LocalDateTime createTime;
    private String createBy;
    private List<InspectionFile> fileList;
}