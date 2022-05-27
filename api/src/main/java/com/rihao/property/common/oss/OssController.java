package com.rihao.property.common.oss;

import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.modules.common.vo.UploadResultVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * @author gaoy
 * 2020/2/26/026
 */
@RestController
@RequestMapping("/api/oss")
public class OssController {

    private IOssService ossService;

    @Log("上传资料")
    @PostMapping("upload")
    @ApiOperation("上传资料")
    public ResBody<?> upload_file(@RequestParam("file") MultipartFile file) throws IOException {
        UploadResultVo uploadResultVo = this.ossService.uploadFile(
                Objects.requireNonNull(file.getOriginalFilename()).substring(0, file.getOriginalFilename().lastIndexOf(".")), file);
        return ResBody.success(uploadResultVo);
    }

    @Autowired
    private void setOssService(IOssService ossService) {
        this.ossService = ossService;
    }
}
