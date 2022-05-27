package com.rihao.property.modules.system.service;

import com.rihao.property.modules.system.entity.SysResource;
import com.rihao.property.modules.system.vo.TreeResourceVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
public interface ISysResourceService extends IService<SysResource> {

    /**
     * 获取所有资源的树形结构
     */
    List<TreeResourceVo> getAllResourceTree();

    /**
     * 根据用户名得到菜单
     * @param username
     * @return
     */
    List<TreeResourceVo> getAllMenuTree(String username);

    /**
     * 根据用户名获取所有资源（包含父节点）
     * @param username
     * @return
     */
    List<SysResource> getSysResourcesByUsername(String username);

    /**
     * 获取所有资源列表
     * @return
     */
    List<SysResource> getAllResource();
}
