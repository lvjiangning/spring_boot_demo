package com.rihao.property.modules.estate.mapper;

import com.rihao.property.modules.estate.controller.params.BuildQueryParam;
import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.vo.BuildingVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rihao.property.modules.lease.contract.dto.BuildingBasicDTO;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
public interface BuildingMapper extends BaseMapper<Building> {

    Page<BuildingVo> selectByQueryParam(Page<Building> page, QueryParam params);

    BuildingBasicDTO selectBasic(Long buildingId);

    Page<Building> findBuildingByLikeName(Page<Building> page, BuildQueryParam params);


    @Data
    @Accessors(chain = true)
    class QueryParam {
        private String name;
        private String buildingIds;
        private Long establishId;
    }
}
