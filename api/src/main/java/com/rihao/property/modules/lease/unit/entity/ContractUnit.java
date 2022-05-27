package com.rihao.property.modules.lease.unit.entity;

import com.rihao.property.common.BaseEntity;
import com.rihao.property.modules.lease.unit.enums.ContractUnitState;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同与单元关联
 * </p>
 *
 * @author wangyu
 * @since 2021-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_contract_unit")
public class ContractUnit extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("contract_id")
    private Long contractId;

    @TableField("unit_id")
    private Long unitId;

    /**
     * 状态 0 关闭 1 正常
     */
    @TableField("status")
    private ContractUnitState status;
    //合同到期日期
    @TableField("durdate")
    private String durdate;
    //楼栋id
    @TableField("building_id")
    private Long buildingId;

    //排序
    @TableField("sort")
    private Integer sort;
}
