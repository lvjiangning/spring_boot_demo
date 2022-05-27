package com.rihao.property.modules.estate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.common.vo.FileVo;
import com.rihao.property.modules.config.emuns.SysFileType;
import com.rihao.property.modules.config.entity.SysFile;
import com.rihao.property.modules.config.service.ISysFileService;
import com.rihao.property.modules.estate.controller.params.ParkFileQueryParam;
import com.rihao.property.modules.estate.convert.ParkFileConvert;
import com.rihao.property.modules.estate.entity.ParkFile;
import com.rihao.property.modules.estate.mapper.ParkFileMapper;
import com.rihao.property.modules.estate.service.IParkFileService;
import com.rihao.property.modules.estate.vo.ParkFileCreateVo;
import com.rihao.property.modules.estate.vo.ParkFileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-06-11
 */
@Service
public class ParkFileServiceImpl extends ServiceImpl<ParkFileMapper, ParkFile> implements IParkFileService {

    private ISysFileService sysFileService;

    @Autowired
    private void setSysFileService(ISysFileService sysFileService) {
        this.sysFileService = sysFileService;
    }

    @Override
    public PageVo<ParkFileVo> search(ParkFileQueryParam parkFileQueryParam) {
        Page<ParkFile> page = new Page<>(parkFileQueryParam.getCurrent(), parkFileQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        ParkFileMapper.QueryParam params = new ParkFileMapper.QueryParam();
        if (parkFileQueryParam.getParkId() != null) {
            params.setParkId(parkFileQueryParam.getParkId());
        }
        Page<ParkFileVo> result = this.getBaseMapper().selectByQueryParam(page, params);
        return PageVo.create(parkFileQueryParam.getCurrent(), parkFileQueryParam.getPageSize(),
                result.getTotal(), result.getRecords());
    }

    @Override
    public boolean createNew(ParkFileCreateVo parkFileCreateVo) {
        // TODO
        for (SysFile file : parkFileCreateVo.getFiles()) {
            ParkFile parkFile = new ParkFile();
            parkFile.setParkId(parkFileCreateVo.getParkId());
            parkFile.setName(parkFileCreateVo.getName());
            parkFile.setFileUrl(file.getFilePath());
            this.save(parkFile);
            //处理附件
            List<Long> files=new ArrayList<>();
            files.add(file.getId());
            sysFileService.updateFileForBusId(files,parkFile.getId(), SysFileType.PARKFILE.getValue());
        }
        return true;
    }

    @Override
    public boolean delete(Long id) {
        boolean b = this.removeById(id);
        if (b && id !=null){
            // 删除附件
          return   this.sysFileService.deleteByBusinessId(null,id,SysFileType.PARKFILE.getValue());
        }
        return false;
    }

    @Override
    public List<ParkFileVo> getByParkId(Long id) {
        QueryWrapper<ParkFile> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ParkFile::getParkId, id);
        return ParkFileConvert.INSTANCE.entityList2VoList(this.list(wrapper));
    }
}
