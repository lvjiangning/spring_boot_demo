package com.rihao.property.modules.system.mapper;

import com.rihao.property.modules.system.entity.SysLog;
import com.rihao.property.modules.system.vo.SysLogVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统日志表 Mapper 接口
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
public interface SysLogMapper extends BaseMapper<SysLog> {

    Page<SysLogVo> selectByQueryParam(Page<SysLog> page, @Param("params") SysLogMapper.QueryParam params);

    @Data
    @Accessors(chain = true)
    class QueryParam{
        private String operName;

        private String description;

        private LocalDateTime operStartTime;

        private LocalDateTime operEndTime;
    }

}
