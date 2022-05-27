package com.rihao.property.modules.system.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.DictQueryParam;
import com.rihao.property.modules.system.entity.SysDict;
import com.rihao.property.modules.system.service.ISysDictService;
import com.rihao.property.modules.system.vo.SysDictCreateVo;
import com.rihao.property.modules.system.vo.SysDictListVo;
import com.rihao.property.modules.system.vo.SysDictSmallVo;
import com.rihao.property.modules.system.vo.SysDictVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 数据字典表 前端控制器
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
@RestController
@RequestMapping("/api/admin/sys-dict")
@Api(value = "数据字典管理", tags = "数据字典管理")
public class SysDictController extends BaseController {

    private ISysDictService dictService;

    @Log("字典列表")
    @PostMapping("/list")
    @ApiOperation("字典列表")
    public ResBody<?> search(DictQueryParam queryParam) {
        PageVo<SysDictListVo> page = this.dictService.search(queryParam);
        addLog("查询字典列表", "查询字典列表");
        return ResBody.success(page);
    }

    @Log("创建字典")
    @PostMapping
    @ApiOperation("创建字典")
    public ResBody<?> createNew(@RequestBody SysDictCreateVo createVo) {
        boolean result = this.dictService.createNew(createVo);
        if (result) {
            addLog("创建字典：" + createVo.getValue(), "创建字典");
            return ResBody.success().message("创建成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("更新字典")
    @PutMapping
    @ApiOperation("更新字典")
    public ResBody<?> update(@RequestBody SysDictVo updateVo) {
        boolean result = this.dictService.update(updateVo);
        if (result) {
            addLog("更新字典：" + updateVo.getValue(), "更新字典");
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("删除字典")
    @DeleteMapping("/{id}")
    @ApiOperation("删除字典")
    public ResBody<?> delete(@PathVariable Long id) {
        SysDict sysDict = this.dictService.getById(id);
        boolean result = this.dictService.delete(id);
        if (result) {
            addLog("删除字典：" + sysDict.getValue(), "删除字典");
            return ResBody.success().message("删除成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("字典详情")
    @GetMapping("/{id}")
    @ApiOperation("字典详情")
    public ResBody<SysDictVo> detail(@PathVariable Long id) {
        SysDictVo vo = this.dictService.detail(id);
        addLog("查看字典详情：" + vo.getValue(), "查看字典详情");
        return ResBody.success(vo);
    }

    @GetMapping("list/parent-id/{parentId}")
    @ApiOperation("根据父级id查询字典项")
    public ResBody<List<SysDictSmallVo>> listByCode(@PathVariable Long parentId) {
        List<SysDictSmallVo> list = this.dictService.listByParentId(parentId);
        return ResBody.success(list);
    }

    @GetMapping("list/parent-code/{code}")
    @ApiOperation("根据父级code查询字典项")
    public ResBody<List<SysDictSmallVo>> listByCode(@PathVariable String code) {
        List<SysDictSmallVo> list = this.dictService.listByParentCode(code);
        return ResBody.success(list);
    }

    @Autowired
    public void setSysDictService(ISysDictService dictService) {
        this.dictService = dictService;
    }
}

