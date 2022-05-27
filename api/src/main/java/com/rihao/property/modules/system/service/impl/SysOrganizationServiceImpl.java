package com.rihao.property.modules.system.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.alibaba.fastjson.JSON;
import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.pojo.query.Filter;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.common.vo.PermissionControlVo;
import com.rihao.property.modules.system.component.SysOrganizationImportVerifyHandler;
import com.rihao.property.modules.system.controller.params.OrganizationQueryParam;
import com.rihao.property.modules.system.convert.SysOrganizationConvert;
import com.rihao.property.modules.system.entity.SysDict;
import com.rihao.property.modules.system.entity.SysOrganization;
import com.rihao.property.modules.system.mapper.SysDictMapper;
import com.rihao.property.modules.system.mapper.SysOrganizationMapper;
import com.rihao.property.modules.system.service.ISysOrganizationService;
import com.rihao.property.modules.system.service.ISysUserOrganizationService;
import com.rihao.property.modules.system.utils.TreeOrganizationUtil;
import com.rihao.property.modules.system.vo.*;
import com.rihao.property.util.FileUtil;
import com.rihao.property.util.ValidationUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 单位表 服务实现类
 * </p>
 *
 * @author ken
 * @since 2020-05-18
 */
@Service
public class SysOrganizationServiceImpl extends ServiceImpl<SysOrganizationMapper, SysOrganization> implements ISysOrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(SysOrganizationServiceImpl.class);
    private ISysUserOrganizationService userOrganizationService;
    private SysOrganizationImportVerifyHandler organizationImportVerifyHandler;
    private SysDictMapper dictMapper;

    @Override
    public PageVo<SysOrganizationListVo> search(OrganizationQueryParam queryParam) {
        Page<SysOrganization> page = new Page<>(queryParam.getCurrent(), queryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("o.create_time")
        ));
        SysOrganizationMapper.QueryParam params = new SysOrganizationMapper.QueryParam();
        if (StringUtils.hasText(queryParam.getName())) {
            params.setName(Filter.LikeValue.both(queryParam.getName()));
        }
        if (StringUtils.hasText(queryParam.getCode())) {
            params.setCode(Filter.LikeValue.both(queryParam.getCode()));
        }
        Page<SysOrganizationListVo> result = this.getBaseMapper().selectByQueryParam(page, params);

        return PageVo.create(queryParam.getCurrent(), queryParam.getPageSize(),
                result.getTotal(), result.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createNew(SysOrganizationCreateVo createVo) {
        this.validNameUnique(createVo.getName(), null);

        SysOrganization entity = SysOrganizationConvert.INSTANCE.createParam2Entity(createVo);
        if (createVo.getParentId() != null && createVo.getParentId() != 0) {
            SysOrganization parent = this.getById(createVo.getParentId());
            entity.setParentIds(parent.makeSelfAsParentsIds());
        } else {
            entity.setParentIds("0/");
            entity.setParentId(0L);
        }

        /**
         * 这里要用封装类型
         * 使用Aop和Async不然会抛出异常
         * Null return value from advice does not match primitive return type
         */
        Boolean result = this.save(entity);

        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SysOrganizationVo updateVo) {
        this.validNameUnique(updateVo.getName(), updateVo.getId());
        SysOrganization entity = SysOrganizationConvert.INSTANCE.updateParam2Entity(updateVo);
        Boolean result = this.updateById(entity);

        return result;
    }

    @Override
    public boolean delete(Long id) {
        SysOrganization organization = this.getById(id);
        if (organization == null) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.DATE_NULL).message("记录不存在"));
        }

        List<SysOrganization> childOrganizations = this.list(new QueryWrapper<SysOrganization>().lambda().eq(SysOrganization::getParentId, organization.getId()));
        if (!CollectionUtils.isEmpty(childOrganizations)) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("存在下级单位，不允许删除"));
        }

        return this.removeById(id);
    }

    @Override
    public SysOrganizationVo detail(Long id) {
        return SysOrganizationConvert.INSTANCE.entity2Vo(this.getById(id));
    }

    @Override
    public List<TreeOrganizationVo> getAllOrganizationTree() {
        //获取启用的资源列表
        List<SysOrganization> allOrganization = this.list();

        //转换
        return TreeOrganizationUtil.convertToTreeOrganization(allOrganization, false);
    }

    @Override
    public List<TreeOrganizationVo> getCurrentUserAllOrganizationTree() {
        PermissionControlVo permissionControlVo = this.userOrganizationService.getCurrentUserOrganizationIds();

        if (permissionControlVo.getOrganizationIds() == null
                || permissionControlVo.getOrganizationIds().length == 0) {
            return Lists.newArrayList();
        }

        Set<Long> ids = Arrays.stream(permissionControlVo.getOrganizationIds()).boxed().collect(Collectors.toSet());
        //获取启用的资源列表
        List<SysOrganization> allOrganization = this.list();
        //转换
        return TreeOrganizationUtil.convertToTreeOrganization(allOrganization, false, ids);
    }

    @Override
    public List<Long> getOrganizationSubIds(Long id) {
        List<Long> organizationSubIds = new ArrayList<>();
        organizationSubIds.add(id);
        List<SysOrganization> organizationSubs = this.getBaseMapper().selectList(
                new QueryWrapper<SysOrganization>().lambda().eq(SysOrganization::getParentId, id));
        if (!CollectionUtils.isEmpty(organizationSubs)) {
            organizationSubIds.addAll(organizationSubs.stream().mapToLong(SysOrganization::getId).boxed().collect(Collectors.toList()));
        }
        return organizationSubIds;
    }

    /**
     * 校验名称唯一
     *
     * @param name 名称
     * @param id   不为空表示要排除id本省的记录
     */
    private void validNameUnique(String name, Long id) {
        QueryWrapper<SysOrganization> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysOrganization::getName, name);
        if (id != null && id != 0) {
            wrapper.lambda().ne(SysOrganization::getId, id);
        }
        SysOrganization entity = this.getOne(wrapper);
        ValidationUtil.notNull(entity, "组织单位", "名称", name);
    }

    @Override
    public void importOrganization(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            ImportParams params = new ImportParams();
            params.setNeedVerify(true);
            params.setNeedSave(true);
            params.setVerifyHandler(organizationImportVerifyHandler);
            ExcelImportResult<SysOrganizationImportVo> result = ExcelImportUtil.importExcelMore(file.getInputStream(), SysOrganizationImportVo.class, params);

            this.saveImportOrganization(result.getList());

            //result.getWorkbook().write(fos);
            if (!CollectionUtils.isEmpty(result.getFailList())) {
                String fileName = FileUtil.processFileName("导入错误提示.xls", request);

                //设置请求返回类型
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                OutputStream os = response.getOutputStream();
                result.getFailWorkbook().write(os);
                os.flush();
                os.close();
            } else {
                //设置请求返回类型
                response.setContentType("application/json");
                OutputStream os = response.getOutputStream();
                ResBody resBody = ResBody.success();
                String res = JSON.toJSONString(resBody);
                os.write(res.getBytes("UTF-8"));
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void saveImportOrganization(List<SysOrganizationImportVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<SysOrganization> organizations = this.list(null);
        Map<String, Long> organizationMap = organizations.stream().collect(Collectors.toMap(SysOrganization::getName, SysOrganization::getId, (key1, key2) -> key2));

        List<SysDict> types = this.dictMapper.selectByParentCode("organizationType");
        Map<String, Long> typeMap = new HashMap<>(types.size());
        if (!CollectionUtils.isEmpty(types)) {
            typeMap = types.stream().collect(Collectors.toMap(SysDict::getValue, SysDict::getId, (key1, key2) -> key2));
        }

        List<SysDict> levels = this.dictMapper.selectByParentCode("organizationLevel");
        Map<String, Long> levelMap = new HashMap<>(levels.size());
        if (!CollectionUtils.isEmpty(levels)) {
            levelMap = levels.stream().collect(Collectors.toMap(SysDict::getValue, SysDict::getId, (key1, key2) -> key2));
        }

        for (SysOrganizationImportVo vo : list) {
            SysOrganizationCreateVo createVo = new SysOrganizationCreateVo();
            BeanUtils.copyProperties(vo, createVo);
            if (!StringUtils.isEmpty(vo.getParentName())) {
                createVo.setParentId(organizationMap.get(vo.getParentName()));
            }
            createVo.setType(typeMap.get(vo.getType()));
            createVo.setLevel(levelMap.get(vo.getLevel()));

            this.createNew(createVo);
        }

    }

    @Autowired
    public void setUserOrganizationService(ISysUserOrganizationService userOrganizationService) {
        this.userOrganizationService = userOrganizationService;
    }

    @Autowired
    public void setOrganizationImportVerifyHandler(SysOrganizationImportVerifyHandler organizationImportVerifyHandler) {
        this.organizationImportVerifyHandler = organizationImportVerifyHandler;
    }

    @Autowired
    public void setDictMapper(SysDictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }
}
