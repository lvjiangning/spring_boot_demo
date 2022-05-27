package com.rihao.property.modules.estate.service.impl;

import com.anteng.boot.pojo.query.Filter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rihao.property.modules.estate.entity.SplitMergeHistory;
import com.rihao.property.modules.estate.mapper.SplitMergeHistoryMapper;
import com.rihao.property.modules.estate.service.ISplitMergeHistoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-06-09
 */
@Service
public class SplitMergeHistoryServiceImpl extends ServiceImpl<SplitMergeHistoryMapper, SplitMergeHistory> implements ISplitMergeHistoryService {

    @Override
    public List<SplitMergeHistory> getMergeHistory(Long id) {
        List<SplitMergeHistory> mergeHistories = new ArrayList<>();
        QueryWrapper<SplitMergeHistory> wrapper = new QueryWrapper();
        wrapper.lambda()
                .like(SplitMergeHistory::getResultUnitIds, Filter.LikeValue.both(id.toString()))
                .or().like(SplitMergeHistory::getOrginalUnitIds, Filter.LikeValue.both(id.toString()));
        return this.list(wrapper);
    }
}
