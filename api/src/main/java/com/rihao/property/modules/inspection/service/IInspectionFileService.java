package com.rihao.property.modules.inspection.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.InspectionFileQueryParam;
import com.rihao.property.modules.estate.vo.InspectionFileCreateVo;
import com.rihao.property.modules.inspection.entity.InspectionFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.modules.inspection.vo.InspectionFileVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-08-13
 */
public interface IInspectionFileService extends IService<InspectionFile> {

    InspectionFile createFile(InspectionFileCreateVo inspectionFileCreateVo);
    Boolean deleteFileByNotExist(List<Long> ids,Long inspectionId);

    PageVo<InspectionFileVo> getFilePage(InspectionFileQueryParam inspectionFileQueryParam);

    List<InspectionFile> getByInspectionId(Long id);
}
