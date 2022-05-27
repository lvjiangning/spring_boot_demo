package com.rihao.property.modules.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.InspectionFileQueryParam;
import com.rihao.property.modules.estate.vo.InspectionFileCreateVo;
import com.rihao.property.modules.inspection.convert.InspectionFileConvert;
import com.rihao.property.modules.inspection.entity.InspectionFile;
import com.rihao.property.modules.inspection.mapper.InspectionFileMapper;
import com.rihao.property.modules.inspection.service.IInspectionFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rihao.property.modules.inspection.vo.InspectionFileVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-08-13
 */
@Service
public class InspectionFileServiceImpl extends ServiceImpl<InspectionFileMapper, InspectionFile> implements IInspectionFileService {

    @Override
    public InspectionFile createFile(InspectionFileCreateVo inspectionFileCreateVo) {
        InspectionFile file = InspectionFileConvert.INSTANCE.createVo2Entity(inspectionFileCreateVo);
         this.save(file);
        return file;
    }

    @Override
    public Boolean deleteFileByNotExist(List<Long> ids, Long inspectionId) {
        this.getBaseMapper().deleteFileByNotExist(ids,inspectionId);
        return true;
    }


    @Override
    public PageVo<InspectionFileVo> getFilePage(InspectionFileQueryParam inspectionFileQueryParam) {
        Page<InspectionFile> page = new Page<>(inspectionFileQueryParam.getCurrent(), inspectionFileQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        InspectionFileMapper.QueryParam params = new InspectionFileMapper.QueryParam();
        if (inspectionFileQueryParam.getInspectionId() != null) {
            params.setInspectionId(inspectionFileQueryParam.getInspectionId());
        }
        Page<InspectionFileVo> result = this.getBaseMapper().selectByQueryParam(page, params);
        return PageVo.create(inspectionFileQueryParam.getCurrent(), inspectionFileQueryParam.getPageSize(),
                result.getTotal(), result.getRecords());
    }

    @Override
    public List<InspectionFile> getByInspectionId(Long id) {
        QueryWrapper<InspectionFile> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InspectionFile::getInspectionId, id);
        return this.list(wrapper);
    }
}
