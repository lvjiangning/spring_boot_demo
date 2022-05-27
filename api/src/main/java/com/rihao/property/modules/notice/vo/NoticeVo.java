package com.rihao.property.modules.notice.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @since 2021-03-27
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class NoticeVo implements Serializable {

    private Long id;
    private int type;
    private String title;
    private String content;
    private String createTime;
}