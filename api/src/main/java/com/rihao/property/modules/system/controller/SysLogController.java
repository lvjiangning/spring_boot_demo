package com.rihao.property.modules.system.controller;


import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.LogQueryParam;
import com.rihao.property.modules.system.service.ISysLogService;
import com.rihao.property.modules.system.vo.SysLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
@RestController
@RequestMapping("/api/admin/sys-log")
@Api(value = "操作日志", tags = "操作日志")
public class SysLogController extends BaseController {

    private ISysLogService logService;

    @Log("操作日志列表")
    @PostMapping("/list")
    @ApiOperation(value = "操作日志列表", response = SysLogVo.class, responseContainer = "list")
    public ResBody<?> findList(LogQueryParam queryParam) {
        PageVo<SysLogVo> result = this.logService.search(queryParam);
        return ResBody.success(result);
    }

    @Autowired
    public void setLogService(ISysLogService logService) {
        this.logService = logService;
    }
}

