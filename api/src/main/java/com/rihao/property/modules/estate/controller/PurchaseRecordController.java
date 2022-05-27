package com.rihao.property.modules.estate.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.enums.EnableState;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.PurchaseRecordFileQueryParam;
import com.rihao.property.modules.estate.controller.params.PurchaseRecordQueryParam;
import com.rihao.property.modules.estate.service.IPurchaseRecordFileService;
import com.rihao.property.modules.estate.service.IPurchaseRecordService;
import com.rihao.property.modules.estate.vo.PurchaseRecordFileCreateVo;
import com.rihao.property.modules.estate.vo.PurchaseRecordFileVo;
import com.rihao.property.modules.estate.vo.PurchaseRecordVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-04-09
 */
@RestController
@RequestMapping("/api/purchase_record")
public class PurchaseRecordController extends BaseController {

    private IPurchaseRecordService purchaseRecordService;
    private IPurchaseRecordFileService purchaseRecordFileService;

    @Log("采购记录")
    @GetMapping("/page")
    public ResBody<?> search(PurchaseRecordQueryParam purchaseRecordQueryParam) throws ParseException {
        PageVo<PurchaseRecordVo> recordVoPageVo = this.purchaseRecordService.search(purchaseRecordQueryParam);
        return ResBody.success(recordVoPageVo);
    }

    @Log("添加采购记录")
    @PostMapping("/add")
    public ResBody<?> createNew(@RequestBody @Valid PurchaseRecordVo purchaseRecordVo) {
        Boolean result = this.purchaseRecordService.createNew(purchaseRecordVo);
        if (result) {
            return ResBody.success().message("新建成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("采购记录详情")
    @GetMapping("/{id}")
    public ResBody<?> detail(@PathVariable Long id) throws ParseException {
        PurchaseRecordVo purchaseRecordVo = this.purchaseRecordService.detail(id);
        return ResBody.success(purchaseRecordVo);
    }

    @Log("修改采购记录")
    @PutMapping("/{id}")
    public ResBody<?> update(@RequestBody @Valid PurchaseRecordVo purchaseRecordVo) {
        Boolean result = this.purchaseRecordService.update(purchaseRecordVo);
        if (result) {
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("删除采购记录")
    @DeleteMapping("/{id}")
    @ApiOperation("删除采购记录")
    public ResBody<?> delete(@PathVariable Long id) {
        this.purchaseRecordService.removeById(id);
        addLog("删除采购记录-" + id, JwtUtil.getCurrentUser().getRealName());
        return ResBody.success();
    }

    @Log("上传资料")
    @PostMapping("upload_file/{id}")
    @ApiOperation("上传资料")
    public ResBody<?> upload_file(@RequestParam("file") MultipartFile file, @PathVariable Long id) throws IOException {
        String result = this.purchaseRecordService.upload_file(id, file);
        return ResBody.success(result);
    }

    @Log("启用")
    @PostMapping("enable/{id}")
    public ResBody<?> enable(@PathVariable Long id) {
        Boolean result = this.purchaseRecordService.changeState(id, EnableState.enable);
        if (result) {
            return ResBody.success().message("启用成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("禁用")
    @PostMapping("disable/{id}")
    public ResBody<?> disable(@PathVariable Long id) {
        Boolean result = this.purchaseRecordService.changeState(id, EnableState.disable);
        if (result) {
            return ResBody.success().message("禁用成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("资料列表")
    @GetMapping("file/page")
    @ApiOperation("资料列表")
    public ResBody<?> filePage(PurchaseRecordFileQueryParam purchaseRecordFileQueryParam) {
        PageVo<PurchaseRecordFileVo> page = this.purchaseRecordFileService.search(purchaseRecordFileQueryParam);
        return ResBody.success(page);
    }

    @Log("新建资料")
    @PostMapping("file")
    @ApiOperation("新建资料")
    public ResBody<?> createFile(@RequestBody PurchaseRecordFileCreateVo purchaseRecordFileCreateVo) {
        boolean result = this.purchaseRecordFileService.createNew(purchaseRecordFileCreateVo);
        if (result) {
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("删除资料")
    @DeleteMapping("file/{id}")
    @ApiOperation("删除资料")
    public ResBody<?> deleteFile(@PathVariable Long id) {
        boolean result = this.purchaseRecordFileService.delete(id);
        if (result) {
            addLog("删除资料-" + id, JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("删除成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Autowired
    private void setPurchaseRecordService(IPurchaseRecordService purchaseRecordService) {
        this.purchaseRecordService = purchaseRecordService;
    }

    @Autowired
    private void setPurchaseRecordFileService(IPurchaseRecordFileService purchaseRecordFileService) {
        this.purchaseRecordFileService = purchaseRecordFileService;
    }
}

