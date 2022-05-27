package com.rihao.property.modules.estate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rihao.property.modules.estate.entity.ParkFile;
import com.rihao.property.modules.estate.vo.ParkFileVo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-06-11
 */
public interface ParkFileMapper extends BaseMapper<ParkFile> {

    Page<ParkFileVo> selectByQueryParam(Page<ParkFile> page, QueryParam params);

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private Long parkId;
    }
}
