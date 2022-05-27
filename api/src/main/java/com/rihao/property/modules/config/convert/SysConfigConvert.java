package com.rihao.property.modules.config.convert;

import com.rihao.property.modules.config.entity.SysConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.config.vo.SysConfigVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-04-21
 * @description
 */
@Mapper
public interface SysConfigConvert {

    SysConfigConvert INSTANCE = Mappers.getMapper(SysConfigConvert.class);

    List<SysConfigVo> entity2ListItemBatch(List<SysConfig> list);

    SysConfig updateParam2Entity(SysConfigVo configVo);
}