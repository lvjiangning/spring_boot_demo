package com.rihao.property.modules.lease.contract.service.impl;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.ContractFileQueryParam;
import com.rihao.property.modules.estate.vo.ContractFileCreateVo;
import com.rihao.property.modules.lease.contract.convert.ContractFileConvert;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.entity.ContractFile;
import com.rihao.property.modules.lease.contract.enums.ContractFileType;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import com.rihao.property.modules.lease.contract.enums.SignUpStatus;
import com.rihao.property.modules.lease.contract.mapper.ContractFileMapper;
import com.rihao.property.modules.lease.contract.service.IContractFileService;
import com.rihao.property.modules.lease.contract.service.IContractService;
import com.rihao.property.modules.lease.contract.vo.ContractFileVo;
import com.rihao.property.modules.lease.contract.vo.SettleContractFilesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-07-12
 */
@Service
public class ContractFileServiceImpl extends ServiceImpl<ContractFileMapper, ContractFile> implements IContractFileService {

    private IContractService contractService;

    @Override
    public PageVo<ContractFileVo> search(ContractFileQueryParam contractFileQueryParam) {
        Page<ContractFile> page = new Page<>(contractFileQueryParam.getCurrent(), contractFileQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        ContractFileMapper.QueryParam params = new ContractFileMapper.QueryParam();
        if (contractFileQueryParam.getContractId() != null) {
            params.setContractId(contractFileQueryParam.getContractId());
        }
        Page<ContractFileVo> result = this.getBaseMapper().selectByQueryParam(page, params);
        return PageVo.create(contractFileQueryParam.getCurrent(), contractFileQueryParam.getPageSize(),
                result.getTotal(), result.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createNew(ContractFileCreateVo contractFileCreateVo) {
        ContractFile contractFile = ContractFileConvert.INSTANCE.createVo2Entity(contractFileCreateVo);
        Boolean result = this.save(contractFile);
        Contract contract = this.contractService.getById(contractFileCreateVo.getContractId());
        contract.setStatus(ContractStatus.wait_contract);
        this.contractService.updateById(contract);
        return result;
    }

    @Override
    public boolean delete(Long id) {
        // TODO 删除OSS
        return this.removeById(id);
    }

    @Override
    public Contract settleInContractFiles(SettleContractFilesVo filesVo, ContractFileType type) {
        Contract contract = this.contractService.getById(filesVo.getId());
        if (contract == null) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.DATE_NULL).message("入驻信息不存在"));
        }
        //附件先删除，再新增
        this.remove(new UpdateWrapper<ContractFile>()
                .lambda()
                .eq(ContractFile::getContractId, contract.getId())
                .eq(ContractFile::getFileType, type)
        );
        if (!CollectionUtils.isEmpty(filesVo.getFiles())) {
            this.saveBatch(filesVo.getFiles().stream()
                    .map(item -> new ContractFile()
                            .setContractId(filesVo.getId())
                            .setFilePath(item.getFilePath())
                            .setFileType(type)
                            .setName(item.getName())
                            .setUploader(item.getUploader())
                            .setFileId(item.getFile().getId())
                    ).collect(Collectors.toList()));
        }
        //如果是提交，并且是CONTRACT 合同文件上传，这个方法有两个请求同时调用
        /**
         * api/contract/approve-files 批复文件
         * api/contract/contract-files 合同文件
         */
        if(filesVo.isSubmit() && type == ContractFileType.CONTRACT){
            return this.contractService.submit(contract.getId());
        }
        if(filesVo.isSubmit() && contract.getSignUpStatus().getValue()<SignUpStatus.CONTRACT_UPLOAD.getValue()){
            contract.setSignUpStatus(SignUpStatus.CONTRACT_UPLOAD);
        }
        this.contractService.updateById(contract);
        return contract;
    }

    @Autowired
    private void setContractService(IContractService contractService) {
        this.contractService = contractService;
    }
}
