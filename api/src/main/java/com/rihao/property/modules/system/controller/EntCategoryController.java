package com.rihao.property.modules.system.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.ent.entity.EntCategory;
import com.rihao.property.modules.ent.service.IEntCategoryService;
import com.rihao.property.modules.ent.vo.EntCategoryVo;
import com.rihao.property.modules.system.controller.params.AdminQueryParam;
import com.rihao.property.modules.system.controller.params.EntCategoryQueryParam;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.service.ISysUserOrganizationService;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.modules.system.vo.SysUserCreateVo;
import com.rihao.property.modules.system.vo.SysUserOrganizationVo;
import com.rihao.property.modules.system.vo.SysUserUpdateVo;
import com.rihao.property.modules.system.vo.SysUserVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
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
@RequestMapping("/api/admin/category")
@Api(value = "领域", tags = "企业领域管理")
public class EntCategoryController extends BaseController {

    private IEntCategoryService entCategoryService;

    @Log("创建领域")
    @PostMapping
    @ApiOperation("创建领域")
    public ResBody<?> createNew(@RequestBody EntCategoryVo createVo) {
        Assert.isTrue(this.entCategoryService.count(
                new QueryWrapper<EntCategory>()
                        .lambda()
                        .eq(EntCategory::getName, createVo.getName())
        ) == 0, "领域已存在");
        if (StringUtils.isNotBlank(createVo.getName())) {
            if (createVo.getName().length() > 45) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("领域名称长度超出限制！"));
            }
        }
        boolean result = this.entCategoryService.createNew(createVo);
        if (result) {
            return ResBody.success().message("创建成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("创建失败"));
    }

    @Log("修改领域")
    @PutMapping
    @ApiOperation("修改领域")
    public ResBody<?> update(@RequestBody EntCategoryVo updateVo) {
        Assert.isTrue(this.entCategoryService.count(
                new QueryWrapper<EntCategory>()
                        .lambda()
                        .eq(EntCategory::getName, updateVo.getName())
                        .ne(EntCategory::getId, updateVo.getId())
        ) == 0, "领域已存在");
        if (StringUtils.isNotBlank(updateVo.getName())) {
            if (updateVo.getName().length() > 45) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("领域名称长度超出限制！"));
            }
        }
        boolean result = this.entCategoryService.update(updateVo);
        if (result) {
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("修改失败"));
    }

    @Log("删除领域")
    @DeleteMapping("/{id}")
    @ApiOperation("删除领域")
    public ResBody<?> delete(@PathVariable Long id) {
        boolean result = this.entCategoryService.delete(id);
        if (result) {
            return ResBody.success().message("删除成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("查询领域")
    @GetMapping("/{id}")
    @ApiOperation("查询领域")
    public ResBody<?> detail(@PathVariable Long id) {
        return ResBody.success(this.entCategoryService.detail(id));
    }

    @Log("领域列表")
    @GetMapping
    @ApiOperation("领域列表")
    public ResBody<?> findList(EntCategoryQueryParam queryParam) {
        PageVo<EntCategoryVo> result = this.entCategoryService.search(queryParam);
        return ResBody.success(result);
    }

    @Log("领域键值对")
    @GetMapping("/key_value_list")
    @ApiOperation("领域键值对")
    public ResBody<?> keyValueList() {
        List<KeyValueVo> result = this.entCategoryService.getKeyValueList();
        return ResBody.success(result);
    }

    @Autowired
    public void setEntCategoryService(IEntCategoryService entCategoryService) {
        this.entCategoryService = entCategoryService;
    }
}