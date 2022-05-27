package com.rihao.property.modules.system.controller;

import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.view.Converters;
import com.anteng.boot.web.bind.view.ExcelViewFactory;
import com.anteng.boot.web.bind.view.Sheet;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.system.controller.params.AdminQueryParam;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.modules.system.vo.SysUserVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author gaoy
 * 2020/4/16/016
 */
@Controller
@Slf4j
@Api(value = "导出", tags = "导出管理")
public class SysExportController extends BaseController {
    private ISysUserService userService;

    @GetMapping("/admin/sys-user/export")
    @ApiOperation("用户列表导出")
    public ModelAndView exportUserList(AdminQueryParam queryParam) {
        Converters.ObjectToStringConverter<KeyValueVo> kvConvertor
                = new Converters.ObjectToStringConverter<>(KeyValueVo.class, obj -> obj == null ? "" : obj.getValue() + "");
        ArrayConverter arrayConverter = new ArrayConverter(KeyValueVo[].class, kvConvertor);
        arrayConverter.setOnlyFirstToString(false);
        queryParam.setPageSize(-1);
        PageVo<SysUserVo> result = this.userService.search(JwtUtil.getCurrentUser(), queryParam);
        Sheet sheet = new Sheet(SysUserVo.class, result.getContent());
        sheet.setSheetName("用户列表");
        sheet.addConverter("receiverObjs", arrayConverter);
        try {
            return new ModelAndView(ExcelViewFactory.buildExcelView("用户列表.xlsx", sheet));
        } catch (Exception e) {
            log.error("导出失败", e);
            return new ModelAndView("error");
        }
    }

    @Autowired
    public void setUserService(ISysUserService userService) {
        this.userService = userService;
    }
}
