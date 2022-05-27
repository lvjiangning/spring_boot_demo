package com.rihao.property.modules.estate.vo;

import com.rihao.property.modules.common.vo.FileVo;
import com.rihao.property.modules.config.entity.SysFile;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
@Accessors(chain = true)
public class ParkFileCreateVo implements Serializable {
    private String name;
    private List<SysFile> files;
    private Long parkId;
}
