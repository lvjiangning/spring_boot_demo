package com.rihao.property.modules.estate.service;

import com.rihao.property.modules.estate.entity.SplitMergeHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-06-09
 */
public interface ISplitMergeHistoryService extends IService<SplitMergeHistory> {

    List<SplitMergeHistory> getMergeHistory(Long id);
}
