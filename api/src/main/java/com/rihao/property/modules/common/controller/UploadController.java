package com.rihao.property.modules.common.controller;

import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.oss.IOssService;
import com.rihao.property.modules.common.vo.AliyunOssStsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Ken
 * @date 2020/5/29
 * @description
 */
@RestController
@RequestMapping("/api/admin/upload")
@Api(value = "上传管理", tags = "上传管理")
public class UploadController {

    private final IOssService ossService;

    public UploadController(IOssService ossService) {
        this.ossService = ossService;
    }

    @Log("上传文件")
    @PostMapping("/image")
    @ApiOperation("上传文件")
    public ResBody<?> uploadImage(@RequestParam("file") MultipartFile file, String path) {
        return ResBody.success();
    }

    @Log("上传文件")
    @PostMapping("/image/base64")
    @ApiOperation("上传文件")
    public ResBody<?> uploadBase64Image() {
        return ResBody.success();
    }

    @RequestMapping("/api/oss")
    public AliyunOssStsVo getAliyunOssSts(@RequestParam(defaultValue = "kcw-property") String dir) {
        return this.ossService.getAliyunOssSts(dir);
    }
}
