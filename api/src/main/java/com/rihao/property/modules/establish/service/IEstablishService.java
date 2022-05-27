package com.rihao.property.modules.establish.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.establish.controller.params.EstablishQueryParam;
import com.rihao.property.modules.establish.entity.Establish;
import com.rihao.property.modules.establish.vo.EstablishVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 单位信息表 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-23
 */
public interface IEstablishService extends IService<Establish> {

    PageVo<Establish> search(EstablishQueryParam establishQueryParam);

    Boolean createNew(EstablishVo createVo);

    Boolean update(EstablishVo updateVo);

    Boolean delete(Long id);

    Establish detail(Long id);

    List<Establish> search();

    Establish getByName(String estname);
}
