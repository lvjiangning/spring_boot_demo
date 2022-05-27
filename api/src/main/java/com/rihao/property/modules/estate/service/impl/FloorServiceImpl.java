package com.rihao.property.modules.estate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.estate.controller.params.FloorQueryParam;
import com.rihao.property.modules.estate.convert.FloorConvert;
import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.entity.Floor;
import com.rihao.property.modules.estate.mapper.FloorMapper;
import com.rihao.property.modules.estate.service.IBuildingService;
import com.rihao.property.modules.estate.service.IFloorService;
import com.rihao.property.modules.estate.vo.FloorVo;
import com.rihao.property.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements IFloorService {

    @Autowired
    @Lazy
    private IBuildingService buildingService;


    @Override
    public PageVo<FloorVo> search(FloorQueryParam floorQueryParam) {
        Page<Floor> page = new Page<>(floorQueryParam.getCurrent(), floorQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.asc("floor_no")
        ));
        FloorMapper.QueryParam params = new FloorMapper.QueryParam();
        if (StringUtils.hasText(floorQueryParam.getBuildingId().toString())) {
            params.setBuildingId(floorQueryParam.getBuildingId());
        }
        if (StringUtils.hasText(floorQueryParam.getFloorNo())) {
            params.setFloorNo(floorQueryParam.getFloorNo());
        }
        Page<FloorVo> result = this.getBaseMapper().selectByQueryParam(page, params);

        return PageVo.create(floorQueryParam.getCurrent(), floorQueryParam.getPageSize(),
                result.getTotal(), result.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createNew(FloorVo createVo) {
        Building building = this.buildingService.getBuildingByName(createVo.getBuildingName());
        this.validFloorName(createVo.getFloorNo(), building.getId(), createVo.getId());
        Floor floor = new Floor();
        floor.setFloorNo(createVo.getFloorNo());
        floor.setBuildingId(building.getId());
        floor.setBuildingName(building.getName());
        Boolean result = this.save(floor);

        this.buildingService.countBuildingInfo(building.getId());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(FloorVo updateVo) {
        this.validFloorName(updateVo.getFloorNo(), updateVo.getBuildingId(), updateVo.getId());
        Floor floor = this.getById(updateVo.getId());
        floor.setFloorNo(updateVo.getFloorNo());
        Boolean result = this.updateById(floor);
        Building building = this.buildingService.getById(floor.getBuildingId());
        this.buildingService.countBuildingInfo(building.getId());
        return result;
    }

    @Override
    public String getFloorNoById(Long id) {
        return this.getById(id).getFloorNo();
    }

    @Override
    public List<FloorVo> getByBuildingId(Long buildingId) {
        QueryWrapper<Floor> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Floor::getBuildingId, buildingId);
        List<Floor> floorList = this.list(wrapper);
        List<FloorVo> vos = FloorConvert.INSTANCE.entity2ListItemBatch(floorList);
        return vos;
    }

    @Override
    public Floor getByName(String floorName) {
        QueryWrapper<Floor> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Floor::getFloorNo, floorName);
        return this.getOne(wrapper);
    }

    @Override
    public void validFloorName(String floorNo, Long buildingId, Long id) {
        QueryWrapper<Floor> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Floor::getFloorNo, floorNo)
                .eq(Floor::getBuildingId, buildingId);
        if (id != null && id != 0) {
            wrapper.lambda().ne(Floor::getId, id);
        }
        Floor entity = this.getOne(wrapper);
        ValidationUtil.notNull(entity, "楼层", "楼层号", floorNo);
    }

    @Override
    public Floor getByBuildingIdAndFloorNo(Long buildingId, String floorNo) {
        QueryWrapper<Floor> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Floor::getFloorNo, floorNo)
                .eq(Floor::getBuildingId, buildingId);
        return this.getOne(wrapper);
    }

    @Override
    public List<KeyValueVo> getAllFloorByBuildingId(Long id) {
        QueryWrapper<Floor> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Floor::getBuildingId, id);
        List<Floor> floors = this.list(wrapper);
        List<KeyValueVo> list = new ArrayList<>();
        for (Floor floor : floors) {
            KeyValueVo vo = new KeyValueVo();
            vo.setKey(floor.getId());
            vo.setValue(floor.getFloorNo());
            list.add(vo);
        }
        return list;
    }
}
