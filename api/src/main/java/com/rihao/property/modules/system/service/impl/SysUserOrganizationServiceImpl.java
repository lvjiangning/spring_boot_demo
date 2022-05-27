package com.rihao.property.modules.system.service.impl;

import com.rihao.property.constant.CommonConstant;
import com.rihao.property.modules.common.vo.PermissionControlVo;
import com.rihao.property.modules.system.entity.SysOrganization;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.entity.SysUserOrganization;
import com.rihao.property.modules.system.mapper.SysOrganizationMapper;
import com.rihao.property.modules.system.mapper.SysUserOrganizationMapper;
import com.rihao.property.modules.system.service.ISysUserOrganizationService;
import com.rihao.property.modules.system.vo.SysUserOrganizationVo;
import com.rihao.property.shiro.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 用户单位关联表 服务实现类
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
@Service
public class SysUserOrganizationServiceImpl extends ServiceImpl<SysUserOrganizationMapper, SysUserOrganization> implements ISysUserOrganizationService {

    private SysOrganizationMapper organizationMapper;

    @Override
    public SysUserOrganizationVo getGrantOrganizations(Long id) {
        SysUserOrganizationVo vo = new SysUserOrganizationVo();
        SysUserOrganization entity = this.getOne(new QueryWrapper<SysUserOrganization>().lambda().eq(SysUserOrganization::getUserId, id));
        if (entity == null) {
            return vo;
        }
        vo.setUserId(entity.getUserId());
        if (!StringUtils.isEmpty(entity.getOrganizationIds())) {
            vo.setOrganizationIds((Long[]) ConvertUtils.convert(entity.getOrganizationIds().split(","), Long.class));
        }
        return vo;
    }

    @Override
    public boolean grantOrganizations(SysUserOrganizationVo userOrganization) {
        //  先删除再授权
        this.remove(new QueryWrapper<SysUserOrganization>().lambda().eq(SysUserOrganization::getUserId, userOrganization.getUserId()));

        SysUserOrganization entity = new SysUserOrganization();
        entity.setUserId(userOrganization.getUserId());
        entity.setOrganizationIds(StringUtils.arrayToDelimitedString(userOrganization.getOrganizationIds(), ","));
        return this.save(entity);
    }

    @Override
    public PermissionControlVo getCurrentUserOrganizationIds() {
        PermissionControlVo vo = new PermissionControlVo();
        SysUser currentUser = JwtUtil.getCurrentUser();
        vo.setRoleId(currentUser.getRoleId());
        if (CommonConstant.DEFAULT_ADMIN_ROLE.equals(currentUser.getRoleId())) {
            List<SysOrganization> organizations = this.organizationMapper.selectList(null);
            if (CollectionUtils.isEmpty(organizations)) {
                return vo;
            }
            long[] organizationIds = organizations.stream().mapToLong(SysOrganization::getId).toArray();
            vo.setOrganizationIds(organizationIds);
            return vo;
        }
        SysUserOrganization userOrganization = this.getOne(new QueryWrapper<SysUserOrganization>().lambda().eq(SysUserOrganization::getUserId, currentUser.getId()));
        if (userOrganization == null) {
            return vo;
        }
        long[] organizationIds = (long[]) ConvertUtils.convert(userOrganization.getOrganizationIds().split(","), long.class);
        vo.setOrganizationIds(organizationIds);
        return vo;
    }

    @Autowired
    public void setOrganizationMapper(SysOrganizationMapper organizationMapper) {
        this.organizationMapper = organizationMapper;
    }

}
