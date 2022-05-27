package com.rihao.property.modules.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.modules.config.entity.ListInfo;
import com.rihao.property.modules.config.entity.SysConfig;
import com.rihao.property.modules.config.vo.ListInfoVo;
import com.rihao.property.modules.config.vo.SysConfigVo;

/**
 * <p>
 * 系统参数设置 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-21
 */
public interface IListInfoService extends IService<ListInfo> {

    String getListInfo(ListInfoVo listInfoVo);

    String save(ListInfoVo listInfoVo);
}
