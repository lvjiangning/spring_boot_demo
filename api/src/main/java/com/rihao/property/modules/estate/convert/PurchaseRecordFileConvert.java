package com.rihao.property.modules.estate.convert;

import com.rihao.property.modules.estate.vo.PurchaseRecordFileCreateVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.estate.entity.PurchaseRecordFile;
import com.rihao.property.modules.estate.vo.PurchaseRecordFileVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-07-08
 * @description
 */
@Mapper
public interface PurchaseRecordFileConvert {

    PurchaseRecordFileConvert INSTANCE = Mappers.getMapper(PurchaseRecordFileConvert.class);

    List<PurchaseRecordFileVo> entity2ListItemBatch(List<PurchaseRecordFile> list);

    PurchaseRecordFile createVo2Entity(PurchaseRecordFileCreateVo purchaseRecordFileCreateVo);
}