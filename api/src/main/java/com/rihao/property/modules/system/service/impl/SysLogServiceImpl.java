package com.rihao.property.modules.system.service.impl;

import com.anteng.boot.pojo.query.Filter;
import com.anteng.boot.web.bind.request.BufferRequest;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.LogQueryParam;
import com.rihao.property.modules.system.entity.SysLog;
import com.rihao.property.modules.system.mapper.SysLogMapper;
import com.rihao.property.modules.system.service.ISysLogService;
import com.rihao.property.modules.system.vo.SysLogVo;
import com.rihao.property.util.ClientInfoUtil;
import com.rihao.property.util.HttpServletRequestUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    /**
     * 默认忽略参数
     */
    private static final String[] ignoreParameters = new String[]{"password", "oldPassword", "newPassword"};

    @Override
    public PageVo<SysLogVo> search(LogQueryParam queryParam) {
        Page<SysLog> page = new Page<>(queryParam.getCurrent(), queryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("l.create_time")
        ));
        SysLogMapper.QueryParam params = new SysLogMapper.QueryParam()
                .setOperStartTime(queryParam.getOperStartTime())
                .setOperEndTime(queryParam.getOperEndTime());
        if (StringUtils.hasText(queryParam.getOperName())) {
            params.setOperName(Filter.LikeValue.both(queryParam.getOperName()));
        }
        if (StringUtils.hasText(queryParam.getDescription())) {
            params.setDescription(Filter.LikeValue.both(queryParam.getDescription()));
        }
        Page<SysLogVo> result = this.getBaseMapper().selectByQueryParam(page, params);

        return PageVo.create(queryParam.getCurrent(), queryParam.getPageSize(),
                result.getTotal(), result.getRecords());
    }

    @Override
    public void saveLog(SysLog log) {
        assert log != null;

        HttpServletRequest request = HttpServletRequestUtil.getRequest();

        log.setUrl(request.getRequestURI());
        log.setMethod(request.getMethod().toUpperCase());


        StringBuilder parameter = new StringBuilder();
        Map<String, String[]> parameterMap = request.getParameterMap();

        if (parameterMap != null) {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String parameterName = entry.getKey();
                if (ArrayUtils.contains(ignoreParameters, parameterName)) {
                    continue;
                }
                String[] parameterValues = entry.getValue();
                if (parameterValues == null) {
                    continue;
                }
                for (String parameterValue : parameterValues) {
                    // 过滤base64图片
                    if (parameterValue.startsWith("data:image/")) {
                        parameterValue = "data:image/";
                    }
                    parameter.append(parameterName).append("=").append(parameterValue).append(",");
                }
            }
        }
        if (parameter.length() > 0) {
            log.setParams(parameter.toString());
        } else if (request instanceof BufferRequest){
            BufferRequest bufferRequest = (BufferRequest) request;
            try {
                String requestBody = bufferRequest.getRequestBody();
                log.setParams(requestBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.setRequestIp(ClientInfoUtil.getIp(request));

        this.save(log);
    }
}
