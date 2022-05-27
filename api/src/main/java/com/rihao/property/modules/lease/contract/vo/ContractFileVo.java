package com.rihao.property.modules.lease.contract.vo;

import com.rihao.property.modules.estate.vo.ContractFileCreateVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @date 2021-07-12
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ContractFileVo extends ContractFileCreateVo {

    private Long id;
    private String contractCode;
}