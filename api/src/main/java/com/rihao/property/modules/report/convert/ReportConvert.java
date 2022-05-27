package com.rihao.property.modules.report.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-31
 * @description
 */
@Mapper
public interface ReportConvert {

    ReportConvert INSTANCE = Mappers.getMapper(ReportConvert.class);
}