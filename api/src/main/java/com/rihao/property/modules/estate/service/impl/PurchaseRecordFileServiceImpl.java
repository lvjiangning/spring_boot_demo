package com.rihao.property.modules.estate.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.PurchaseRecordFileQueryParam;
import com.rihao.property.modules.estate.convert.PurchaseRecordFileConvert;
import com.rihao.property.modules.estate.entity.PurchaseRecordFile;
import com.rihao.property.modules.estate.mapper.PurchaseRecordFileMapper;
import com.rihao.property.modules.estate.service.IPurchaseRecordFileService;
import com.rihao.property.modules.estate.vo.PurchaseRecordFileCreateVo;
import com.rihao.property.modules.estate.vo.PurchaseRecordFileVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-07-08
 */
@Service
public class PurchaseRecordFileServiceImpl extends ServiceImpl<PurchaseRecordFileMapper, PurchaseRecordFile> implements IPurchaseRecordFileService {

    @Override
    public PageVo<PurchaseRecordFileVo> search(PurchaseRecordFileQueryParam purchaseRecordFileQueryParam) {
        Page<PurchaseRecordFile> page = new Page<>(purchaseRecordFileQueryParam.getCurrent(), purchaseRecordFileQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        PurchaseRecordFileMapper.QueryParam params = new PurchaseRecordFileMapper.QueryParam();
        if (purchaseRecordFileQueryParam.getRecordId() != null) {
            params.setRecordId(purchaseRecordFileQueryParam.getRecordId());
        }
        Page<PurchaseRecordFileVo> result = this.getBaseMapper().selectByQueryParam(page, params);
        return PageVo.create(purchaseRecordFileQueryParam.getCurrent(), purchaseRecordFileQueryParam.getPageSize(),
                result.getTotal(), result.getRecords());
    }

    @Override
    public boolean createNew(PurchaseRecordFileCreateVo purchaseRecordFileCreateVo) {
        PurchaseRecordFile entity = PurchaseRecordFileConvert.INSTANCE.createVo2Entity(purchaseRecordFileCreateVo);
        return this.save(entity);
    }

    @Override
    public boolean delete(Long id) {
        // TODO
        return this.removeById(id);
    }
}
