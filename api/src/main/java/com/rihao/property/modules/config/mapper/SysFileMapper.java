package com.rihao.property.modules.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rihao.property.modules.config.entity.SysConfig;
import com.rihao.property.modules.config.entity.SysFile;

import java.util.List;

public interface SysFileMapper extends BaseMapper<SysFile> {
    Integer updateFileForBusId(List<Long> fileIds, Long busId,Long type);

    Integer deleteByBusinessId(List<Long> fileIds, Long busId,Long type);
}
