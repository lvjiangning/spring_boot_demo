package com.rihao.property.modules.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 行政区
 * </p>
 *
 * @author ken
 * @since 2020-05-19
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("tb_district")
public class District implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 行政区等级 1:省 2:市 3:区
     */
    @TableField("level")
    private Integer level;

    /**
     * 行政区全称
     */
    @TableField("full_name")
    private String fullName;

    /**
     * 经度
     */
    @TableField("lat")
    private BigDecimal lat;

    /**
     * 经度
     */
    @TableField("lng")
    private BigDecimal lng;

    /**
     * 行政区简称
     */
    @TableField("name")
    private String name;

    /**
     * 组合全称
     */
    @TableField("merge_name")
    private String mergeName;

    /**
     * 父级行政区编码
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 父级行政区路径
     */
    @TableField("parent_ids")
    private String parentIds;

    /**
     * 拼音
     */
    @TableField("pinyin")
    private String pinyin;


}
