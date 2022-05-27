package com.rihao.property.modules.notice.controller;


import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.notice.controller.params.NoticeQueryParam;
import com.rihao.property.modules.notice.service.INoticeService;
import com.rihao.property.modules.notice.vo.NoticeVo;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-27
 */
@RestController
@RequestMapping("/api/notice")
public class NoticeController extends BaseController {

    private final INoticeService noticeService;

    public NoticeController(INoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Log("消息列表")
    @ApiOperation("消息列表")
    @GetMapping("page")
    public ResBody<?> search(NoticeQueryParam noticeQueryParam) {
        PageVo<NoticeVo> page = this.noticeService.search(noticeQueryParam);
        addLog("查看消息列表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("消息详情")
    @ApiOperation("消息详情")
    @GetMapping("/{id}")
    public ResBody<?> detail(@PathVariable Long id) {
        NoticeVo noticeVo = this.noticeService.detail(id);
        addLog("查看消息详情-" + id, JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(noticeVo);
    }
}

