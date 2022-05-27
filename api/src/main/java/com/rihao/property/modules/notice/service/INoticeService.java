package com.rihao.property.modules.notice.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.notice.controller.params.NoticeQueryParam;
import com.rihao.property.modules.notice.entity.Notice;
import com.rihao.property.modules.notice.vo.NoticeVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-27
 */
public interface INoticeService extends IService<Notice> {

    PageVo<NoticeVo> search(NoticeQueryParam noticeQueryParam);

    NoticeVo detail(Long id);
}
