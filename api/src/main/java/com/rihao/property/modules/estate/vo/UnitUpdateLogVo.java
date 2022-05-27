package com.rihao.property.modules.estate.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @description
 * @since 2021-03-28
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UnitUpdateLogVo implements Serializable {




    private Long id;

    private String updateBeforeText;

    private String updateAfterText;

    private LocalDateTime modifyTime;

    private String modifyBy;

    private Integer sort;

}