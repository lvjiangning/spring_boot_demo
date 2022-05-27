package com.rihao.property.modules.notice.convert;

import com.rihao.property.modules.notice.entity.Notice;
import com.rihao.property.modules.notice.vo.NoticeVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-27
 * @description
 */
@Mapper
public interface NoticeConvert {

    NoticeConvert INSTANCE = Mappers.getMapper(NoticeConvert.class);

    List<NoticeVo> entity2ListItemBatch(List<Notice> list);

    NoticeVo entity2Vo(Notice byId);
}