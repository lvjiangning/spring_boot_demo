package com.rihao.property.modules.estate.vo;

import com.rihao.property.common.vo.KeyValueVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @date 2021-06-09
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SplitMergeHistoryVo implements Serializable {

    private Long id;
    private String type;
    private String before;
    private String after;
    private String user;
    private LocalDateTime time;
    private List<KeyValueVo> beforeList;
    private List<KeyValueVo> afterList;
}