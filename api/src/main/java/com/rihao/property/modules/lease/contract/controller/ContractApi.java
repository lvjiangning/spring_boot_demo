package com.rihao.property.modules.lease.contract.controller;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.modules.ent.service.IEntService;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.enums.ContractFileType;
import com.rihao.property.modules.lease.contract.service.IContractFileService;
import com.rihao.property.modules.lease.contract.service.IContractService;
import com.rihao.property.modules.lease.contract.vo.SettleContractFilesVo;
import com.rihao.property.modules.lease.contract.vo.SettleInBaseInfoVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.xpath.operations.Bool;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contract")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Api(tags = "企业入驻")
public class ContractApi extends BaseController {
    IContractService contractService;
    IContractFileService contractFileService;
    IEntService entService;

    @Log("新入驻填写企业资料")
    @PostMapping("/ent")
    @ApiOperation("新入驻填写企业资料")
    public ResBody settle(@RequestBody @Valid SettleInBaseInfoVo settleVo) {
        if (!CollectionUtils.isEmpty(settleVo.getBuildingIdList())){
            List<Long> collect = settleVo.getBuildingIdList().stream().distinct().collect(Collectors.toList());
            if (collect.size()<settleVo.getBuildingIdList().size()){
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败：重复的楼栋信息"));
            }
        }
        Contract contract = this.contractService.settleInBaseInfoAndContract(settleVo);
        if (contract != null) {
            addLog("入驻基本信息填写-" + settleVo.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success(contract.getId()).message("保存成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("取消入驻")
    @PostMapping("/ent/cancel/{id}")
    @ApiOperation("取消入驻")
    public ResBody cancel_settle(@PathVariable Long id) {
        Boolean result = this.contractService.cancelSettle(id);
        if (result) {
            addLog("取消入驻-" + id, JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("取消成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @GetMapping("/ent-name/validate")
    public ResBody<Void> validateEntName(String name, Long entId) {
        boolean result = this.entService.validateName(entId, name);
        if (result) {
            return ResBody.success();
        }
        return ResBody.failure("企业名已存在");
    }

    /*@Log("新入驻填写合同信息")
    @PostMapping
    @ApiOperation("新入驻填写合同资料")
    public ResBody<?> settleContract(@RequestBody @Valid SettleContractVo contractVo) throws Exception {
        Contract contract = this.contractService.settleInContract(contractVo);
        if (contract != null) {
            addLog("入驻合同信息填写-" + contract.getEntName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("保存成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }*/

    @Log("新入驻填写合同批复文件")
    @PostMapping("/approve-files")
    @ApiOperation("新入驻填写合同批复文件")
    public ResBody<?> settleContractApprove(@RequestBody @Valid SettleContractFilesVo filesVo) throws Exception {
        Contract contract = this.contractFileService.settleInContractFiles(filesVo, ContractFileType.APPROVE);
        if (contract != null) {
            addLog("入驻批复文件填写-" + contract.getEntName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("保存成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("新入驻上传合同文件")
    @PostMapping("/contract-files")
    @ApiOperation("新入驻上传合同文件")
    public ResBody<?> settleContractFiles(@RequestBody @Valid SettleContractFilesVo filesVo) throws Exception {
        Contract contract = this.contractFileService.settleInContractFiles(filesVo, ContractFileType.CONTRACT);
        if (contract != null) {
            addLog("入驻批复文件填写-" + contract.getEntName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("保存成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("提交入驻信息")
    @PostMapping("/submit/{id}")
    @ApiOperation("直接提交完成信息")
    public ResBody<?> submit(@PathVariable Long id) throws Exception {
        Contract contract = this.contractService.submit(id);
        if (contract != null) {
            addLog(contract.getEntName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("保存成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }
}
