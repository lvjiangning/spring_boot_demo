package com.rihao.property.modules.ent.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.ent.controller.params.EntMattersQueryParam;
import com.rihao.property.modules.ent.service.IEntMattersService;
import com.rihao.property.modules.ent.vo.EntMattersVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-29
 */
@RestController
@RequestMapping("/api/matters")
public class EntMattersController extends BaseController {

    private final IEntMattersService entMattersService;

    public EntMattersController(IEntMattersService entMattersService) {
        this.entMattersService = entMattersService;
    }

    @Log("事项列表")
    @GetMapping("page")
    @ApiOperation("事项列表")
    public ResBody<?> search(EntMattersQueryParam entMattersQueryParam) {
        PageVo<EntMattersVo> page = this.entMattersService.search(entMattersQueryParam);
        addLog("查看事项列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("登记事项")
    @PostMapping
    @ApiOperation("登记事项")
    public ResBody<?> createNew(@RequestBody @Valid EntMattersVo entMattersVo) {
        Boolean result = this.entMattersService.createNew(entMattersVo);
        if (result == null || result) {
            addLog("登记事项-" + entMattersVo.getTitle(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("新建成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("修改事项")
    @PutMapping
    @ApiOperation("修改事项")
    public ResBody<?> update(@RequestBody @Valid EntMattersVo entMattersVo) {
        Boolean result = this.entMattersService.update(entMattersVo);
        if (result == null || result) {
            addLog("修改事项-" + entMattersVo.getTitle(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("新建成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("事项详情")
    @GetMapping("/{id}")
    @ApiOperation("事项详情")
    public ResBody<?> detail(@PathVariable Long id) {
        EntMattersVo vo = this.entMattersService.detail(id);
        addLog("查看事项详情-" + vo.getTitle(), JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(vo);
    }

    @Log("删除事项")
    @DeleteMapping("/{id}")
    @ApiOperation("删除事项")
    public ResBody<?> delete(@PathVariable Long id) {
        this.entMattersService.removeById(id);
        addLog("删除事项-" + id, JwtUtil.getCurrentUser().getRealName());
        return ResBody.success();
    }
}

