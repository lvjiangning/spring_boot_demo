package com.rihao.property.modules.estate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.PurchaseRecordFileQueryParam;
import com.rihao.property.modules.estate.entity.PurchaseRecordFile;
import com.rihao.property.modules.estate.vo.PurchaseRecordFileCreateVo;
import com.rihao.property.modules.estate.vo.PurchaseRecordFileVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-07-08
 */
public interface IPurchaseRecordFileService extends IService<PurchaseRecordFile> {

    PageVo<PurchaseRecordFileVo> search(PurchaseRecordFileQueryParam purchaseRecordFileQueryParam);

    boolean createNew(PurchaseRecordFileCreateVo purchaseRecordFileCreateVo);

    boolean delete(Long id);
}
