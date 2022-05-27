package com.rihao.property.modules.estate.controller;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.ent.controller.params.ParkImport;
import com.rihao.property.modules.estate.controller.params.ParkFileQueryParam;
import com.rihao.property.modules.estate.controller.params.ParkQueryParam;
import com.rihao.property.modules.estate.service.IParkFileService;
import com.rihao.property.modules.estate.service.IParkService;
import com.rihao.property.modules.estate.vo.*;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.shiro.util.JwtUtil;
import com.rihao.property.util.excel.ExcelException;
import com.rihao.property.util.excel.ExcelUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-07-05
 */
@RestController
@RequestMapping("/api/park")
public class ParkController extends BaseController {

    private IParkService parkService;
    private ISysUserService userService;
    private IParkFileService parkFileService;

    @Log("园区列表")
    @ApiOperation("园区列表")
    @GetMapping("page")
    public ResBody<?> search(ParkQueryParam parkQueryParam) {
        PageVo<ParkVo> page = this.parkService.search(parkQueryParam);
        addLog("查询园区列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("园区详情")
    @ApiOperation("园区详情")
    @GetMapping("/{id}")
    public ResBody<?> detail(@PathVariable Long id) {
        ParkVo vo = this.parkService.detail(id);
        addLog("查看园区详情-" + vo.getName(), JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(vo);
    }

    @Log("更新园区")
    @PutMapping
    @ApiOperation("更新园区")
    public ResBody<?> update(@RequestBody ParkUpdateVo parkUpdateVo) {
        boolean result = this.parkService.update(parkUpdateVo);
        if (result) {
            addLog("修改园区信息-" + parkUpdateVo.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("添加园区")
    @PostMapping
    @ApiOperation("添加园区")
    public ResBody<?> createNew(@RequestBody ParkCreateVo parkCreateVo) {
        boolean result = this.parkService.createNew(parkCreateVo);
        if (result) {
            addLog("添加园区-" + parkCreateVo.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("添加成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("删除园区")
    @DeleteMapping("/{id}")
    @ApiOperation("删除园区")
    public ResBody<?> delete(@PathVariable Long id) {
        boolean result = this.parkService.delete(id);
        if (result) {
            addLog("删除园区-" + id, JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("删除成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("根据用户权限获取园区ky对")
    @GetMapping("permission_list")
    public ResBody<?> getPermissionList() {
        return ResBody.success(this.parkService.getPermissionList());
    }

    @Log("上传合同模版")
    @PostMapping("upload_contract_temp/{id}")
    @ApiOperation("上传合同模版")
    public ResBody<?> uploadContractTemp(@RequestParam("file") MultipartFile file, @PathVariable Long id) throws IOException {
        this.parkService.uploadContractTemp(id, file);
        return ResBody.success();
    }

    @Log("资料列表")
    @GetMapping("file/page")
    @ApiOperation("资料列表")
    public ResBody<?> filePage(ParkFileQueryParam parkFileQueryParam) {
        PageVo<ParkFileVo> page = this.parkFileService.search(parkFileQueryParam);
        return ResBody.success(page);
    }

    @Log("新建资料")
    @PostMapping("file")
    @ApiOperation("新建资料")
    public ResBody<?> createFile(@RequestBody ParkFileCreateVo parkFileCreateVo) {
        boolean result = this.parkFileService.createNew(parkFileCreateVo);
        if (result) {
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("删除资料")
    @DeleteMapping("file/{id}")
    @ApiOperation("删除资料")
    public ResBody<?> deleteFile(@PathVariable Long id) {
        boolean result = this.parkFileService.delete(id);
        if (result) {
            addLog("删除资料-" + id, JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("删除成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("园区全景信息")
    @ApiOperation("园区全景信息")
    @GetMapping("/detail/{id}")
    public ResBody<?> getParkDetail(@PathVariable Long id) {
        ParkVo vo = this.parkService.panorama(id);
        addLog("查看园区全景-" + vo.getName(), JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(vo);
    }

    @Log("导入单元信息")
    @PostMapping("import")
    public ResBody<?> importBuilding(@RequestParam("file") MultipartFile file) throws ExcelException {
        try {
            List[] results = ExcelUtil.readExcel(file, ParkImport.class);
            boolean result = this.parkService.importBuilding(results);
            if (result) {
                return ResBody.success().message("导入成功");
            }
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message(e.getMessage()));
        }
    }

    @GetMapping("/getByEstId/{estId}")
    public ResBody<?> getByEstId(@PathVariable Long estId) {
        return ResBody.success(this.parkService.getByEstId(estId));
    }

    @GetMapping("/getByBuildingId/{buildingId}")
    public ResBody<?> getByBuildingId(@PathVariable Long buildingId) {
        return ResBody.success(this.parkService.getByBuildingId(buildingId));
    }

    @Autowired
    private void setService(IParkService parkService,
                            ISysUserService userService,
                            IParkFileService parkFileService) {
        this.parkService = parkService;
        this.userService = userService;
        this.parkFileService = parkFileService;
    }
}

