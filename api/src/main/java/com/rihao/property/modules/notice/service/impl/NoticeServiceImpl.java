package com.rihao.property.modules.notice.service.impl;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.notice.controller.params.NoticeQueryParam;
import com.rihao.property.modules.notice.convert.NoticeConvert;
import com.rihao.property.modules.notice.entity.Notice;
import com.rihao.property.modules.notice.mapper.NoticeMapper;
import com.rihao.property.modules.notice.service.INoticeService;
import com.rihao.property.modules.notice.vo.NoticeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-27
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Override
    public PageVo<NoticeVo> search(NoticeQueryParam noticeQueryParam) {
        Page<Notice> page = new Page<>(noticeQueryParam.getCurrent(), noticeQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        Page<Notice> noticePage = this.page(page, wrapper);

        return PageVo.create(
                noticeQueryParam.getCurrent(),
                noticeQueryParam.getPageSize(),
                noticePage.getTotal(),
                NoticeConvert.INSTANCE.entity2ListItemBatch(noticePage.getRecords())
        );
    }

    @Override
    public NoticeVo detail(Long id) {
        return NoticeConvert.INSTANCE.entity2Vo(this.getById(id));
    }
}
