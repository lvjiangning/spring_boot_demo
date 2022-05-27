package com.rihao.property.modules.report.mapper;

import com.rihao.property.modules.report.vo.LandUseReportVo;
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
 * @since 2021-03-31
 */
public interface ReportMapper extends BaseMapper<LandUseReportVo> {

    Page<LandUseReportVo> selectLanUseReportByQueryParam(Page<LandUseReportVo> page, QueryParam params);

    @Data
    @Accessors(chain = true)
    class QueryParam {
    }
}
