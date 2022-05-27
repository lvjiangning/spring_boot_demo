package com.rihao.property.modules.estate.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.BuildQueryParam;
import com.rihao.property.modules.estate.service.IBuildingService;
import com.rihao.property.modules.estate.vo.BuildingCreateVo;
import com.rihao.property.modules.estate.vo.BuildingUpdateVo;
import com.rihao.property.modules.estate.vo.BuildingVo;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.modules.system.vo.SysUserVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.Api;
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
@RequestMapping("/api/building")
@Api("楼栋管理")
public class BuildingController extends BaseController {

    private IBuildingService buildingService;
    private ISysUserService userService;

    @Log("楼栋列表")
    @ApiOperation("楼栋列表")
    @GetMapping("page")
    public ResBody<?> search(BuildQueryParam buildQueryParam) {
        PageVo<BuildingVo> page = this.buildingService.search(buildQueryParam);
        addLog("查询楼栋列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("楼栋详情")
    @ApiOperation("楼栋详情")
    @GetMapping("/{id}")
    public ResBody<?> detail(@PathVariable Long id) {
        BuildingVo vo = this.buildingService.detail(id);
        addLog("查看楼栋详情-" + vo.getName(), JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(vo);
    }

    @Log("楼栋全景信息")
    @ApiOperation("楼栋全景信息")
    @GetMapping("/detail/{id}")
    public ResBody<?> getBuildDetail(@PathVariable Long id) {
        BuildingVo vo = this.buildingService.panorama(id);
        addLog("查看楼栋详情-" + vo.getName(), JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(vo);
    }

    @Log("更新楼栋")
    @PutMapping
    @ApiOperation("更新楼栋")
    public ResBody<?> update(@RequestBody BuildingVo updateVo) {
        boolean result = this.buildingService.update(updateVo);
        if (result) {
            addLog("修改楼栋信息-" + updateVo.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("添加楼栋")
    @PostMapping
    @ApiOperation("添加楼栋")
    public ResBody<?> createNew(@RequestBody BuildingVo createVo) {
        SysUser user = JwtUtil.getCurrentUser();
        if (user.getRoleId() == 1)
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("超级管理员不可进行该操作"));
        boolean result = this.buildingService.createNew(createVo);
        if (result) {
            addLog("添加楼栋-" + createVo.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("添加成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("添加楼栋")
    @PostMapping("/create/full")
    @ApiOperation("添加楼栋全部信息")
    public ResBody<?> createFull(@RequestBody BuildingCreateVo buildingCreateVo) {
        SysUser user = JwtUtil.getCurrentUser();
        if (user.getRoleId() == 1)
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("超级管理员不可进行该操作"));
        boolean result = this.buildingService.createFull(buildingCreateVo);
        if (result) {
            addLog("添加楼栋-" + buildingCreateVo.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("添加成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("修改楼栋")
    @PutMapping("/update/full")
    @ApiOperation("修改楼栋全部信息")
    public ResBody<?> updateFull(@RequestBody BuildingUpdateVo buildingUpdateVo) {
        SysUser user = JwtUtil.getCurrentUser();
        if (user.getRoleId() == 1)
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("超级管理员不可进行该操作"));
        boolean result = this.buildingService.updateFull(buildingUpdateVo);
        if (result) {
            addLog("添加楼栋-" + buildingUpdateVo.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("添加成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("获取权限范围内楼栋列表")
    @GetMapping("query_permission_building_list")
    public ResBody<?> queryPermissionBuildingList() {
        SysUser user = JwtUtil.getCurrentUser();
        Long[] parkIds = new Long[0];
        if (user.getRoleId() == 3) {
            SysUserVo userVo = this.userService.findOne(user.getId());
            parkIds = userVo.getParkIdList();
        }
        return ResBody.success(this.buildingService.queryPermissionBuildingList(parkIds));
    }

    @Log("根据id获取楼栋名称")
    @GetMapping("/getBuildingNameById/{id}")
    public ResBody<?> getBuildingNameById(@PathVariable Long id) {
        return ResBody.success(this.buildingService.getBuildingNameById(id));
    }

    @Log("根据单位ID获取楼栋ky对")
    @GetMapping("getByEstId/{estId}")
    public ResBody<?> getByEstId(@PathVariable Long estId) {
        return ResBody.success(this.buildingService.getByEstId(estId));
    }

    @Log("根据用户权限获取楼栋ky对")
    @GetMapping("permission_list")
    public ResBody<?> getPermissionList() {
        return ResBody.success(this.buildingService.getByEstId(JwtUtil.getCurrentUser().getEstablishId()));
    }

    @Log("删除楼栋")
    @DeleteMapping("/{id}")
    @ApiOperation("删除楼栋")
    public ResBody<?> delete(@PathVariable Long id) {
        boolean result = this.buildingService.delete(id);
        if (result) {
            addLog("删除楼栋-" + id, JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("删除成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @GetMapping("list/{parkId}")
    public ResBody<?> getListByParkId(@PathVariable Long parkId) {
        return ResBody.success(this.buildingService.getListByParkId(parkId));
    }

    @Autowired
    private void setService(IBuildingService buildingService, ISysUserService userService) {
        this.buildingService = buildingService;
        this.userService = userService;
    }
}
