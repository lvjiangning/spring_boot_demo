package com.rihao.property.modules.ent.vo;

import com.rihao.property.modules.config.entity.SysFile;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
@Accessors(chain = true)
public class EntUpdateNameVo implements Serializable {
    private Long id;
    private List<SysFile> files;
    private String newName;

}
