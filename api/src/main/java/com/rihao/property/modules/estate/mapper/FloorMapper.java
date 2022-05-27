package com.rihao.property.modules.estate.mapper;

import com.rihao.property.modules.estate.entity.Floor;
import com.rihao.property.modules.estate.vo.FloorVo;
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
 * @since 2021-03-28
 */
public interface FloorMapper extends BaseMapper<Floor> {

    Page<FloorVo> selectByQueryParam(Page<Floor> page, QueryParam params);

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private Long buildingId;
        private String floorNo;
    }
}
