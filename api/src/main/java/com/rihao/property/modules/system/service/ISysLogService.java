package com.rihao.property.modules.system.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.LogQueryParam;
import com.rihao.property.modules.system.entity.SysLog;
import com.rihao.property.modules.system.vo.SysLogVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
public interface ISysLogService extends IService<SysLog> {

    PageVo<SysLogVo> search(LogQueryParam queryParam);

    /**
     * aop记录日志
     * @param log
     */
    void saveLog(SysLog log);

}
