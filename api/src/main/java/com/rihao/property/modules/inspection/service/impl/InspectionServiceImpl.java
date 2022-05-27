package com.rihao.property.modules.inspection.service.impl;

import com.anteng.boot.pojo.query.Filter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rihao.property.common.page.PageParams;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.config.service.ISysFileService;
import com.rihao.property.modules.estate.controller.params.InspectionFileQueryParam;
import com.rihao.property.modules.estate.entity.ParkFile;
import com.rihao.property.modules.estate.mapper.ParkFileMapper;
import com.rihao.property.modules.estate.vo.InspectionFileCreateVo;
import com.rihao.property.modules.estate.vo.ParkFileVo;
import com.rihao.property.modules.inspection.controller.params.InspectionQueryParam;
import com.rihao.property.modules.inspection.convert.InspectionConvert;
import com.rihao.property.modules.inspection.convert.InspectionFileConvert;
import com.rihao.property.modules.inspection.entity.Inspection;
import com.rihao.property.modules.inspection.entity.InspectionFile;
import com.rihao.property.modules.inspection.mapper.InspectionFileMapper;
import com.rihao.property.modules.inspection.mapper.InspectionMapper;
import com.rihao.property.modules.inspection.service.IInspectionFileService;
import com.rihao.property.modules.inspection.service.IInspectionService;
import com.rihao.property.modules.inspection.vo.InspectionCreateVo;
import com.rihao.property.modules.inspection.vo.InspectionFileVo;
import com.rihao.property.modules.inspection.vo.InspectionUpdateVo;
import com.rihao.property.modules.inspection.vo.InspectionVo;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.shiro.util.JwtUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import io.swagger.models.auth.In;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-27
 */
@Service
public class InspectionServiceImpl extends ServiceImpl<InspectionMapper, Inspection> implements IInspectionService {

    private IInspectionFileService fileService;

    @Autowired
    private ISysFileService sysFileService;

    @Override
    public PageVo<InspectionVo> search(InspectionQueryParam inspectionQueryParam) {
        Page<Inspection> page = new Page<>(inspectionQueryParam.getCurrent(), inspectionQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        InspectionMapper.QueryParam params = new InspectionMapper.QueryParam();
        params.setParkIds(JwtUtil.getCurrentUserParkIds());
        params.setLikeParam(inspectionQueryParam.getLikeParam());
        if (StringUtils.hasText(inspectionQueryParam.getLocation())) {
            params.setLocation(Filter.LikeValue.both(inspectionQueryParam.getLocation()));
        }
        if (StringUtils.hasText(inspectionQueryParam.getTitle())) {
            params.setTitle(Filter.LikeValue.both(inspectionQueryParam.getTitle()));
        }
        if (StringUtils.hasText(inspectionQueryParam.getInspector())) {
            params.setInspector(Filter.LikeValue.both(inspectionQueryParam.getInspector()));
        }
        if (StringUtils.hasText(inspectionQueryParam.getParkId())) {
            params.setParkIds(inspectionQueryParam.getParkId());
        }
        //模糊查询的有参数
        if (StringUtils.hasText(inspectionQueryParam.getLikeParam())) {
            params.setTitle(Filter.LikeValue.both(inspectionQueryParam.getLikeParam()));
            params.setInspector(Filter.LikeValue.both(inspectionQueryParam.getLikeParam()));
        }
        SysUser user = JwtUtil.getCurrentUser();
        if (user.getRoleId() != 1) {
            params.setEstablishId(user.getEstablishId());
        }
        Page<InspectionVo> result = this.getBaseMapper().selectByQueryParam(page, params);
        List<InspectionVo> list = result.getRecords();
        for (InspectionVo inspectionVo : list) {
            List<InspectionFile> fileList = this.fileService.getByInspectionId(inspectionVo.getId());
            if (CollectionUtils.isNotEmpty(fileList)){
                for (int i = 0; i < fileList.size(); i++) {
                    fileList.get(i).setSysFile(sysFileService.getById(fileList.get(i).getFileId()));
                }
            }
            inspectionVo.setFileList(fileList);
        }

        return PageVo.create(inspectionQueryParam.getCurrent(), inspectionQueryParam.getPageSize(),
                result.getTotal(), list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createNew(InspectionCreateVo inspectionCreateVo) {
        Inspection entity = InspectionConvert.INSTANCE.createParam2Entity(inspectionCreateVo);
        Boolean result = this.save(entity);
        if (CollectionUtils.isNotEmpty(inspectionCreateVo.getFiles())) {
            for (InspectionFile inspectionFile : inspectionCreateVo.getFiles()) {
                InspectionFileCreateVo createVo = new InspectionFileCreateVo();
                createVo.setName(inspectionFile.getName());
                createVo.setInspectionId(entity.getId());
                createVo.setFileUrl(inspectionFile.getFileUrl());
                createVo.setFileId(inspectionFile.getFileId());
                this.fileService.createFile(createVo);
            }
        }
        return result;
    }

    @Override
    public InspectionVo detail(Long id) {
        return InspectionConvert.INSTANCE.entity2Vo(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(InspectionUpdateVo inspectionUpdateVo) {
        Inspection inspection = InspectionConvert.INSTANCE.updateParam2Entity(inspectionUpdateVo);
         this.updateById(inspection);
         //处理附件
        if (CollectionUtils.isNotEmpty(inspectionUpdateVo.getFiles())){
            List<InspectionFile> files = inspectionUpdateVo.getFiles();
            List<Long> existIds=new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {
                InspectionFile inspectionFile = files.get(i);
                if (inspectionFile.getId() == null) { //id 为空表示新增
                    InspectionFileCreateVo createVo = new InspectionFileCreateVo();
                    createVo.setName(inspectionFile.getName());
                    createVo.setInspectionId(inspection.getId());
                    createVo.setFileUrl(inspectionFile.getFileUrl());
                    createVo.setFileId(inspectionFile.getFileId());
                    inspectionFile = this.fileService.createFile(createVo);
                }
                existIds.add(inspectionFile.getId());
            }
            //删除无用的
             this.fileService.deleteFileByNotExist(existIds,inspection.getId());
        }else {
            //删除无用的
            this.fileService.deleteFileByNotExist(null,inspection.getId());
        }
        return true;
    }

    @Autowired
    private void setFileService(IInspectionFileService fileService) {
        this.fileService = fileService;
    }
}
