package com.rihao.property.modules.inspection.mapper;

import com.rihao.property.modules.inspection.entity.Inspection;
import com.rihao.property.modules.inspection.vo.InspectionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-03-27
 */
public interface InspectionMapper extends BaseMapper<Inspection> {

    Page<InspectionVo> selectByQueryParam(Page<Inspection> page, @Param("params") QueryParam params);

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private String location;
        private String inspector;
        private String title;
        private Long establishId;
        private String parkIds;
        private String likeParam;
    }
}
