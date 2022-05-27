package com.rihao.property.modules.close_history.convert;

import com.rihao.property.modules.close_history.entity.ContractCloseHistory;
import com.rihao.property.modules.close_history.vo.ContractCloseHistoryVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
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
public interface ContractCloseHistoryConvert {

    ContractCloseHistoryConvert INSTANCE = Mappers.getMapper(ContractCloseHistoryConvert.class);

    List<ContractCloseHistoryVo> entity2ListItemBatch(List<ContractCloseHistory> list);

    ContractCloseHistoryVo entity2Vo(ContractCloseHistory byContractId);
}