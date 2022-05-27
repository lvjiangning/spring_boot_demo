package com.rihao.property.modules.lease.contract.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.lease.contract.controller.params.ContractQueryParam;
import com.rihao.property.modules.report.controller.params.LeaseReportContractQueryParam;
import com.rihao.property.modules.lease.contract.controller.params.SettleQueryParam;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.vo.*;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;
import com.rihao.property.modules.report.controller.params.LedgerReportBaseQueryParam;
import com.rihao.property.modules.report.controller.params.UseHouseDetailQueryParam;
import com.rihao.property.modules.system.entity.SysUser;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
public interface IContractService extends IService<Contract> {

    PageVo<ContractVo> search(ContractQueryParam contractQueryParam);

    PageVo<ContractVo> searchWaitPage(ContractQueryParam contractQueryParam);

    Contract sign(ContractCreateVo createVo) throws Exception;

    Boolean close(CloseParams closeParams);


    ContractDetailVo detail(Long id);

    Boolean change(ContractVo createVo) throws Exception;

    List<Contract> getAvailableList();

    XWPFDocument writeContract(HttpServletResponse response, Long contractId, SysUser user) throws IOException, ParseException;
    //查询即将过期的合同
    PageVo<ContractVo> getExpirationContractPage(ContractQueryParam contractQueryParam);

    PageVo<ContractVo> history_page(ContractQueryParam contractQueryParam);

    PageVo<ContractVo> getByBuildingId(Integer current, Integer pageSize, Long buildingId);

    List<Contract> getByBuildingId(Long buildingId);

    Page<ContractUnit> getContractUnitByBuildingId(LedgerReportBaseQueryParam ledgerReportQueryParam);

    PageVo<Contract> getByEntId(ContractQueryParam contractQueryParam);

    Contract getByCode(String code);

    PageVo<ContractVo> getByEntName(ContractQueryParam contractQueryParam);

    List<ContractVo> getByListEntId(Long entId);

    Boolean uploadOa(MultipartFile file, Long id) throws IOException;

    Boolean uploadContract(MultipartFile file, Long id) throws IOException;



    Contract settle(SettleVo settleVo);

    /**
     * 入驻基本信息填写
     */
    Contract settleInBaseInfo(SettleInBaseInfoVo settleVo);

    Contract submit(Long id);

    Contract settleInBaseInfoAndContract(SettleInBaseInfoVo settleVo);

    boolean cancelSettle(Long id);

    PageVo<ContractSettleVo> settleHistory(SettleQueryParam contractQueryParam);

    Contract settleInContract(SettleContractVo contractVo);

    Contract temporaryContract(SettleContractVo contractVo);

    List<ContractVo> selectRunningListForIndex();
    //查询到期的合同
    List<Contract> selectContractByDueDate();

    //根据合同关闭单元信息
    Boolean closeUnitByContract(Contract contract);

    //根据合同关闭单元信息
    Boolean contractByToDayStart(Contract contract);

    //查询所有今天应该生效的合同
    List<Contract> selectContractByToDayStart();

    /**
     * 通过条件，查询符合条件的企业
     * @return
     */
    Page<Long> findEntIdByContract(LeaseReportContractQueryParam leaseReportContractQueryParam);

    /**
     * 通过参数查询合同信息
     * @return
     */
    List<ContractVo> findLeaseReportContractByParam(LeaseReportContractQueryParam leaseReportContractQueryParam);

    /**
     * 查询用房明细列表相关的合同
     * @param useHouseDetailQueryParam
     * @return
     */
    List<ContractVo> useHouseDetailReport(UseHouseDetailQueryParam useHouseDetailQueryParam);

    /**
     * 合同导出
     * @param content
     */
    void export(List<ContractVo> content,HttpServletResponse response,String fileName);
}
