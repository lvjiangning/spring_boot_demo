package com.rihao.property.modules.lease.contract.controller;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.close_history.convert.ContractCloseHistoryConvert;
import com.rihao.property.modules.close_history.entity.ContractCloseHistory;
import com.rihao.property.modules.close_history.enums.CloseTypeEnum;
import com.rihao.property.modules.close_history.service.IContractCloseHistoryService;
import com.rihao.property.modules.close_history.vo.ContractCloseHistoryVo;
import com.rihao.property.modules.config.entity.SysConfig;
import com.rihao.property.modules.config.service.ISysConfigService;
import com.rihao.property.modules.ent.entity.Ent;
import com.rihao.property.modules.ent.service.IEntService;
import com.rihao.property.modules.estate.convert.UnitConvert;
import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.entity.Unit;
import com.rihao.property.modules.estate.service.IBuildingService;
import com.rihao.property.modules.estate.service.IParkService;
import com.rihao.property.modules.estate.service.IUnitService;
import com.rihao.property.modules.estate.vo.UnitVo;
import com.rihao.property.modules.lease.contract.controller.params.ContractQueryParam;
import com.rihao.property.modules.lease.contract.controller.params.SettleQueryParam;
import com.rihao.property.modules.lease.contract.convert.ContractConvert;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.enums.BizType;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import com.rihao.property.modules.lease.contract.service.IContractAdjustService;
import com.rihao.property.modules.lease.contract.service.IContractService;
import com.rihao.property.modules.lease.contract.vo.*;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;
import com.rihao.property.modules.lease.unit.service.IContractUnitService;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * ???????????????
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
@RestController
@RequestMapping("/api/contract")
@Api(tags = "????????????")
public class ContractController extends BaseController {

    private IContractService contractService;
    private ISysConfigService configService;
    private IEntService entService;
    private IContractCloseHistoryService closeHistoryService;
    private IBuildingService buildingService;
    private IParkService parkService;
    private IContractUnitService contractUnitService;
    private IUnitService unitService;
    @Autowired
    @Lazy
    private IContractAdjustService contractAdjustService;

    @Log("????????????")
    @GetMapping("page")
    @ApiOperation(("????????????"))
    public ResBody<?> search(ContractQueryParam contractQueryParam) throws ParseException {
        PageVo<ContractVo> page;
        if (contractQueryParam.getStatus() != null && contractQueryParam.getStatus().equals(ContractStatus.wait)) {
            page = this.contractService.searchWaitPage(contractQueryParam);
            return ResBody.success(page);
        }
        if (contractQueryParam.getStatus() != null && contractQueryParam.getStatus().equals(ContractStatus.due_to_close)) {
            if (!StringUtils.isNotBlank(contractQueryParam.getExpirationMonth())){
                contractQueryParam.setExpirationMonth("1");//??????1??????
            }
            return ResBody.success(this.contractService.getExpirationContractPage(contractQueryParam));
        }
        page = this.contractService.search(contractQueryParam);
        addLog("??????????????????", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }


    @Log("????????????")
    @GetMapping("export")
    @ApiOperation(("??????????????????"))
    public void export(ContractQueryParam contractQueryParam,HttpServletResponse response) throws ParseException {
        //?????????????????????
        contractQueryParam.setPageSize(100000);
        contractQueryParam.setCurrent(1);
        String fileName="";
        PageVo<ContractVo> page=new PageVo<>();
        if (contractQueryParam.getStatus() != null && contractQueryParam.getStatus().equals(ContractStatus.wait)) {
            fileName="????????????";
            page = this.contractService.searchWaitPage(contractQueryParam);
        } else if (contractQueryParam.getStatus() != null && contractQueryParam.getStatus().equals(ContractStatus.due_to_close)) {
            if (!StringUtils.isNotBlank(contractQueryParam.getExpirationMonth())){
                contractQueryParam.setExpirationMonth("1");//??????1??????
            }
            fileName="????????????";
            page=this.contractService.getExpirationContractPage(contractQueryParam);
        }else {
            page = this.contractService.search(contractQueryParam);
        }
        contractService.export(page.getContent(),response,fileName);
        addLog("??????????????????", JwtUtil.getCurrentUser().getRealName());

    }

    @Log("????????????????????????")
    @GetMapping("history_page")
    @ApiOperation(("????????????????????????"))
    public ResBody<?> history_page(ContractQueryParam contractQueryParam) {
        PageVo<ContractVo> page = this.contractService.history_page(contractQueryParam);
        addLog("??????????????????", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("??????????????????")
    @GetMapping("settle-history")
    @ApiOperation(("??????????????????"))
    public ResBody<?> settleHistory(SettleQueryParam contractQueryParam) {
        PageVo<ContractSettleVo> page = this.contractService.settleHistory(contractQueryParam);
        addLog("??????????????????", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    /*@Log("?????????")
    @PostMapping("settle")
    @ApiOperation("?????????")
    public ResBody<?> settle(@RequestBody @Valid SettleVo settleVo) throws Exception {
        Contract contract = this.contractService.settle(settleVo);
        if (contract != null) {
            addLog("?????????-" + settleVo.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("????????????");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("????????????"));
    }*/

    @Log("????????????")
    @PostMapping("sign")
    @ApiOperation("????????????")
    public ResBody<?> createNew(@RequestBody @Valid ContractCreateVo createVo) throws Exception {
        Contract contract = this.contractService.sign(createVo);
        if (contract != null) {
            addLog("????????????-" + createVo.getEntId(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("????????????");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("????????????"));
    }

    @Log("????????????")
    @GetMapping("/{id}")
    @ApiOperation("????????????")
    public ResBody<?> detail(@PathVariable Long id) {
        addLog("????????????????????????-" + id, JwtUtil.getCurrentUser().getRealName());
        /*ContractVo vo = this.contractService.detail(id);
        return ResBody.success(vo);*/
        ContractDetailVo detail = this.contractService.detail(id);
        if (detail !=null) {
            detail.setContractAdjustVo(contractAdjustService.adjustContractDetailByContractId(detail.getId()));
        }
        return ResBody.success(detail);
    }

    @Log("????????????")
    @GetMapping("/code/{code}")
    @ApiOperation("????????????")
    public ResBody<?> getByCode(@PathVariable String code) {
        addLog("????????????????????????-" + code, JwtUtil.getCurrentUser().getRealName());
        ContractVo vo = ContractConvert.INSTANCE.entity2Vo(this.contractService.getByCode(code));
       if (StringUtils.isNotBlank(vo.getBuildingIds())){
           String[] split =vo.getBuildingIds().split(",");
           List<Long> ids=new ArrayList<>();
           List<String> buildName=new ArrayList<>();
           for (String id:split){
               ids.add(Long.parseLong(id));
               Building byId = buildingService.getById(Long.parseLong(id));
               buildName.add(byId.getName());
           }
           vo.setBuildingNames(buildName);
           vo.setBuildingIdList(ids);
       }
        Park park = this.parkService.getById(vo.getParkId());
        vo.setParkName(park.getName());
        ContractCloseHistory history = this.closeHistoryService.getByContractId(vo.getId());
        if (history != null) {
            ContractCloseHistoryVo closeHistoryVo = ContractCloseHistoryConvert
                    .INSTANCE.entity2Vo(history);
            closeHistoryVo.setCreateTime(history.getCreateTime());
            closeHistoryVo.setCreateBy(history.getCreateBy());
            switch (closeHistoryVo.getCloseType()) {
                case EXTEND:
                    closeHistoryVo.setCloseTypeString("??????");
                case REDUCE:
                    closeHistoryVo.setCloseTypeString("??????");
                case RENEWAL:
                    closeHistoryVo.setCloseTypeString("??????");
                case RETREAT:
                    closeHistoryVo.setCloseTypeString("??????");
                case CHANGE:
                    closeHistoryVo.setCloseTypeString("??????");
                case ADJUST:
                    closeHistoryVo.setCloseTypeString("??????");
                case STOP:
                    closeHistoryVo.setCloseTypeString("??????");
            }
            vo.setContractCloseHistory(closeHistoryVo);
        }
        Ent ent = this.entService.getById(vo.getEntId());
        vo.setEntName(ent.getName());
        List<UnitVo> unitVos = new ArrayList<>();
        List<ContractUnit> contractUnitList = this.contractUnitService.getUnitListByContractId(vo.getId());
        for (ContractUnit contractUnit : contractUnitList) {
            Unit unit=   this.unitService.getById(contractUnit.getUnitId());
            UnitVo unitVo = UnitConvert.INSTANCE.entity2Vo(unit);
            Building byId = buildingService.getById(unit.getBuildingId());
            unitVo.setBuildingName(byId.getName());
            unitVos.add(unitVo);
        }
        vo.setUnitVos(unitVos);
        return ResBody.success(vo);
    }

    @Log("????????????")
    @PostMapping("/close")
    @ApiOperation("????????????")
    public ResBody<?> close(@RequestBody CloseParams closeParams) {
        Boolean result = this.contractService.close(closeParams);
        if (result) {
            addLog("????????????-" + closeParams.getId(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("????????????");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("????????????"));
    }

    @Log("????????????")
    @PostMapping("change")
    @ApiOperation("????????????")
    public ResBody<?> change(@RequestBody @Valid ContractVo createVo) throws Exception {
        Boolean result = this.contractService.change(createVo);
        if (result == null || result) {
            addLog("????????????-" + createVo.getEntName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("????????????");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("????????????"));
    }

    @Log("????????????")
    @GetMapping("write/{id}")
    public ResBody<?> writeContract(HttpServletResponse response, @PathVariable Long id) throws IOException, ParseException {
        XWPFDocument document = this.contractService.writeContract(response, id, JwtUtil.getCurrentUser());
        Contract contract = this.contractService.getById(id);
        String fileNameURL = URLEncoder.encode(contract.getEntName() + ".doc", "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileNameURL + ";" + "filename*=utf-8''" + fileNameURL);
        response.flushBuffer();
        OutputStream outputStream = response.getOutputStream();
        document.write(outputStream);
        outputStream.flush();
        outputStream.close();

        return ResBody.success();
    }

    @Log("????????????????????????")
    @GetMapping("expirationList")
    public ResBody<?> getExpirationContractPage(ContractQueryParam contractQueryParam) {
        SysUser user = JwtUtil.getCurrentUser();
        if (user.getRoleId() != 1) {
            SysConfig config = this.configService.getConfig(user.getEstablishId());
            contractQueryParam.setExpirationMonth(config.getContactExpiration() + "");
            return ResBody.success(this.contractService.getExpirationContractPage(contractQueryParam));
        }
        return ResBody.success();
    }

    @Log("????????????ID??????????????????")
    @PostMapping("getByEntId")
    public ResBody<?> getByEntId(ContractQueryParam contractQueryParam) {
        PageVo<Contract> contractList = this.contractService.getByEntId(contractQueryParam);
        return ResBody.success(contractList);
    }

    @Log("????????????????????????????????????")
    @GetMapping("getByEntName")
    public ResBody<?> getByEntName(ContractQueryParam contractQueryParam) {
        PageVo<ContractVo> contractList = this.contractService.getByEntName(contractQueryParam);
        return ResBody.success(contractList);
    }

    @Log("?????????????????????OA??????")
    @PostMapping("/uploadOa/{id}")
    public ResBody<?> uploadOa(@RequestParam("file") MultipartFile file, @PathVariable Long id) throws IOException {
        Boolean result = this.contractService.uploadOa(file, id);
        if (result == null || result) {
            return ResBody.success().message("????????????");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("????????????"));
    }

    @Log("????????????")
    @PostMapping("/uploadContract/{id}")
    public ResBody<?> uploadContract(@RequestParam("file") MultipartFile file, @PathVariable Long id) throws IOException {
        Boolean result = this.contractService.uploadContract(file, id);
        if (result == null || result) {
            return ResBody.success().message("????????????");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("????????????"));
    }

    @Log("??????????????????")
    @PostMapping("/adjust")
    public ResBody<?> adjustContract(@RequestBody @Valid ContractAdjustVo contractAdjustVo) throws IOException {
        String result = this.contractAdjustService.adjustContract(contractAdjustVo);
        if (StringUtils.isBlank(result)) {
            return ResBody.success().message("????????????");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("???????????????"+result));
    }

    @Log("??????????????????")
    @PostMapping("/adjust/{id}")
    public ResBody<?> adjustContractDetail(@PathVariable Long id) throws IOException {
        return ResBody.success(this.contractAdjustService.adjustContractDetailByContractId(id)).message("????????????");
    }



    @Autowired
    private void setContractService(IContractService contractService) {
        this.contractService = contractService;
    }

    @Autowired
    private void setUserService(ISysUserService userService) {
    }

    @Autowired
    private void setConfigService(ISysConfigService configService) {
        this.configService = configService;
    }

    @Autowired
    private void setEntService(IEntService entService) {
        this.entService = entService;
    }

    @Autowired
    private void setCloseHistoryService(IContractCloseHistoryService closeHistoryService) {
        this.closeHistoryService = closeHistoryService;
    }

    @Autowired
    private void setBuildingService(IBuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @Autowired
    private void setParkService(IParkService parkService) {
        this.parkService = parkService;
    }

    @Autowired
    private void setContractUnitService(IContractUnitService contractUnitService) {
        this.contractUnitService = contractUnitService;
    }

    @Autowired
    private void setUnitService(IUnitService unitService) {
        this.unitService = unitService;
    }
}

