package com.rihao.property.modules.lease.contract.controller;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.service.IContractService;
import com.rihao.property.modules.lease.contract.vo.SettleContractVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/contract/alter")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Api(tags = "租赁变更")
public class ContractChangeApi extends BaseController {
    IContractService contractService;

    @Log("新建变更合同信息")
    @PostMapping
    @ApiOperation("新建变更合同信息")
    public ResBody<?> settleContract(@RequestBody @Valid SettleContractVo contractVo) throws Exception {
        Contract contract = this.contractService.settleInContract(contractVo);
        if (contract != null) {
            addLog("新建变更合同信息-" + contract.getEntName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success(contract.getId()).message("保存成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("暂存变更合同信息")
    @PostMapping("temporaryContract")
    @ApiOperation("暂存变更合同信息")
    public ResBody<?> temporaryContract(@RequestBody @Valid SettleContractVo contractVo) throws Exception {
        Contract contract = this.contractService.temporaryContract(contractVo);
        if (contract != null) {
            addLog("暂存变更合同信息-" + contract.getEntName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success(contract.getId()).message("保存成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }
}
