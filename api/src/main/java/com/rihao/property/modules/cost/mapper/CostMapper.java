package com.rihao.property.modules.cost.mapper;

import com.rihao.property.modules.cost.entity.Cost;
import com.rihao.property.modules.cost.vo.CostVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-03-30
 */
public interface CostMapper extends BaseMapper<Cost> {

    Page<CostVo> selectByQueryParam(Page<Cost> page, QueryParam params);

    Page<CostVo> selectDeposit(Page<Cost> page, QueryParam params);

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private Long entId;
        private Integer typeId;
        private Long contractId;
        private String costTime;
        private Integer status;
    }
}
