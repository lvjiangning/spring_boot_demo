package com.rihao.property.modules.estate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rihao.property.common.enums.EnableState;
import com.rihao.property.common.oss.IOssService;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.common.vo.UploadResultVo;
import com.rihao.property.modules.config.emuns.SysFileType;
import com.rihao.property.modules.config.service.ISysFileService;
import com.rihao.property.modules.estate.controller.params.PurchaseRecordQueryParam;
import com.rihao.property.modules.estate.convert.PurchaseRecordConvert;
import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.entity.PurchaseRecord;
import com.rihao.property.modules.estate.mapper.PurchaseRecordMapper;
import com.rihao.property.modules.estate.service.IBuildingService;
import com.rihao.property.modules.estate.service.IParkService;
import com.rihao.property.modules.estate.service.IPurchaseRecordService;
import com.rihao.property.modules.estate.vo.PurchaseRecordVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-09
 */
@Service
public class PurchaseRecordServiceImpl extends ServiceImpl<PurchaseRecordMapper, PurchaseRecord> implements IPurchaseRecordService {

    private IOssService ossService;
    private IParkService parkService;
    private ISysFileService sysFileService;

    @Override
    public PageVo<PurchaseRecordVo> search(PurchaseRecordQueryParam purchaseRecordQueryParam) throws ParseException {
        Page<PurchaseRecord> page = new Page<>(purchaseRecordQueryParam.getCurrent(), purchaseRecordQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        QueryWrapper<PurchaseRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PurchaseRecord::getParkId, purchaseRecordQueryParam.getParkId());
        Page<PurchaseRecord> result = this.page(page, wrapper);
        List<PurchaseRecord> list = result.getRecords();
        for (PurchaseRecord purchaseRecord : list) {
            if (purchaseRecord.getWinningDate() != null)
                purchaseRecord.setWinningDate(purchaseRecord.getWinningDate().substring(0, 10));
            if (purchaseRecord.getSignDate() != null)
                purchaseRecord.setSignDate(purchaseRecord.getSignDate().substring(0, 10));
            if (purchaseRecord.getBiddingDate() != null)
                purchaseRecord.setBiddingDate(purchaseRecord.getBiddingDate().substring(0, 10));
            //设置合同开始和结束时间
            if(purchaseRecord.getContractStartDate() != null){
                purchaseRecord.setContractStartDate(purchaseRecord.getContractStartDate().substring(0, 10));
            }
            if(purchaseRecord.getContractEndDate() != null){
                purchaseRecord.setContractEndDate(purchaseRecord.getContractEndDate().substring(0, 10));
            }
        }
        List<PurchaseRecordVo> voList = PurchaseRecordConvert.INSTANCE.entity2ListItemBatch(list);
        for (PurchaseRecordVo purchaseRecordVo : voList) {
          /*  String signDate = purchaseRecordVo.getSignDate();
            String prefix = signDate.substring(0, 4);
            String suffix = signDate.substring(4, 10);
            prefix = (Integer.parseInt(prefix) + Integer.parseInt(purchaseRecordVo.getContractTerm())) + "";
            purchaseRecordVo.setDueDate(prefix + suffix);*/

            Park park = this.parkService.getById(purchaseRecordVo.getParkId());
            purchaseRecordVo.setParkName(park.getName());
        }

        return PageVo.create(purchaseRecordQueryParam.getCurrent(), purchaseRecordQueryParam.getPageSize(),
                result.getTotal(), voList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createNew(PurchaseRecordVo purchaseRecordVo) {
        QueryWrapper<PurchaseRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PurchaseRecord::getParkId, purchaseRecordVo.getParkId());
        List<PurchaseRecord> records = this.list(wrapper);
        for (PurchaseRecord record : records) {
            record.setState(EnableState.disable);
            this.updateById(record);
        }

        PurchaseRecord purchaseRecord = PurchaseRecordConvert.INSTANCE.createParam2Entity(purchaseRecordVo);
        Boolean saveStatus= this.save(purchaseRecord);

        if (saveStatus){// 为true
            //保存附件
            if (CollectionUtils.isNotEmpty(purchaseRecordVo.getFileIds())){
              return   sysFileService.updateFileForBusId(purchaseRecordVo.getFileIds(),purchaseRecord.getId(), SysFileType.PURCHASERECORD.getValue());
            }
            return saveStatus;
        }
        return false;
    }

    @Override
    public PurchaseRecordVo detail(Long id) throws ParseException {
        PurchaseRecordVo purchaseRecordVo = PurchaseRecordConvert.INSTANCE.entity2Vo(this.getById(id));
        if (purchaseRecordVo.getWinningDate() != null)
            purchaseRecordVo.setWinningDate(purchaseRecordVo.getWinningDate().substring(0, 10));
        if (purchaseRecordVo.getSignDate() != null)
            purchaseRecordVo.setSignDate(purchaseRecordVo.getSignDate().substring(0, 10));
        if (purchaseRecordVo.getBiddingDate() != null)
            purchaseRecordVo.setBiddingDate(purchaseRecordVo.getBiddingDate().substring(0, 10));
        return purchaseRecordVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(PurchaseRecordVo purchaseRecordVo) {
        PurchaseRecord record = getById(purchaseRecordVo.getId());
        PurchaseRecord purchaseRecord = PurchaseRecordConvert.INSTANCE.createParam2Entity(purchaseRecordVo);
        //修改数据时需要重新设置数据的状态，不然会默认启用状态
        if(record != null && record.getState() != null){
            purchaseRecord.setState(record.getState());
        }
        boolean b = this.updateById(purchaseRecord);
        //更新附件相关信息
        if (b){
            if (CollectionUtils.isNotEmpty(purchaseRecordVo.getFileIds())){
                //先删除不在附件列表中的
                this.sysFileService.deleteByBusinessId(purchaseRecordVo.getFileIds(),purchaseRecord.getId(),SysFileType.PURCHASERECORD.getValue());
                //再保存当前的
                return this.sysFileService.updateFileForBusId(purchaseRecordVo.getFileIds(),purchaseRecord.getId(),SysFileType.PURCHASERECORD.getValue());
            }
            return b;
        }
        return  false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload_file(Long id, MultipartFile file) throws IOException {
        PurchaseRecord record = this.getById(id);
        String name = record.getWinningCompany() + "-招标文件";
        UploadResultVo uploadResultVo = this.ossService.uploadFile(name, file);
        record.setFilePath(uploadResultVo.getUrl());
        this.updateById(record);
        return uploadResultVo.getUrl();
    }

    @Override
    public PurchaseRecord getEnableRecord(Long parkId) {
        QueryWrapper<PurchaseRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(PurchaseRecord::getParkId, parkId)
                .eq(PurchaseRecord::getState, EnableState.enable);
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeState(Long id, EnableState state) {
        PurchaseRecord record = this.getById(id);

        // 如果是启用，则禁用其他所有的记录
        QueryWrapper<PurchaseRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PurchaseRecord::getParkId, record.getParkId());
        if (state.equals(EnableState.enable)) {
            List<PurchaseRecord> records = this.list(wrapper);
            for (PurchaseRecord purchaseRecord : records) {
                purchaseRecord.setState(EnableState.disable);
                this.updateById(purchaseRecord);
            }
        }

        record.setState(state);
        Boolean result = this.updateById(record);

        return result;
    }

    @Override
    public Float getManageFee(Long pardId) {
        PurchaseRecord record = this.getEnableRecord(pardId);
        if (record != null) {
            return Float.parseFloat(record.getManagementFee());
        }
        return null;
    }

    @Override
    public List<PurchaseRecordVo> getListByParkId(Long id) {
        QueryWrapper<PurchaseRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PurchaseRecord::getParkId, id);
        return PurchaseRecordConvert.INSTANCE.entity2ListItemBatch(this.list(wrapper));
    }

    @Autowired
    private void setOssService(IOssService ossService) {
        this.ossService = ossService;
    }

    @Autowired
    private void setParkService(IParkService parkService) {
        this.parkService = parkService;
    }
    @Autowired
    private void setSysFileService(ISysFileService sysFileService) {
        this.sysFileService = sysFileService;
    }
}
