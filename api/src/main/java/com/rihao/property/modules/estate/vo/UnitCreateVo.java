package com.rihao.property.modules.estate.vo;

import com.rihao.property.modules.estate.enums.RentType;
import com.rihao.property.modules.estate.enums.UnitUseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UnitCreateVo implements Serializable {

    @ApiModelProperty("单元号")
    private String unitNo;

    @ApiModelProperty("使用面积")
    private String usableArea;

    @ApiModelProperty("建筑面积")
    private String builtUpArea;

    @ApiModelProperty("自用、公用、物业用房、租赁")
    private UnitUseType useType;

    @ApiModelProperty("配套租赁 产业租赁")
    private RentType rentType;

    @ApiModelProperty("自用、公用、物业用房、租赁")
    private String useTypeName;

    //@ApiModelProperty("配套租赁 产业租赁")
    //private String rentTypeName;

    @ApiModelProperty("楼栋ID")
    private Long buildingId;

    @ApiModelProperty("楼层ID")
    private Long floorId;

    @ApiModelProperty("楼层号")
    private String floorNo;

    public String getUseTypeLable(){
            switch (this.getUseType()) {
                case for_self:
                    return  "自用";
                case for_public:
                    return "公用";
                case for_estate:
                    return "物业用房";
                case industry:
                    return "产业租赁";
                case set:
                    return "配套租赁";
            }
            return null;
    }
}
