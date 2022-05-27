package com.rihao.property.modules.estate.mapper;

import com.rihao.property.modules.estate.entity.Unit;
import com.rihao.property.modules.estate.vo.UnitVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rihao.property.modules.report.vo.ParkStatisticVo;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
public interface UnitMapper extends BaseMapper<Unit> {

    Page<UnitVo> selectByQueryParam(Page<Unit> page, QueryParam params);

    Map<String,Double>  selectByQueryParamSum(@Param("params") UnitMapper.QueryParam params);

    ParkStatisticVo.UnitSettleStatisticVo statisticUnitSettle();

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private Long parkId;
        private Long floorId;
        private String status;
        private Long buildingId;
        private String unitNo;
        private String delFlag;
        private String parkIds;
        private String likeParam;
    }
}
