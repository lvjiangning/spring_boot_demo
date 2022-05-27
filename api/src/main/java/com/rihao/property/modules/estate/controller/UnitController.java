package com.rihao.property.modules.estate.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageParams;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.UnitQueryParam;
import com.rihao.property.modules.estate.service.IUnitService;
import com.rihao.property.modules.estate.vo.*;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
@RestController
@RequestMapping("/api/unit")
public class UnitController extends BaseController {

    private final IUnitService unitService;

    public UnitController(IUnitService unitService) {
        this.unitService = unitService;
    }

    @Log("单元列表")
    @GetMapping("page")
    @ApiOperation("单元列表")
    public ResBody<?> search(UnitQueryParam unitQueryParam) {
        PageVo<UnitVo> page = this.unitService.search(unitQueryParam);
        addLog("查看单元列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("单元详情")
    @GetMapping("/{id}")
    @ApiOperation("单元详情")
    public ResBody<?> detail(@PathVariable Long id) {
        UnitVo vo = this.unitService.detail(id);
        addLog("查看单元详情-" + id, JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(vo);
    }

    @Log("单元详情")
    @GetMapping("/detail/{id}")
    @ApiOperation("单元详情")
    public ResBody<?> panorama(@PathVariable Long id) {
        UnitVo vo = this.unitService.panorama(id);
        addLog("查看单元详情-" + id, JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(vo);
    }

    @Log("所有单元列表")
    @GetMapping("all_list")
    @ApiOperation("所有单元列表")
    public ResBody<?> all_list(UnitQueryParam unitQueryParam) {
        PageVo<UnitVo> page = this.unitService.searchAllList(unitQueryParam);
        addLog("查看楼栋所有单元列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("查询所有单元的建筑面积与使用面积汇总")
    @GetMapping("sumCount")
    @ApiOperation("查询所有单元的建筑面积与使用面积汇总")
    public ResBody<Map<String, Double>> sumCount(UnitQueryParam unitQueryParam) {
        Map<String, Double> allArea = this.unitService.searchAllArea(unitQueryParam);
        addLog("查询所有单元的建筑面积与使用面积汇总", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(allArea);
    }

    @Log("空置单元列表")
    @GetMapping("free_list")
    @ApiOperation("空置单元列表")
    public ResBody<?> free_list(Long buildingId) {
        List<UnitKeyValueVo> page = this.unitService.searchFreeList(buildingId);
        addLog("查看空置单元列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("获取楼层空置单元列表")
    @GetMapping("/floor/free_list/{floorId}")
    @ApiOperation("空置单元列表")
    public ResBody<?> floor_free_list(@PathVariable Long floorId) {
        List<UnitVo> page = this.unitService.searchFreeListByFloorId(floorId);
        addLog("查看楼层空置单元列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    /**
     *
     * @param buildingId
     * @param unitState free 未租赁，busy 租赁中
     * @return
     */
    @Log("租赁单元列表")
    @GetMapping("rent_list")
    @ApiOperation("租赁单元列表")
    public ResBody<?> rent_list(Long buildingId,String unitState) {
        List<UnitKeyValueVo> page = this.unitService.searchRentList(buildingId,unitState);
        addLog("查看租赁单元列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("合同单元列表")
    @GetMapping("contract_unit_list")
    @ApiOperation("合同单元列表")
    public ResBody<?> contract_unit_list(Long contractId) {
        List<UnitKeyValueVo> page = this.unitService.searchContractUnitList(contractId);
        return ResBody.success(page);
    }

    @Log("查询单元面积")
    @GetMapping("get_area")
    public ResBody<?> getArea(Long buildingId, String unitNames) {
        String area = this.unitService.getAreaByBuildingIdAndUnitNames(buildingId, unitNames);
        return ResBody.success(area);
    }

    @Log("查询单元面积")
    @GetMapping("get_area_by_ids")
    public ResBody<?> getAreaByIds(String unitIds) {
        String area = this.unitService.getAreaByUnitIds(unitIds);
        return ResBody.success(area);
    }

    @Log("新建单元")
    @PostMapping
    public ResBody<?> createNew(@RequestBody UnitCreateVo createVo) {
        Boolean result = this.unitService.createNew(createVo);
        if (result) {
            return ResBody.success();
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("修改单元")
    @PutMapping
    public ResBody<?> update(@RequestBody UnitUpdateVo updateVo) {
        Boolean result = this.unitService.update(updateVo);
        if (result) {
            return ResBody.success();
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("根据楼层ID查询单元列表")
    @GetMapping("/list/{floorId}")
    public ResBody<?> getUnitListByFloorId(@PathVariable Long floorId) {
        return ResBody.success(this.unitService.getByFloorId(floorId));
    }

    @Log("拆分单元")
    @PostMapping("/split")
    public ResBody<?> split(@RequestBody UnitSplitVo unitSplitVo) {
        Boolean result = this.unitService.split(unitSplitVo);
        if (result) {
            return ResBody.success();
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("合并单元")
    @PostMapping("/merge")
    public ResBody<?> merge(@RequestBody UnitMergeVo unitMergeVo) {
        Boolean result = this.unitService.merge(unitMergeVo);
        if (result) {
            return ResBody.success();
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @GetMapping("/relateHistory")
    public ResBody<?> relateHistory(PageParams params) {
        return ResBody.success(this.unitService.relateHistory(params));
    }

    @DeleteMapping("/{id}")
    public ResBody<?> delete(@PathVariable Long id) {
        return ResBody.success(this.unitService.deleteById(id));
    }
}

