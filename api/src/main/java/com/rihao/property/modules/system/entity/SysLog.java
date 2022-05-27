package com.rihao.property.modules.system.entity;

import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_sys_log")
public class SysLog extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 日志类型 INFO ERROR
     */
    @TableField("type")
    private String type;

    /**
     * 请求链接
     */
    @TableField("url")
    private String url;

    /**
     * 请求方法
     */
    @TableField("method")
    private String method;

    /**
     * 请求参数
     */
    @TableField("params")
    private String params;

    /**
     * 请求ip
     */
    @TableField("request_ip")
    private String requestIp;

    /**
     * 请求耗时
     */
    @TableField("time")
    private Long time;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 操作内容
     */
    @TableField("content")
    private String content;

    /**
     * 异常详细
     */
    @TableField("exception_detail")
    private String exceptionDetail;

    public SysLog(String type, Long time) {
        this.type = type;
        this.time = time;
    }
}
