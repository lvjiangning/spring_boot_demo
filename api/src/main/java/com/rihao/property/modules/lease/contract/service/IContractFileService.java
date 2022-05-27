package com.rihao.property.modules.lease.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.ContractFileQueryParam;
import com.rihao.property.modules.estate.vo.ContractFileCreateVo;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.entity.ContractFile;
import com.rihao.property.modules.lease.contract.enums.ContractFileType;
import com.rihao.property.modules.lease.contract.vo.ContractFileVo;
import com.rihao.property.modules.lease.contract.vo.SettleContractFilesVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-07-12
 */
public interface IContractFileService extends IService<ContractFile> {

    PageVo<ContractFileVo> search(ContractFileQueryParam contractFileQueryParam);

    boolean createNew(ContractFileCreateVo contractFileCreateVo);

    boolean delete(Long id);
    //合同批复文件提交
    Contract settleInContractFiles(SettleContractFilesVo filesVo, ContractFileType type);
}
