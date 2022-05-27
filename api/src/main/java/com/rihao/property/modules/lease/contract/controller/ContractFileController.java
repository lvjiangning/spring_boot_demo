package com.rihao.property.modules.lease.contract.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.ContractFileQueryParam;
import com.rihao.property.modules.estate.vo.ContractFileCreateVo;
import com.rihao.property.modules.lease.contract.service.IContractFileService;
import com.rihao.property.modules.lease.contract.vo.ContractFileVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-07-12
 */
@RestController
@RequestMapping("/api/contract/file")
public class ContractFileController extends BaseController {

    private IContractFileService fileService;

    @Log("资料列表")
    @GetMapping("page")
    @ApiOperation("资料列表")
    public ResBody<?> page(ContractFileQueryParam contractFileQueryParam) throws IOException {
        PageVo<ContractFileVo> page = this.fileService.search(contractFileQueryParam);
        return ResBody.success(page);
    }

    @Log("新建资料")
    @PostMapping
    @ApiOperation("新建资料")
    public ResBody<?> createFile(@RequestBody ContractFileCreateVo contractFileCreateVo) throws IOException {
        boolean result = this.fileService.createNew(contractFileCreateVo);
        if (result) {
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("删除资料")
    @DeleteMapping("/{id}")
    @ApiOperation("删除资料")
    public ResBody<?> deleteFile(@PathVariable Long id) {
        boolean result = this.fileService.delete(id);
        if (result) {
            addLog("删除资料-" + id, JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("删除成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Autowired
    private void setFileService(IContractFileService fileService) {
        this.fileService = fileService;
    }
}

