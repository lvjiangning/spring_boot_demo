package com.rihao.property.modules.ent.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @date 2021-04-11
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EntUsedNameVo implements Serializable {

    private Long id;
    private String usedName;
    private String duration;
}