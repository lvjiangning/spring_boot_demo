package com.rihao.property.common.oss;

import com.aliyun.oss.model.OSSObject;
import com.rihao.property.modules.common.vo.AliyunOssStsVo;
import com.rihao.property.modules.common.vo.UploadResultVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IOssService {

    UploadResultVo uploadFile(String name, MultipartFile file) throws IOException;

    OSSObject getFile(String name);

    AliyunOssStsVo getAliyunOssSts(String dir);
}
