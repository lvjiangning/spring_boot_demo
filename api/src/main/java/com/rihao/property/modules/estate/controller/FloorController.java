package com.rihao.property.modules.estate.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.FloorQueryParam;
import com.rihao.property.modules.estate.service.IFloorService;
import com.rihao.property.modules.estate.vo.FloorVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
@RestController
@RequestMapping("/api/floor")
public class FloorController extends BaseController {

    private IFloorService floorService;

    @Log("楼层列表")
    @GetMapping("page")
    @ApiOperation("楼层列表")
    public ResBody<?> search(FloorQueryParam floorQueryParam) {
        PageVo<FloorVo> page = this.floorService.search(floorQueryParam);
        addLog("查看楼层列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("楼层详细")
    @GetMapping("/{id}")
    public ResBody<?> detail(@PathVariable Long id) {
        return ResBody.success(this.floorService.getById(id));
    }

    @Log("添加楼层")
    @PostMapping
    public ResBody<?> createNew(@RequestBody FloorVo createVo) {
        Boolean result = this.floorService.createNew(createVo);
        if (result) {
            return ResBody.success();
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("修改楼层")
    @PutMapping
    public ResBody<?> update(@RequestBody FloorVo updateVo) {
        Boolean result = this.floorService.update(updateVo);
        if (result) {
            return ResBody.success();
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("根据ID获取楼层号")
    @GetMapping("/getFloorNameById/{id}")
    public ResBody<?> getFloorNameById(@PathVariable Long id) {
        return ResBody.success(this.floorService.getFloorNoById(id));
    }

    @GetMapping("/all_floor/{id}")
    public ResBody<?> getAllFloorByBuildingId(@PathVariable Long id){
        return ResBody.success(this.floorService.getAllFloorByBuildingId(id));
    }

    @Autowired
    private void setService(IFloorService floorService) {
        this.floorService = floorService;
    }
}