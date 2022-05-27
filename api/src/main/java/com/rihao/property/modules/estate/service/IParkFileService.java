package com.rihao.property.modules.estate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.estate.controller.params.ParkFileQueryParam;
import com.rihao.property.modules.estate.entity.ParkFile;
import com.rihao.property.modules.estate.vo.ParkFileCreateVo;
import com.rihao.property.modules.estate.vo.ParkFileVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-06-11
 */
public interface IParkFileService extends IService<ParkFile> {

    PageVo<ParkFileVo> search(ParkFileQueryParam parkFileQueryParam);

    boolean createNew(ParkFileCreateVo parkFileCreateVo);

    boolean delete(Long id);

    List<ParkFileVo> getByParkId(Long id);
}
