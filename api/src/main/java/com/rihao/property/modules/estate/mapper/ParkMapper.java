package com.rihao.property.modules.estate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.vo.ParkVo;
import com.rihao.property.modules.report.vo.ParkStatisticVo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-07-05
 */
public interface ParkMapper extends BaseMapper<Park> {

    Page<ParkVo> selectByQueryParam(Page<Park> page, QueryParam params);

    ParkStatisticVo statisticParks();

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private String name;
        private String parkIds;
    }
}
