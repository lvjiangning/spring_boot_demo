package com.rihao.property.modules.lease.contract.vo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class UploadOaFileVo implements Serializable {
    private MultipartFile oaFile;
    private Long id;
    private String reason;
}
