package com.rihao.property.modules.system.mapper;

import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.vo.SysUserVo;
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
 * @author gaoy
 * @since 2020-02-26
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    Page<SysUserVo> selectByAdminQueryParam(Page<SysUser> page, @Param("params") AdminQueryParam params);


    @Data
    @Accessors(chain = true)
    class AdminQueryParam{
        private String username;
        private String realName;
        private Long role;
        private String establishId;
    }
}
