package com.rihao.property.modules.estate.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.estate.controller.params.FloorQueryParam;
import com.rihao.property.modules.estate.entity.Floor;
import com.rihao.property.modules.estate.vo.FloorVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
public interface IFloorService extends IService<Floor> {

    PageVo<FloorVo> search(FloorQueryParam floorQueryParam);

    Boolean createNew(FloorVo createVo);

    Boolean update(FloorVo updateVo);

    String getFloorNoById(Long id);

    List<FloorVo> getByBuildingId(Long buildingId);

    Floor getByName(String floorName);

    void validFloorName(String floorNo, Long buildingId, Long id);

    Floor getByBuildingIdAndFloorNo(Long buildingId, String floorNo);

    List<KeyValueVo> getAllFloorByBuildingId(Long id);
}
