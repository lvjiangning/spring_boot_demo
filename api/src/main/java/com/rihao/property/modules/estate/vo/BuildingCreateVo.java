package com.rihao.property.modules.estate.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @description
 * @since 2021-03-28
 */
@Data
@ApiModel
@Accessors(chain = true)
public class BuildingCreateVo implements Serializable {

    private String name;
    private Long parkId;
    private List<UnitCreateVo> unitList;
}