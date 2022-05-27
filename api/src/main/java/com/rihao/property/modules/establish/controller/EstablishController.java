package com.rihao.property.modules.establish.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.modules.establish.controller.params.EstablishQueryParam;
import com.rihao.property.modules.establish.service.IEstablishService;
import com.rihao.property.modules.establish.vo.EstablishVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 * 单位信息表 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-04-23
 */
@RestController
@RequestMapping("/api/establish")
public class EstablishController extends BaseController {

    private final IEstablishService establishService;

    public EstablishController(IEstablishService establishService) {
        this.establishService = establishService;
    }

    @Log("获取单位列表")
    @GetMapping("page")
    public ResBody<?> search(EstablishQueryParam establishQueryParam) {
        return ResBody.success(this.establishService.search(establishQueryParam));
    }

    @Log("获取单位列表")
    @GetMapping("list")
    public ResBody<?> list() {
        return ResBody.success(this.establishService.search());
    }

    @Log("获取单位详细信息")
    @GetMapping("/{id}")
    public ResBody<?> detail(@PathVariable Long id) {
        return ResBody.success(this.establishService.detail(id));
    }

    @Log("添加新单位")
    @PostMapping
    public ResBody<?> createNew(@RequestBody EstablishVo createVo) {
        Boolean result = this.establishService.createNew(createVo);
        if (result) {
            return ResBody.success();
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("修改单位")
    @PutMapping
    public ResBody<?> update(@RequestBody EstablishVo updateVo) {
        Boolean result = this.establishService.update(updateVo);
        if (result) {
            return ResBody.success();
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("删除单位")
    @DeleteMapping("/{id}")
    public ResBody<?> delete(@PathVariable Long id) {
        Boolean result = this.establishService.delete(id);
        if (result) {
            return ResBody.success();
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }
}

