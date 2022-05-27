package com.rihao.property.modules.cost.controller;


import com.alibaba.excel.EasyExcel;
import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.cost.controller.params.CostQueryParam;
import com.rihao.property.modules.cost.controller.params.RentExcelModel;
import com.rihao.property.modules.cost.service.ICostService;
import com.rihao.property.modules.cost.vo.CostVo;
import com.rihao.property.modules.cost.vo.DepositExcelModel;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-30
 */
@RestController
@RequestMapping("/api/cost")
public class CostController extends BaseController {

    private final ICostService costService;

    public CostController(ICostService costService) {
        this.costService = costService;
    }

    @Log("费用列表")
    @GetMapping("page")
    @ApiOperation("费用列表")
    public ResBody<?> search(CostQueryParam costQueryParam) throws ParseException {
        PageVo<CostVo> page = this.costService.search(costQueryParam);
        addLog("查看费用列表", JwtUtil.getUsername());
        return ResBody.success(page);
    }

    @Log("押金列表")
    @GetMapping("deposit/page")
    @ApiOperation("押金列表")
    public ResBody<?> deposit(CostQueryParam costQueryParam) throws ParseException {
        PageVo<CostVo> page = this.costService.depositPage(costQueryParam);
        addLog("查看押金列表", JwtUtil.getUsername());
        return ResBody.success(page);
    }

    @Log("添加费用")
    @PostMapping("add")
    public ResBody<?> createNew(@RequestBody @Valid CostVo costVo) {
        Boolean result = this.costService.createNew(costVo);
        if (result) {
            addLog("添加费用-" + costVo.getEntName() + "-" + costVo.getCostTime() + "-" + costVo.getTypeId(),
                    JwtUtil.getUsername());
            return ResBody.success().message("添加成功");
        } else
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("设置状态")
    @PostMapping("status")
    public ResBody<?> setStatus(Long id, int status) {
        this.costService.setStatus(id, status);
        return ResBody.success();
    }

    @Log("费用详细")
    @GetMapping("/{id}")
    public ResBody<?> detail(@PathVariable String id) throws ParseException {
        CostVo costVo = this.costService.detail(id);
        return ResBody.success(costVo);
    }

    @Log("导出押金清单")
    @GetMapping("/export/deposit")
    public void export_deposit(HttpServletResponse response) throws IOException {
        List<DepositExcelModel> list = this.costService.getDepositList();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), DepositExcelModel.class).sheet("模板").doWrite(list);
    }

    @Log("导出房租列表")
    @GetMapping("/export/rent")
    public void export_rent(HttpServletResponse response) throws IOException {
        List<RentExcelModel> list = this.costService.getRentList();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), RentExcelModel.class).sheet("模板").doWrite(list);
    }
}