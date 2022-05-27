package com.rihao.property.common.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author gaoy
 * 2020/2/28/028
 */
@ApiModel
public class PageVo<T> {
    @JsonProperty("data")
    private List<T> content;
    private Pagination pagination;

    public PageVo() {
    }

    public static <T> PageVo<T> create(Integer page, Integer size, long total, List<T> content) {
        PageVo<T> pageVo = new PageVo<>();
        pageVo.pagination = new Pagination();
        pageVo.pagination.current = page;
        pageVo.pagination.pageSize = size;
        pageVo.pagination.total = total;
        pageVo.content = content;
        return pageVo;
    }
    @ApiModelProperty("总记录数")
    public long getTotal() {
        return this.pagination.total;
    }
    @ApiModelProperty("当前页")
    public Integer getCurrent() {
        return this.pagination.current;
    }
    @ApiModelProperty("每页记录数")
    public Integer getPageSize() {
        return this.pagination.pageSize;
    }

    @ApiModelProperty("总页数")
    public int getTotalPages() {
        return (int) Math.ceil((double) getTotal() / (double) getPageSize());
    }

    @ApiModelProperty("记录")
    public List<T> getContent() {
        return content;
    }

    @Data
    private static class Pagination {
        private long total;
        private Integer current;
        private Integer pageSize;


    }
}
