package com.rihao.property.modules.system.component;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.rihao.property.modules.system.entity.SysOrganization;
import com.rihao.property.modules.system.mapper.SysOrganizationMapper;
import com.rihao.property.modules.system.vo.SysOrganizationImportVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.StringJoiner;

/**
 * @author Ken
 * @date 2020/6/7
 * @description
 */
@Component
public class SysOrganizationImportVerifyHandler implements IExcelVerifyHandler<SysOrganizationImportVo> {

    private SysOrganizationMapper organizationMapper;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(SysOrganizationImportVo organizationImportVo) {
        StringJoiner joiner = new StringJoiner(",");

        SysOrganization organization = this.organizationMapper.selectOne(new QueryWrapper<SysOrganization>().lambda().eq(SysOrganization::getName, organizationImportVo.getName()));
        if (organization != null) {
            joiner.add("单位名称已存在");
        }

        if (!StringUtils.isEmpty(organizationImportVo.getParentName())) {
            SysOrganization parentOrganization = this.organizationMapper.selectOne(new QueryWrapper<SysOrganization>().lambda().eq(SysOrganization::getName, organizationImportVo.getParentName()));
            if (organization == null) {
                joiner.add("上级单位名称不存在");
            }
        }

        if (joiner.length() != 0) {
            return new ExcelVerifyHandlerResult(false, joiner.toString());
        }
        return new ExcelVerifyHandlerResult(true);
    }


    @Autowired
    public void setOrganizationMapper(SysOrganizationMapper organizationMapper) {
        this.organizationMapper = organizationMapper;
    }
}
