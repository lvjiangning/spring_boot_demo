package com.rihao.property.modules.config.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.modules.config.service.IListInfoService;
import com.rihao.property.modules.config.service.ISysConfigService;
import com.rihao.property.modules.config.vo.ListInfoVo;
import com.rihao.property.modules.config.vo.SysConfigVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/api/listInfo")
@Api("自定义列表")
public class ListInfoController extends BaseController {

   @Autowired
   private IListInfoService listInfoService;

    @Log("查询自定义列表配置")
    @GetMapping("/getListInfoByUser")
    @ApiOperation("查询自定义列表结果")
    public ResBody<?> getListInfoByUser(ListInfoVo listInfoVo) {
        return ResBody.success(this.listInfoService.getListInfo(listInfoVo));
    }

    @Log("修改自定义列表配置")
    @PostMapping("/update")
    @ApiOperation("更新自定义列表")
    public ResBody<?> update(@RequestBody ListInfoVo listInfoVo) {
        String result = this.listInfoService.save(listInfoVo);
        if (StringUtils.isBlank(result)) {
            addLog("修改自定义列表成功", JwtUtil.getUsername());
            return ResBody.success().message("配置成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }
}