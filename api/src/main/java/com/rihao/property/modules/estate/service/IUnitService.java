package com.rihao.property.modules.estate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.common.page.PageParams;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.UnitQueryParam;
import com.rihao.property.modules.estate.entity.Floor;
import com.rihao.property.modules.estate.entity.Unit;
import com.rihao.property.modules.estate.enums.UnitUseType;
import com.rihao.property.modules.estate.vo.*;
import com.rihao.property.modules.report.controller.params.UseHouseDetailQueryParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
public interface IUnitService extends IService<Unit> {

    PageVo<UnitVo> search(UnitQueryParam unitQueryParam);

    UnitVo detail(Long id);

    String getAreaByBuildingIdAndUnitNames(Long buildingId, String unitNames);

    Unit getUnitByBuildingIdAndName(Long buildingId, String unitName);

    Unit getUnitByName(String name);

    List<UnitKeyValueVo> searchFreeList(Long buildingId);

    List<UnitKeyValueVo> searchContractUnitList(Long contractId);

    List<UnitKeyValueVo> searchFreeListByContract(Long buildingId, Long contractId);

    Boolean createNew(UnitCreateVo createVo);

    Boolean update(UnitUpdateVo updateVo);

    public Boolean createUpdateLog(UnitUpdateVo updateVo,Unit oldUnit);

    String getAreaByUnitIds(String unitIds);

    List<UnitVo> getByFloorId(Long floorId);

    Boolean split(UnitSplitVo unitSplitVo);

    Boolean merge(UnitMergeVo unitMergeVo);

    List<UnitVo> searchFreeListByFloorId(Long floorId);

    void countFloorInfo(Floor floor);

    List<UnitKeyValueVo> searchRentList(Long buildingId,String unitState);

    UnitVo panorama(Long id);

    String getUseTypeName(UnitUseType unitUseType);

    PageVo<SplitMergeHistoryVo> relateHistory(PageParams params);

    void validUnitName(String unitNo, Long floorId, Long id);

    List<Unit> getAllListByBuildingId(Long buildingId);

    List<Unit> getFreeListByBuildingId(List<Long> buildingId);

    PageVo<UnitVo> searchAllList(UnitQueryParam unitQueryParam);

    /**
     * 查询单元信息的面积汇总，配合searchAllList 使用
     * @param unitQueryParam
     * @return
     */
    Map<String, Double> searchAllArea(UnitQueryParam unitQueryParam);

    /**
     * 通过使用类型 ，进行参数中的面积汇总，租金rent 目前固定60,空置面积 =当前空闲的面积
     * @param vos
     * @return
     */
    Map<String,Double> useTypeAreaSumAndRent(List<UnitVo> vos);

    Boolean deleteById(Long id);

    /**
     * 查询用房明细报表数据
     * @param useHouseDetailQueryParam
     */
    PageVo<UnitVo> useHouseDetailReport(UseHouseDetailQueryParam useHouseDetailQueryParam);
}