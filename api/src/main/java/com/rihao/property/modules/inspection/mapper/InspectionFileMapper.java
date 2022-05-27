package com.rihao.property.modules.inspection.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rihao.property.modules.inspection.entity.InspectionFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rihao.property.modules.inspection.vo.InspectionFileVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-08-13
 */
public interface InspectionFileMapper extends BaseMapper<InspectionFile> {

   Integer deleteFileByNotExist(List<Long> ids, Long inspectionId);

    Page<InspectionFileVo> selectByQueryParam(Page<InspectionFile> page, QueryParam params);

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private Long inspectionId;
    }
}
