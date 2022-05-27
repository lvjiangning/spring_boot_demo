package com.rihao.property.modules.ent.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ApiModel
@Accessors(chain = true)
public class EntUpdateVo extends EntCreateVo {
    private Long id;
    private String newName;
}
