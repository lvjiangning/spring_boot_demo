package com.rihao.property.modules.config.entity;

import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统参数设置
 * </p>
 *
 * @author wangyu
 * @since 2021-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_sys_config")
public class SysConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 出租方
     */
    @TableField("lessor")
    private String lessor;

    /**
     * 出租方地址
     */
    @TableField("lessor_address")
    private String lessorAddress;

    /**
     * 合同-区
     */
    @TableField("region")
    private String region;

    /**
     * 合同-路
     */
    @TableField("road")
    private String road;

    /**
     * 合同-详细地址
     */
    @TableField("road_detail")
    private String roadDetail;

    /**
     * 权利人名称
     */
    @TableField("place_holder")
    private String placeHolder;

    /**
     * 每月交租日期
     */
    @TableField("payment_date")
    private String paymentDate;

    /**
     * 拖欠月租警告月数
     */
    @TableField("arrears_month")
    private String arrearsMonth;

    /**
     * 拖欠月租警告天数
     */
    @TableField("arrears_day")
    private String arrearsDay;

    /**
     * 甲方送达地址
     */
    @TableField("delivery_address")
    private String deliveryAddress;

    /**
     * 单位ID
     */
    @TableField("establish_id")
    private Long establishId;

    /**
     * 合同到期预警时间（月）
     */
    @TableField("contact_expiration")
    private Integer contactExpiration;
}
