package com.rihao.property.modules.system.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.AdminQueryParam;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.service.ISysUserOrganizationService;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.modules.system.vo.SysUserCreateVo;
import com.rihao.property.modules.system.vo.SysUserOrganizationVo;
import com.rihao.property.modules.system.vo.SysUserUpdateVo;
import com.rihao.property.modules.system.vo.SysUserVo;
import com.rihao.property.shiro.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
@RestController
@RequestMapping("/api/admin/sys-user")
@Api(value = "用户", tags = "用户管理")
public class SysUserController extends BaseController {

    private ISysUserService userService;
    private ISysUserOrganizationService userOrganizationService;

    @Log("创建用户")
    @PostMapping
    @ApiOperation("创建用户")
    public ResBody<?> createNew(@RequestBody SysUserCreateVo createVo) {
        //validate username
        Assert.isTrue(this.userService.count(
                new QueryWrapper<SysUser>()
                        .lambda()
                        .eq(SysUser::getUsername, createVo.getUsername())
                ) == 0
                , "用户名已存在");
        boolean result = this.userService.createNew(createVo);
        if (result) {
            addLog("创建系统账号：" + createVo.getUsername(), "创建系统账号");
            return ResBody.success().message("创建成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("创建失败"));
    }

    @Log("修改用户")
    @PutMapping
    @ApiOperation("修改用户")
    public ResBody<?> update(@RequestBody SysUserUpdateVo vo) {
        //validate username
        Assert.isTrue(this.userService.count(
                new QueryWrapper<SysUser>()
                        .lambda()
                        .eq(SysUser::getUsername, vo.getUsername())
                        .ne(SysUser::getId, vo.getId())
                ) == 0
                , "用户名已存在");
        boolean result = this.userService.update(vo);
        if (result) {
            addLog("修改系统账号：" + vo.getUsername(), "修改系统账号");
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("修改失败"));
    }

    @Log("删除用户")
    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    public ResBody<?> delete(@PathVariable Long id) {
        SysUser sysUser = this.userService.getById(id);
        boolean result = this.userService.deleteUser(id);
        if (result) {
            addLog("删除系统账号：" + sysUser.getUsername(), "删除系统账号");
            return ResBody.success().message("删除成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("用户列表")
    @GetMapping
    @ApiOperation("用户列表")
    public ResBody<?> findList(AdminQueryParam queryParam) {
        PageVo<SysUserVo> result = this.userService.search(JwtUtil.getCurrentUser(), queryParam);
        addLog("查询系统账号列表", "查询系统账号列表");
        return ResBody.success(result);
    }

    @Log("启用账号")
    @PutMapping("/{id}/enable")
    @ApiOperation("启用账号")
    public ResBody<?> enableUser(@PathVariable Long id) {
        boolean result = this.userService.changeState(id, SysUser.State.enable);
        if (result) {
            SysUser sysUser = this.userService.getById(id);
            addLog("启用系统账号：" + sysUser.getUsername(), "启用系统账号");
            return ResBody.success().message("启用成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("禁用账号")
    @PutMapping("/{id}/disable")
    @ApiOperation("禁用账号")
    public ResBody<?> disableUser(@PathVariable Long id) {
        boolean result = this.userService.changeState(id, SysUser.State.disabled);
        if (result) {
            SysUser sysUser = this.userService.getById(id);
            addLog("禁用系统账号：" + sysUser.getUsername(), "禁用系统账号");
            return ResBody.success().message("禁用成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("用户详情")
    @GetMapping("/{id}")
    @ApiOperation("用户详情")
    public ResBody<SysUserVo> findList(@PathVariable Long id) {
        SysUserVo vo = this.userService.findOne(id);
        addLog("查看系统账号：" + vo.getUsername(), "查看系统账号");
        return ResBody.success(vo);
    }

    @Log("用户信息")
    @GetMapping("/info")
    public ResBody<?> getUserInfo() {
        SysUser user = JwtUtil.getCurrentUser();
        return ResBody.success(user);
    }

    @Log("重置密码")
    @PutMapping("/{id}/{password}")
    @ApiOperation("重置密码")
    public ResBody<?> resetPassword(@PathVariable Long id) {
        boolean result = this.userService.resetPassword(id);
        if (result) {
            SysUser sysUser = this.userService.getById(id);
            addLog("重置系统账号密码：" + sysUser.getUsername(), "重置系统账号密码");
            return ResBody.success().message("重置成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("重置失败"));
    }

    @Log("获取授权单位")
    @GetMapping("/{id}/organization")
    @ApiOperation("获取授权单位")
    public ResBody<?> grantOrganizations(@PathVariable Long id) {
        SysUserOrganizationVo vo = this.userOrganizationService.getGrantOrganizations(id);
        return ResBody.success(vo);
    }

    @Log("授权单位")
    @PutMapping("/organization")
    @ApiOperation("授权单位")
    public ResBody<?> grantOrganizations(@RequestBody @Valid SysUserOrganizationVo userOrganization) {
        boolean result = this.userOrganizationService.grantOrganizations(userOrganization);
        if (result) {
            SysUser sysUser = this.userService.getById(userOrganization.getUserId());
            addLog("系统账号授权单位：" + sysUser.getUsername(), "系统账号授权单位");
            return ResBody.success().message("授权成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Autowired
    public void setUserService(ISysUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserOrganizationService(ISysUserOrganizationService userOrganizationService) {
        this.userOrganizationService = userOrganizationService;
    }
}