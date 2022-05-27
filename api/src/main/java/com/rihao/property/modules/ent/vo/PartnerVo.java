package com.rihao.property.modules.ent.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ApiModel
@Accessors(chain = true)
public class PartnerVo implements Serializable {

//    private Long entId;
    private String name;
    private String proportion;
}
