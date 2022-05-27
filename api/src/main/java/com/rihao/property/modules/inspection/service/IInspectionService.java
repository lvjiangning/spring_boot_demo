package com.rihao.property.modules.inspection.service;

import com.rihao.property.common.page.PageParams;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.InspectionFileQueryParam;
import com.rihao.property.modules.estate.vo.InspectionFileCreateVo;
import com.rihao.property.modules.inspection.controller.params.InspectionQueryParam;
import com.rihao.property.modules.inspection.entity.Inspection;
import com.rihao.property.modules.inspection.entity.InspectionFile;
import com.rihao.property.modules.inspection.vo.InspectionCreateVo;
import com.rihao.property.modules.inspection.vo.InspectionFileVo;
import com.rihao.property.modules.inspection.vo.InspectionUpdateVo;
import com.rihao.property.modules.inspection.vo.InspectionVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-27
 */
public interface IInspectionService extends IService<Inspection> {

    PageVo<InspectionVo> search(InspectionQueryParam inspectionQueryParam);

    Boolean createNew(InspectionCreateVo inspectionCreateVo);

    InspectionVo detail(Long id);

    Boolean update(InspectionUpdateVo inspectionUpdateVo);


}
