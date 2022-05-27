package com.rihao.property.modules.estate.vo;

import com.rihao.property.modules.config.entity.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @date 2021-07-05
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ParkVo extends ParkCreateVo {

    private Long id;
    private String estName;
    private String contractTempUrl;
    private int buildingAmount;
    private String managementFee;
    private List<ParkFileVo> fileList;
    private List<BuildingVo> buildingList;
    private List<PurchaseRecordVo> purchaseRecordVoList;
    private LocalDateTime createTime;
    private String createBy;
    private List<SysFile> files;

    @ApiModelProperty("空闲单元数")
    private Integer freeUnitNum;
    @ApiModelProperty("空闲单元面积")
    private BigDecimal freeUnitArea;
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