package com.rihao.property.modules.ent.controller;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageParams;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.ent.controller.params.*;
import com.rihao.property.modules.ent.entity.Ent;
import com.rihao.property.modules.ent.entity.EntUsedName;
import com.rihao.property.modules.ent.service.IAliyunOssService;
import com.rihao.property.modules.ent.service.IEntService;
import com.rihao.property.modules.ent.vo.*;
import com.rihao.property.shiro.util.JwtUtil;
import com.rihao.property.util.excel.ExcelException;
import com.rihao.property.util.excel.ExcelUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-26
 */
@RestController
@RequestMapping("/api/ent")
public class EntController extends BaseController {

    private IEntService entService;
    private IAliyunOssService aliyunOssService;

    @Log("企业列表")
    @GetMapping("page")
    public ResBody<?> search(EntQueryParam entQueryParam) {
        PageVo<EntVo> page = this.entService.search(entQueryParam);
        return ResBody.success(page);
    }

    @Log("企业列表导出")
    @GetMapping("export")
    @ApiOperation("企业列表导出")
    public void searchExport(EntQueryParam entQueryParam, HttpServletResponse response) {
         this.entService.searchExport(entQueryParam,response);
    }

   /* @Log("新建企业")
    @PostMapping
    public ResBody<?> createNew(@RequestBody @Valid EntCreateVo entCreateVo) {
        Ent result = this.entService.createNew(entCreateVo);
        if (result != null) {
            addLog("新建企业-" + entCreateVo.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }*/

    @Log("企业详细")
    @GetMapping("/{id}")
    public ResBody<?> detail(@PathVariable String id) {
        EntVo entVo = this.entService.detail(id);
        return ResBody.success(entVo);
    }

    @Log("修改企业")
    @PutMapping
    public ResBody<?> update(@RequestBody @Valid EntUpdateVo entUpdateVo) {
        Boolean result = this.entService.update(entUpdateVo);
        if (result == null || result) {
            addLog("修改企业-" + entUpdateVo.getName(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("删除企业")
    @DeleteMapping("/{id}")
    @ApiOperation("删除企业")
    public ResBody<?> delete(@PathVariable Long id) {
        boolean result = this.entService.delete(id);
        if (result) {
            addLog("删除企业-" + id, JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("删除成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("查询重复入驻")
    @GetMapping("/duplicate")
    public ResBody<?> duplicate(EntQueryParam entQueryParam) {
        PageVo<EntVo> entVos = this.entService.duplicate(entQueryParam);
        return ResBody.success(entVos);
    }

    @Log("企业更名")
    @PutMapping("update_name")
    public ResBody<?> update_name(@RequestBody @Valid EntUpdateNameVo entUpdateNameVo) {
        Boolean result = this.entService.updateName(entUpdateNameVo);
        if (result == null || result) {
            addLog("变更企业名称" + entUpdateNameVo.getNewName(),
                    JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("变更企业名称成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("企业曾用名列表")
    @GetMapping("used_name")
    public ResBody<?> queryUsedName(EntUsedNameQueryParam entUsedNameQueryParam) {
        PageVo<EntUsedName> page = this.entService.queryUsedName(entUsedNameQueryParam);
        return ResBody.success(page);
    }

    @Log("企业变更历史")
    @GetMapping("change_history")
    public ResBody<?> changeHistoryPage(EntChangeHistoryQueryParam entChangeHistoryQueryParam) {
        PageVo<EntChangeHistoryVo> page = this.entService.queryHistory(entChangeHistoryQueryParam);
        return ResBody.success(page);
    }

    @Log("企业变更详细")
    @GetMapping("change_history_detail")
    public ResBody<?> changeHistoryDetail(EntChangeHistoryDetailQueryParam entChangeHistoryDetailQueryParam) {
        PageVo<EntChangeHistoryDetailVo> entChangeHistoryDetailPageVo
                = this.entService.queryChangeDetail(entChangeHistoryDetailQueryParam);
        return ResBody.success(entChangeHistoryDetailPageVo);
    }

    @Log("企业画像")
    @GetMapping("protrait/{id}")
    public ResBody<?> protrait(@PathVariable Long id) {
        ProtraitVo protraitVo = this.entService.getProtriatVo(id);
        return ResBody.success(protraitVo);
    }

    @Log("未签订合同的企业列表")
    @GetMapping("new_settle_list")
    public ResBody<?> searchNewSettleList() {
        List<KeyValueVo> list = this.entService.searchNewSettleList();
        return ResBody.success(list);
    }

    @Log("已经签订合同的企业列表")
    @GetMapping("old_settle_list")
    public ResBody<?> searchOldSettleList() {
        List<KeyValueVo> list = this.entService.searchOldSettleList();
        return ResBody.success(list);
    }

    @Log("企业key-value")
    @GetMapping("key_value_list")
    public ResBody<?> keyValuesList() {
        List<KeyValueVo> list = this.entService.getKeyValueList();
        return ResBody.success(list);
    }

    @Log("根据名称获取公司")
    @GetMapping("get_by_name")
    public ResBody<?> getByName(String entName) {
        Ent ent = this.entService.getByName(entName);
        return ResBody.success(ent);
    }

    @Log("根据Excel查询入驻重复")
    @PostMapping("duplicate_excel")
    public ResBody<?> duplicate_excel(PageParams params, @RequestParam("file") MultipartFile file) throws ExcelException {
        try {
            List[] results = ExcelUtil.readExcel(file, DuplicateSearch.class);
            PageVo<EntVo> entVos = this.entService.duplicate_excel(params, results);
            return ResBody.success(entVos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResBody.failure(e.getMessage());
        }
    }

    @Autowired
    private void setService(IEntService entService, IAliyunOssService aliyunOssService) {
        this.entService = entService;
        this.aliyunOssService = aliyunOssService;
    }
}