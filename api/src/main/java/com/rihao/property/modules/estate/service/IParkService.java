package com.rihao.property.modules.estate.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.estate.controller.params.ParkQueryParam;
import com.rihao.property.modules.estate.entity.Park;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.modules.estate.vo.ParkCreateVo;
import com.rihao.property.modules.estate.vo.ParkUpdateVo;
import com.rihao.property.modules.estate.vo.ParkVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-07-05
 */
public interface IParkService extends IService<Park> {

    PageVo<ParkVo> search(ParkQueryParam parkQueryParam);

    ParkVo detail(Long id);

    boolean update(ParkUpdateVo parkUpdateVo);

    boolean createNew(ParkCreateVo parkCreateVo);

    boolean delete(Long id);

    List<KeyValueVo> getByEstId(Long establishId);

    Park getByName(String parkName);

    void uploadContractTemp(Long id, MultipartFile file) throws IOException;

    ParkVo panorama(Long id);

    void countParkService(Long id);

    boolean importBuilding(List[] results);

    List<KeyValueVo> getPermissionList();

    String getByBuildingId(Long buildingId);
}
