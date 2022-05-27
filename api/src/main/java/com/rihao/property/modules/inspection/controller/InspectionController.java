package com.rihao.property.modules.inspection.controller;


import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageParams;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.InspectionFileQueryParam;
import com.rihao.property.modules.estate.vo.InspectionFileCreateVo;
import com.rihao.property.modules.estate.vo.ParkFileCreateVo;
import com.rihao.property.modules.inspection.controller.params.InspectionQueryParam;
import com.rihao.property.modules.inspection.entity.InspectionFile;
import com.rihao.property.modules.inspection.service.IInspectionFileService;
import com.rihao.property.modules.inspection.service.IInspectionService;
import com.rihao.property.modules.inspection.vo.InspectionCreateVo;
import com.rihao.property.modules.inspection.vo.InspectionFileVo;
import com.rihao.property.modules.inspection.vo.InspectionUpdateVo;
import com.rihao.property.modules.inspection.vo.InspectionVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-27
 */
@RestController
@RequestMapping("/api/inspection")
public class InspectionController extends BaseController {

    private IInspectionService inspectionService;
    private IInspectionFileService fileService;

    @Log("巡查列表")
    @GetMapping("page")
    @ApiOperation("巡查列表")
    public ResBody<?> getInspectionPage(InspectionQueryParam inspectionQueryParam) {
        PageVo<InspectionVo> page = this.inspectionService.search(inspectionQueryParam);
        addLog("查看巡查列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("新建巡查记录")
    @PostMapping
    @ApiOperation("新建巡查记录")
    public ResBody<?> createNew(@RequestBody @Valid InspectionCreateVo inspectionCreateVo) {
        if (inspectionCreateVo.getContent() != null) {
            int length = StringUtils.length(inspectionCreateVo.getContent());
            if (length > 500) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("重大情况长度超出限制！"));
            }
        }
        Boolean result = this.inspectionService.createNew(inspectionCreateVo);
        if (result == null || result) {
            addLog("新建巡查记录-：" + inspectionCreateVo.getTitle(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("新建成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("修改巡查记录")
    @PutMapping
    @ApiOperation("修改巡查记录")
    public ResBody<?> update(@RequestBody @Valid InspectionUpdateVo inspectionUpdateVo) {
        if (inspectionUpdateVo.getContent() != null) {
            int length = StringUtils.length(inspectionUpdateVo.getContent());
            if (length > 500) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("重大情况长度超出限制！"));
            }
        }
        Boolean result = this.inspectionService.update(inspectionUpdateVo);
        if (result == null || result) {
            addLog("修改巡查记录-" + inspectionUpdateVo.getTitle(), JwtUtil.getCurrentUser().getRealName());
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @Log("巡查详情")
    @GetMapping("/{id}")
    public ResBody<?> detail(@PathVariable Long id) {
        InspectionVo vo = this.inspectionService.detail(id);
        addLog("查看巡查详情-" + vo.getTitle(), JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(vo);
    }

    @Log("删除巡查详情")
    @DeleteMapping("/{id}")
    public ResBody<?> delete(@PathVariable String id) {
        this.inspectionService.removeById(id);
        addLog("删除巡查详情-" + id, JwtUtil.getCurrentUser().getRealName());
        return ResBody.success();
    }

    @Log("新建资料")
    @PostMapping("file")
    @ApiOperation("新建资料")
    public ResBody<?> createFile(@RequestBody InspectionFileCreateVo inspectionFileCreateVo) {
        InspectionFile file = this.fileService.createFile(inspectionFileCreateVo);
        if (file !=null && file.getId() !=null){
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @DeleteMapping("/file/{id}")
    public ResBody<?> deleteFile(@PathVariable Long id) {
        this.fileService.removeById(id);
        addLog("删除文件-" + id, JwtUtil.getCurrentUser().getRealName());
        return ResBody.success();
    }

    @GetMapping("file/page")
    public ResBody<?> searchFile(InspectionFileQueryParam inspectionFileQueryParam) {
        PageVo<InspectionFileVo> page = this.fileService.getFilePage(inspectionFileQueryParam);
        return ResBody.success(page);
    }

    @Autowired
    private void setFileService(IInspectionFileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    private void setInspectionService(IInspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }
}

