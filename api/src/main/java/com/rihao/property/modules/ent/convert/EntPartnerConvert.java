package com.rihao.property.modules.ent.convert;

import com.rihao.property.modules.ent.vo.PartnerVo;
import com.rihao.property.modules.ent.entity.EntPartner;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-04-06
 * @description
 */
@Mapper
public interface EntPartnerConvert {

    EntPartnerConvert INSTANCE = Mappers.getMapper(EntPartnerConvert.class);

    EntPartner createParam2Entity(PartnerVo partnerVo);
}