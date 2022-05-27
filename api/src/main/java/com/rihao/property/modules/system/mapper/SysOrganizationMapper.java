package com.rihao.property.modules.system.mapper;

import com.rihao.property.modules.system.entity.SysOrganization;
import com.rihao.property.modules.system.vo.SysOrganizationListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 单位表 Mapper 接口
 * </p>
 *
 * @author ken
 * @since 2020-05-18
 */
public interface SysOrganizationMapper extends BaseMapper<SysOrganization> {

    Page<SysOrganizationListVo> selectByQueryParam(Page<SysOrganization> page, @Param("params") SysOrganizationMapper.QueryParam params);

    @Data
    @Accessors(chain = true)
    class QueryParam{
        private String name;
        private String code;
    }

}
