package com.rihao.property.modules.estate.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
public class BuildingVo implements Serializable {

    private Long id;
    private Long estId;
    private Long parkId;
    private String estName;
    private String parkName;
    private String name;
    private String floorAmount;
    private String totalArea;
    private String coveredArea;
    private String builtUpArea;
    private String usableArea;
    private int allUnitAmount;
    private int freeUnitAmount;
    private List<FloorVo> floorList;
    private List<UnitVo> units;
    private LocalDateTime createTime;
    private String createBy;
    /**
     * rent 租金
     * freeArea 空闲面积
     *  for_self:
     *  *                 自用
     *  *              for_public:
     *  *                 公用
     *  *              for_estate:
     *  *                 物业用房
     *  *              industry:
     *  *                 产业租赁
     *  *              set:
     *  *                 配套租赁
     */
    private Map<String,Double> areaMap;//存放单元使用性质的面积合计，租金rent 固定60
}