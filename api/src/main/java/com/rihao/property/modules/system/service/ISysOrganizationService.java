package com.rihao.property.modules.system.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.OrganizationQueryParam;
import com.rihao.property.modules.system.entity.SysOrganization;
import com.rihao.property.modules.system.vo.SysOrganizationCreateVo;
import com.rihao.property.modules.system.vo.SysOrganizationListVo;
import com.rihao.property.modules.system.vo.SysOrganizationVo;
import com.rihao.property.modules.system.vo.TreeOrganizationVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 单位表 服务类
 * </p>
 *
 * @author ken
 * @since 2020-05-18
 */
public interface ISysOrganizationService extends IService<SysOrganization> {

    PageVo<SysOrganizationListVo> search(OrganizationQueryParam queryParam);

    Boolean createNew(SysOrganizationCreateVo createVo);

    boolean update(SysOrganizationVo updateVo);

    boolean delete(Long id);

    SysOrganizationVo detail(Long id);

    /**
     * 获取所有资源的树形结构
     */
    List<TreeOrganizationVo> getAllOrganizationTree();

    /**
     * 获取当前用户所有资源的树形结构
     * @return
     */
    List<TreeOrganizationVo> getCurrentUserAllOrganizationTree();

    /**
     * 获取所有子级单位id（包含自身id）
     * @param id
     * @return
     */
    List<Long> getOrganizationSubIds(Long id);

    /**
     * 导入
     * @param file
     */
    void importOrganization(MultipartFile file, HttpServletRequest request, HttpServletResponse response);
}
