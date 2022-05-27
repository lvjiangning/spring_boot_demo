package com.rihao.property.modules.lease.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rihao.property.modules.lease.contract.entity.ContractFile;
import com.rihao.property.modules.lease.contract.vo.ContractFileVo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-07-12
 */
public interface ContractFileMapper extends BaseMapper<ContractFile> {


    Page<ContractFileVo> selectByQueryParam(Page<ContractFile> page, QueryParam params);

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private Long contractId;
    }
}
