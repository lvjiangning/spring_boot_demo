package com.rihao.property.modules.estate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.estate.controller.params.BuildQueryParam;
import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.vo.*;
import com.rihao.property.modules.lease.contract.dto.BuildingBasicDTO;

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
public interface IBuildingService extends IService<Building> {

    PageVo<BuildingVo> search(BuildQueryParam buildQueryParam);

    BuildingVo detail(Long id);

    boolean update(BuildingVo updateVo);

    List<BuildingKeyValueVo> queryPermissionBuildingList(Long[] parkIds);

    String getBuildingNameById(Long id);

    boolean createNew(BuildingVo createVo);

    Building getBuildingByName(String buildingName);

    List<BuildingKeyValueVo> getByEstId(Long estId);

    BuildingVo panorama(Long id);

    void countBuildingInfo(Long buildingId);

    List<Building> getByParkId(Long parkId);

    boolean createFull(BuildingCreateVo buildingCreateVo);

    boolean updateFull(BuildingUpdateVo buildingUpdateVo);

    boolean delete(Long id);

    Building getBuildingByNameAndParkId(String buildingName, Long parkId);

    List<KeyValueVo> getListByParkId(Long parkId);

    BuildingBasicDTO selectBasic(Long buildingId);

}
