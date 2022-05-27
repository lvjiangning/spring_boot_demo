package com.rihao.property.modules.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rihao.property.modules.config.convert.SysConfigConvert;
import com.rihao.property.modules.config.entity.ListInfo;
import com.rihao.property.modules.config.entity.SysConfig;
import com.rihao.property.modules.config.mapper.ListInfoMapper;
import com.rihao.property.modules.config.mapper.SysConfigMapper;
import com.rihao.property.modules.config.service.IListInfoService;
import com.rihao.property.modules.config.service.ISysConfigService;
import com.rihao.property.modules.config.vo.ListInfoVo;
import com.rihao.property.modules.config.vo.SysConfigVo;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.shiro.util.JwtTokenUtil;
import com.rihao.property.shiro.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统参数设置 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-21
 */
@Service
public class ListInfoImpl extends ServiceImpl<ListInfoMapper, ListInfo> implements IListInfoService {

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public String getListInfo(ListInfoVo listInfoVo) {
        if (listInfoVo == null){
            return "查询失败：参数为空！";
        }
        if (StringUtils.isBlank(listInfoVo.getListName())){
            return "查询失败：列表名参数为空！";
        }

        String userName = JwtUtil.getUsername(JwtTokenUtil.getToken());
        SysUser sysUserByUsername = sysUserService.getSysUserByUsername(userName);

        ListInfo listInfo = this.getBaseMapper().selectOne(new QueryWrapper<ListInfo>().lambda().eq(ListInfo::getListName, listInfoVo.getListName())
                .eq(ListInfo::getUserId, sysUserByUsername.getId()));
        if (listInfo != null){
            return listInfo.getListInfo();
        }
        return null;
    }

    @Override
    public String save(ListInfoVo listInfoVo) {
        if (listInfoVo == null){
            return "新增失败：参数为空！";
        }
        if (StringUtils.isBlank(listInfoVo.getListName())){
            return "新增失败：列表名参数为空！";
        }
        if (StringUtils.isBlank(listInfoVo.getListInfo())){
            return "新增失败：列表明细参数为空！";
        }
        String userName = JwtUtil.getUsername(JwtTokenUtil.getToken());
        SysUser sysUserByUsername = sysUserService.getSysUserByUsername(userName);
        //先删除当前用户之前保存的，再新增
        this.getBaseMapper().delete(new QueryWrapper<ListInfo>().lambda().eq(ListInfo::getListName, listInfoVo.getListName())
                    .eq(ListInfo::getUserId, sysUserByUsername.getId()));

        ListInfo listInfo=new ListInfo();
        listInfo.setListInfo(listInfoVo.getListInfo());
        listInfo.setListName(listInfoVo.getListName());
        listInfo.setUserId(sysUserByUsername.getId());
        this.getBaseMapper().insert(listInfo);
        return null;
    }
}
