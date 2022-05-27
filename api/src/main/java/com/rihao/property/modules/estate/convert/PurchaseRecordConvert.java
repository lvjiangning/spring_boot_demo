package com.rihao.property.modules.estate.convert;

import com.rihao.property.modules.estate.entity.PurchaseRecord;
import com.rihao.property.modules.estate.vo.PurchaseRecordVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-04-09
 * @description
 */
@Mapper
public interface PurchaseRecordConvert {

    PurchaseRecordConvert INSTANCE = Mappers.getMapper(PurchaseRecordConvert.class);

    List<PurchaseRecordVo> entity2ListItemBatch(List<PurchaseRecord> list);

    PurchaseRecord createParam2Entity(PurchaseRecordVo purchaseRecordVo);

    PurchaseRecordVo entity2Vo(PurchaseRecord byId);
}