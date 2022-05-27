package com.rihao.property.modules.lease.contract.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.lease.contract.controller.params.ContractQueryParam;
import com.rihao.property.modules.lease.contract.controller.params.SettleQueryParam;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.entity.ContractAdjust;
import com.rihao.property.modules.lease.contract.vo.*;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;
import com.rihao.property.modules.report.controller.params.LedgerReportBaseQueryParam;
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
public interface IContractAdjustService extends IService<ContractAdjust> {

    String adjustContract(ContractAdjustVo contractAdjustVo);

    ContractAdjustVo adjustContractDetailByContractId(Long contractId);
}
