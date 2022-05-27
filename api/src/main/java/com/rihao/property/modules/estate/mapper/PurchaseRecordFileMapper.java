package com.rihao.property.modules.estate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rihao.property.modules.estate.entity.PurchaseRecordFile;
import com.rihao.property.modules.estate.vo.PurchaseRecordFileVo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-07-08
 */
public interface PurchaseRecordFileMapper extends BaseMapper<PurchaseRecordFile> {

    Page<PurchaseRecordFileVo> selectByQueryParam(Page<PurchaseRecordFile> page, QueryParam params);

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private Long recordId;
    }
}
