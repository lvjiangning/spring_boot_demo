package com.rihao.property.common.page;

import com.rihao.property.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gaoy
 * 2020/2/28/028
 */
@Data
@ApiModel("查询参数对象")
public class PageParams {
    @ApiModelProperty(value = "页码,默认为1", example = "1")
    private Integer current = CommonConstant.DEFAULT_PAGE_INDEX;
    @ApiModelProperty(value = "页大小,默认为10", example = "10")
    private Integer pageSize = CommonConstant.DEFAULT_PAGE_SIZE;

    public void setCurrent(Integer current) {
        if (current == null || current <= 0) {
            this.current = CommonConstant.DEFAULT_PAGE_INDEX;
        } else {
            this.current = current;
        }
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null) {
            this.pageSize = CommonConstant.DEFAULT_PAGE_SIZE;
        } else {
            this.pageSize = pageSize;
        }
    }
}
