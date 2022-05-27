package com.rihao.property.modules.config.service;

import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.modules.common.vo.UploadResultVo;
import com.rihao.property.modules.config.entity.SysConfig;
import com.rihao.property.modules.config.entity.SysFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 文件上传接口
 */
public interface ISysFileService extends IService<SysFile> {
     List<SysFile> upload(MultipartFile file, HttpServletRequest request,Long businessId,Boolean isOnly,Long type) throws IOException;

     String viewFile(SysFile sysFile, String suffix, boolean needSwf, HttpServletRequest request, HttpServletResponse response);

     void showImage(SysFile sysFile, HttpServletRequest request, HttpServletResponse response) throws IOException;

     void getSwfFile(SysFile sysFile, HttpServletResponse response) throws Exception;

     List<SysFile> getFilesByBusIdAndType(Long busId,Long type);

     Boolean updateFileForBusId(List<Long> fileIds,Long busId,Long type);

     Boolean deleteByBusinessId(List<Long> fileIds,Long busId,Long type);
}
