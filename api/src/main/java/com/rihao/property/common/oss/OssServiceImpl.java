package com.rihao.property.common.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PolicyConditions;
import com.rihao.property.modules.common.vo.AliyunOssStsVo;
import com.rihao.property.modules.common.vo.UploadResultVo;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Service
public class OssServiceImpl implements IOssService {

    @Override
    public UploadResultVo uploadFile(String name, MultipartFile file) throws IOException {
        OSSClient ossClient =
                new OSSClient("oss-cn-shenzhen.aliyuncs.com", "LTAI0zVbhlQjiTDZ", "KA3aDaSm8B9VP7Wg3F7o5p45gIhsne");
        String fileName = name + "." + Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        ossClient.putObject("kcw-property", fileName, file.getInputStream());
        UploadResultVo uploadResultVo = new UploadResultVo();
        uploadResultVo.setUrl("https://kcw-property.oss-cn-shenzhen.aliyuncs.com/" + fileName);
        uploadResultVo.setName(file.getOriginalFilename());
        return uploadResultVo;
    }

    @Override
    public OSSObject getFile(String name) {
        OSSClient ossClient =
                new OSSClient("oss-cn-shenzhen.aliyuncs.com", "LTAI0zVbhlQjiTDZ", "KA3aDaSm8B9VP7Wg3F7o5p45gIhsne");
        return ossClient.getObject("kcw-property", name);
    }

    @Override
    public AliyunOssStsVo getAliyunOssSts(String dir) {
        PolicyConditions policyConditions = new PolicyConditions();
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 1, 10 * 1024 * 1024);
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
        Date now = new Date();
        int expireTime = 3600 * 24 * 3;
        Date expiration = DateUtils.addSeconds(now, expireTime);
        OSSClient ossClient =
                new OSSClient("oss-cn-shenzhen.aliyuncs.com", "LTAI0zVbhlQjiTDZ", "KA3aDaSm8B9VP7Wg3F7o5p45gIhsne");
        String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        return new AliyunOssStsVo()
                .setAccessId("LTAI0zVbhlQjiTDZ")
                .setDir(dir)
                // .setExpire(expiration.getTime())
                .setHost("https://kcw-property.oss-cn-shenzhen.aliyuncs.com")
                .setPolicy(encodedPolicy)
                .setSignature(postSignature);
    }
}
