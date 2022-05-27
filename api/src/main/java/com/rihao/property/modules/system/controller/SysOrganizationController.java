package com.rihao.property.modules.system.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.OrganizationQueryParam;
import com.rihao.property.modules.system.entity.SysOrganization;
import com.rihao.property.modules.system.service.ISysOrganizationService;
import com.rihao.property.modules.system.vo.SysOrganizationCreateVo;
import com.rihao.property.modules.system.vo.SysOrganizationListVo;
import com.rihao.property.modules.system.vo.SysOrganizationVo;
import com.rihao.property.modules.system.vo.TreeOrganizationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 单位表 前端控制器
 * </p>
 *
 * @author ken
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/admin/sys-organization")
@Api(value = "单位管理", tags = "单位管理")
public class SysOrganizationController extends BaseController {

    private ISysOrganizationService organizationService;

    @Log("单位列表")
    @PostMapping("/list")
    @ApiOperation(value = "单位列表", response = SysOrganizationListVo.class, responseContainer = "list")
    public ResBody<?> search(OrganizationQueryParam queryParam) {
        PageVo<SysOrganizationListVo> page = this.organizationService.search(queryParam);
        addLog("查询单位列表", "查询单位列表");
        return ResBody.success(page);
    }

    @Log("创建单位")
    @PostMapping
    @ApiOperation("创建单位")
    public ResBody<?> createNew(@RequestBody SysOrganizationCreateVo createVo) {
        Boolean result = this.organizationService.createNew(createVo);
        if (result == null || result) {
            addLog("创建单位：" + createVo.getName(), "创建单位");
            return ResBody.success().message("创建成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("更新单位")
    @PutMapping
    @ApiOperation("更新单位")
    public ResBody<?> update(@RequestBody SysOrganizationVo updateVo) {
        boolean result = this.organizationService.update(updateVo);
        if (result) {
            addLog("更新单位：" + updateVo.getName(), "更新单位");
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("删除单位")
    @DeleteMapping("/{id}")
    @ApiOperation("删除单位")
    public ResBody<?> delete(@PathVariable Long id) {
        SysOrganization organization = this.organizationService.getById(id);
        boolean result = this.organizationService.delete(id);
        if (result) {
            addLog("删除单位：" + organization.getName(), "删除单位");
            return ResBody.success().message("删除成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("单位详情")
    @GetMapping("/{id}")
    @ApiOperation("单位详情")
    public ResBody<?> detail(@PathVariable Long id) {
        SysOrganizationVo vo = this.organizationService.detail(id);
        addLog("查看单位详情：" + vo.getName(), "查看代为详情");
        return ResBody.success(vo);
    }


    @GetMapping("/tree")
    @ApiOperation("所有单位tree")
    public ResBody<?> findTreeNodes() {
        List<TreeOrganizationVo> list = this.organizationService.getAllOrganizationTree();
        return ResBody.success(list);
    }

    @GetMapping("/tree/current-user")
    @ApiOperation("当前账号所有单位tree")
    public ResBody<?> findCurrentUserTreeNodes() {
        List<TreeOrganizationVo> list = this.organizationService.getCurrentUserAllOrganizationTree();
        return ResBody.success(list);
    }


    @PostMapping("/import")
    @ApiOperation("单位导入")
    public void importUser(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        this.organizationService.importOrganization(file, request, response);
    }

    @Autowired
    public void setOrganizationService(ISysOrganizationService organizationService) {
        this.organizationService = organizationService;
    }
}

