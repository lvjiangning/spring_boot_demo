package com.rihao.property.modules.ent.controller;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.modules.ent.controller.params.EntPartnerQueryParam;
import com.rihao.property.modules.ent.entity.EntPartner;
import com.rihao.property.modules.ent.service.IEntPartnerService;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-29
 */
@RestController
@RequestMapping("/api/partner")
public class EntPartnerController extends BaseController {

    private final IEntPartnerService entPartnerService;

    public EntPartnerController(IEntPartnerService entPartnerService) {
        this.entPartnerService = entPartnerService;
    }

    @Log("股东列表")
    @GetMapping("list")
    @ApiOperation("股东列表")
    public ResBody<?> search(EntPartnerQueryParam entPartnerQueryParam) {
        List<EntPartner> list = this.entPartnerService.search(entPartnerQueryParam.getEntId());
        addLog("查看股东列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(list);
    }

    @Log("登记股东")
    @PostMapping
    @ApiOperation("登记股东")
    public ResBody<?> createNew(@RequestBody @Valid EntPartner entPartner) {
        Boolean result = this.entPartnerService.createNew(entPartner);
        if (result == null || result) {
            addLog("登记股东-" + entPartner.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("新建成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("修改股东")
    @PutMapping
    @ApiOperation("修改股东")
    public ResBody<?> update(@RequestBody @Valid EntPartner entPartner) {
        Boolean result = this.entPartnerService.update(entPartner);
        if (result == null || result) {
            addLog("修改股东-" + entPartner.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("新建成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("股东详情")
    @GetMapping("/{id}")
    @ApiOperation("股东详情")
    public ResBody<?> detail(@PathVariable Long id) {
        EntPartner entity = this.entPartnerService.detail(id);
        addLog("查看股东详情-" + entity.getName(), JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(entity);
    }

    @Log("删除股东")
    @DeleteMapping("/{id}")
    @ApiOperation("删除股东")
    public ResBody<?> delete(@PathVariable Long id) {
        this.entPartnerService.removeById(id);
        addLog("删除股东-" + id, JwtUtil.getCurrentUser().getRealName());
        return ResBody.success();
    }
}

