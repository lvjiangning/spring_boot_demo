package com.rihao.property.modules.ent.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.rihao.property.modules.common.vo.AliyunOssStsVo;
import com.rihao.property.modules.ent.controller.params.AliyunOssProperties;
import com.rihao.property.modules.ent.service.IAliyunOssService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class AliyunOssServiceImpl implements InitializingBean, DisposableBean, IAliyunOssService {

    private final AliyunOssProperties ossProperties;
    private OSSClient ossClient;

    public AliyunOssStsVo getAliyunOssSts(String dir) {
        PolicyConditions policyConditions = new PolicyConditions();
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 1, 10 * 1024 * 1024);
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
        Date now = new Date();
        Date expiration = DateUtils.addSeconds(now, ossProperties.getExpireTime());
        String postPolicy = this.ossClient.generatePostPolicy(expiration, policyConditions);
        try {
            byte[] binaryData = postPolicy.getBytes("UTF-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            return new AliyunOssStsVo()
                    .setAccessId(ossProperties.getAccessKeyId())
                    .setDir(dir)
                    // .setExpire(expiration.getTime())
                    .setHost("https://" + ossProperties.getBucketHost())
                    .setPolicy(encodedPolicy)
                    .setSignature(postSignature);
        } catch (UnsupportedEncodingException e) {
            throw new DataIntegrityViolationException("生成OssSts错误");
        }
    }

    public AliyunOssServiceImpl(AliyunOssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (ossProperties.isValid()) {
            this.ossClient = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getSecret());
        }
    }

    @Override
    public void destroy() throws Exception {
        if (this.ossClient != null) {
            this.ossClient.shutdown();
        }
    }
}
