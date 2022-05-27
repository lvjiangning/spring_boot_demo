package com.rihao.property.modules.system.controller;


import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.auth0.jwt.JWT;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.establish.service.IEstablishService;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.mapper.ContractMapper;
import com.rihao.property.modules.lease.contract.service.IContractService;
import com.rihao.property.modules.lease.contract.vo.ContractVo;
import com.rihao.property.modules.system.controller.params.LogQueryParam;
import com.rihao.property.modules.system.service.ISysLogService;
import com.rihao.property.modules.system.service.ISysRoleService;
import com.rihao.property.modules.system.vo.SysLogVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
@RestController
@RequestMapping("/api/admin/workspace")
@Api(value = "工作台数据", tags = "工作台数据")
public class WorkplaceController extends BaseController {

    private IContractService contractService;
    private IEstablishService establishService;
    private ISysRoleService sysRoleService;

    @Log("工作台数据")
    @GetMapping
    @ApiOperation(value = "工作台数据")
    public ResBody<?> findList() {
        WorkData workData = new WorkData();
        List<ContractWorkVo> contracts = new ArrayList<>();
        List<ContractVo> contractVos = this.contractService.selectRunningListForIndex();
        for (ContractVo contractVo : contractVos) {
            ContractWorkVo contract = new ContractWorkVo();
            contract.setParkName(contract.getParkName());
            contract.setEntName(contractVo.getEntName());
            contract.setCreateTime(contractVo.getCreateTime());
            contracts.add(contract);
        }
        workData.setContractList(contracts);
        if (JwtUtil.getCurrentUser().getEstablishId() != null)
            workData.setEstName(this.establishService.getById(JwtUtil.getCurrentUser().getEstablishId()).getName());
        else
            workData.setEstName("");
        workData.setRole(this.sysRoleService.getById(JwtUtil.getCurrentUser().getRoleId()).getName());
        workData.setUserName(JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(workData);
    }

    @Autowired
    public void setContractService(IContractService contractService) {
        this.contractService = contractService;
    }

    @Autowired
    private void setEstablishService(IEstablishService establishService) {
        this.establishService = establishService;
    }

    @Autowired
    private void setSysRoleService(ISysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }
}

@Data
class WorkData {
    private String estName;
    private String userName;
    private String role;
    private List<ContractWorkVo> contractList;
    private List<NewsVo> newsVos;
}

@Data
class ContractWorkVo {
    private String entName;
    private String parkName;
    private String buildingName;
    private String unit;
    private String createTime;
}

@Data
class NewsVo{
    private String info;
    private String createTime;
}
