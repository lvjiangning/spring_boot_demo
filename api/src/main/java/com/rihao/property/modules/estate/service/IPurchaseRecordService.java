package com.rihao.property.modules.estate.service;

import com.rihao.property.common.enums.EnableState;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.PurchaseRecordQueryParam;
import com.rihao.property.modules.estate.entity.PurchaseRecord;
import com.rihao.property.modules.estate.vo.PurchaseRecordVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-09
 */
public interface IPurchaseRecordService extends IService<PurchaseRecord> {

    PageVo<PurchaseRecordVo> search(PurchaseRecordQueryParam purchaseRecordQueryParam) throws ParseException;

    Boolean createNew(PurchaseRecordVo purchaseRecordVo);

    PurchaseRecordVo detail(Long id) throws ParseException;

    Boolean update(PurchaseRecordVo purchaseRecordVo);

    String upload_file(Long id, MultipartFile file) throws IOException;

    PurchaseRecord getEnableRecord(Long parkId);

    Boolean changeState(Long id, EnableState state);

    Float getManageFee(Long pardId);

    List<PurchaseRecordVo> getListByParkId(Long id);
}
