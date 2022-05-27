package com.rihao.property.modules.system.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.pojo.query.Filter;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.system.controller.params.RoleQueryParam;
import com.rihao.property.modules.system.convert.SysRoleConvert;
import com.rihao.property.modules.system.entity.SysRole;
import com.rihao.property.modules.system.service.ISysRoleResourceService;
import com.rihao.property.modules.system.service.ISysRoleService;
import com.rihao.property.modules.system.vo.SysRoleCreateVo;
import com.rihao.property.modules.system.vo.SysRolePermissionVo;
import com.rihao.property.modules.system.vo.SysRoleUpdateVo;
import com.rihao.property.modules.system.vo.SysRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
@RestController
@RequestMapping("/api/admin/sys-role")
@Api(value = "角色管理", tags = "角色管理")
public class SysRoleController extends BaseController {
    private ISysRoleService roleService;
    private ISysRoleResourceService roleResourceService;

    @Log("创建角色")
    @PostMapping
    @ApiOperation("创建角色")
    public ResBody<?> createNew(@RequestBody SysRoleCreateVo createVo) {
        boolean result = this.roleService.createNew(createVo);
        if (result) {
            addLog("创建角色：" + createVo.getName(), "创建角色");
            return ResBody.success().message("创建成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("修改角色")
    @PutMapping
    @ApiOperation("修改角色")
    public ResBody<?> update(@RequestBody SysRoleUpdateVo vo) {
        SysRole model = SysRoleConvert.INSTANCE.updateParam2Entity(vo);
        boolean result = this.roleService.updateById(model);
        if (result) {
            addLog("修改角色：" + vo.getName(), "修改角色");
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("删除角色")
    @DeleteMapping("/{id}")
    @ApiOperation("删除角色")
    public ResBody<?> delete(@PathVariable Long id) {
        SysRole sysRole = this.roleService.getById(id);
        boolean result = this.roleService.removeById(id);
        if (result) {
            addLog("删除角色：" + sysRole.getName(), "删除角色");
            return ResBody.success().message("删除成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("角色列表")
    @GetMapping
    @ApiOperation("角色列表")
    public ResBody<PageVo<SysRoleVo>> findList(RoleQueryParam queryParam) {
        PageVo<SysRoleVo> result = this.roleService.search(queryParam);
        addLog("查询角色列表", "查询角色列表");
        return ResBody.success(result);
    }

    @GetMapping("/kv")
    @ApiOperation("KV列表")
    public ResBody<List<KeyValueVo>> findList(String name) {
        LambdaQueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>()
                .lambda()
                .ne(SysRole::getId, 1L)
                // .ne(SysRole::getId, 2L)
                .orderByDesc(SysRole::getCreateTime);
        if (StringUtils.hasText(name)) {
            queryWrapper.like(SysRole::getName, Filter.LikeValue.both(name));
        }
        List<SysRole> result = this.roleService.list(queryWrapper);
        return ResBody.success(SysRoleConvert.INSTANCE.entity2KeyValueListVo(result));
    }


    @PutMapping("/permission")
    @ApiOperation("角色授权")
    public ResBody<?> grantPermissions(@RequestBody @Valid SysRolePermissionVo rolePermission) {
        boolean result = this.roleResourceService.grantPermission(rolePermission);
        if (result) {
            SysRole sysRole = this.roleService.getById(rolePermission.getRoleId());
            addLog("角色授权：" + sysRole.getName(), "角色授权");
            return ResBody.success().message("授权成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @GetMapping("/{roleId}/permission")
    @ApiOperation("获取角色授权")
    public ResBody<SysRolePermissionVo> getPermissions(@PathVariable Long roleId) {
        SysRolePermissionVo rolePermission = this.roleResourceService.getGrantedPermissions(roleId);
        return ResBody.success(rolePermission);
    }

    @GetMapping("/{id}")
    public ResBody<?> getRoleById(@PathVariable Long id) {
        return ResBody.success(this.roleService.getById(id));
    }

    @Autowired
    public void setRoleService(ISysRoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setRoleResourceService(ISysRoleResourceService roleResourceService) {
        this.roleResourceService = roleResourceService;
    }
}

