package com.rihao.property.modules.config.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.modules.config.service.ISysConfigService;
import com.rihao.property.modules.config.vo.SysConfigVo;
import com.rihao.property.shiro.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统参数设置 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-04-21
 */
@RestController
@RequestMapping("/api/config")
public class SysConfigController extends BaseController {

    private final ISysConfigService configService;

    public SysConfigController(ISysConfigService configService) {
        this.configService = configService;
    }

    @Log("查询系统配置")
    @GetMapping("/{estId}")
    public ResBody<?> getConfig(@PathVariable Long estId) {
        return ResBody.success(this.configService.getConfig(estId));
    }

    @Log("修改系统配置")
    @PostMapping("/update")
    public ResBody<?> updateConfig(@RequestBody SysConfigVo configVo) {
        Boolean result = this.configService.update(configVo);
        if (result) {
            addLog("修改系统配置", JwtUtil.getUsername());
            return ResBody.success().message("配置成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }
}