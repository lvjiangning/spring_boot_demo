package com.rihao.property.modules.system.convert;

import com.rihao.property.modules.system.entity.SysOrganization;
import com.rihao.property.modules.system.vo.SysOrganizationCreateVo;
import com.rihao.property.modules.system.vo.SysOrganizationVo;
import com.rihao.property.modules.system.vo.SysOrganizationListVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Ken
 * @date 2020/5/18
 * @description
 */
@Mapper
public interface SysOrganizationConvert {

    SysOrganizationConvert INSTANCE = Mappers.getMapper(SysOrganizationConvert.class);

    List<SysOrganizationListVo> entity2ListItemBatch(List<SysOrganization> list);

    SysOrganization createParam2Entity(SysOrganizationCreateVo createVo);

    SysOrganization updateParam2Entity(SysOrganizationVo updateVo);

    SysOrganizationVo entity2Vo(SysOrganization entity);
}
