package com.rihao.property.modules.ent.mapper;

import com.rihao.property.modules.ent.entity.EntMatters;
import com.rihao.property.modules.ent.vo.EntMattersVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-03-29
 */
public interface EntMattersMapper extends BaseMapper<EntMatters> {

    Page<EntMattersVo> selectByQueryParam(Page<EntMatters> page, QueryParam params);

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private String entId;
    }
}
