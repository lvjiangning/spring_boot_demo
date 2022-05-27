package com.rihao.property.modules.ent.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.pojo.query.Filter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.rihao.property.common.page.PageParams;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.config.emuns.SysFileType;
import com.rihao.property.modules.config.entity.SysFile;
import com.rihao.property.modules.config.service.ISysFileService;
import com.rihao.property.modules.ent.controller.params.*;
import com.rihao.property.modules.ent.convert.EntChangeHistoryDetailConvert;
import com.rihao.property.modules.ent.convert.EntConvert;
import com.rihao.property.modules.ent.convert.EntPartnerConvert;
import com.rihao.property.modules.ent.entity.*;
import com.rihao.property.modules.ent.enums.NewSettleStatus;
import com.rihao.property.modules.ent.mapper.EntMapper;
import com.rihao.property.modules.ent.service.*;
import com.rihao.property.modules.ent.vo.*;
import com.rihao.property.modules.estate.vo.InspectionFileCreateVo;
import com.rihao.property.modules.inspection.entity.InspectionFile;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.service.IContractService;
import com.rihao.property.modules.lease.contract.vo.ContractVo;
import com.rihao.property.modules.report.service.IReportService;
import com.rihao.property.modules.report.service.impl.ReportServiceImpl;
import com.rihao.property.modules.report.vo.UseHouseLedgerVo;
import com.rihao.property.shiro.util.JwtUtil;
import com.rihao.property.util.ValidationUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-26
 */
@Service
public class EntServiceImpl extends ServiceImpl<EntMapper, Ent> implements IEntService {

    @Autowired
    private IEntPartnerService entPartnerService;
    @Autowired
    private IEntUsedNameService entUsedNameService;
    @Autowired
    private IEntChangeHistoryService entChangeHistoryService;
    @Autowired
    private IEntChangeHistoryDetailService entChangeHistoryDetailService;
    @Autowired
    private IEntMattersService mattersService;
    @Autowired
    private IEntCategoryService categoryService;
    @Autowired
    private IContractService contractService;
    @Autowired
    private IEntMattersService entMattersService;
    @Autowired
    private ISysFileService sysFileService;
    @Override
    public PageVo<EntVo> search(EntQueryParam entQueryParam) {
        Page<Ent> page = new Page<>(entQueryParam.getCurrent(), entQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        EntMapper.QueryParam params = new EntMapper.QueryParam();
        params.setParkIds(JwtUtil.getCurrentUserParkIds());
        if (entQueryParam.getName() != null) {
            params.setName(Filter.LikeValue.both(entQueryParam.getName()));
        }
        if (entQueryParam.getLegal() != null) {
            params.setLegal(Filter.LikeValue.both(entQueryParam.getLegal()));
        }
        if (entQueryParam.getLegalPhoneNumber() != null) {
            params.setLegalPhoneNumber(Filter.LikeValue.both(entQueryParam.getLegalPhoneNumber()));
        }
        if (entQueryParam.getUnifiedSocialCreditCode() != null) {
            params.setUnifiedSocialCreditCode(Filter.LikeValue.both(entQueryParam.getUnifiedSocialCreditCode()));
        }
        params.setLikeParam(entQueryParam.getLikeParam());
        Page<EntVo> entPage = this.getBaseMapper().selectByQueryParam(page, params);

        return PageVo.create(
                entQueryParam.getCurrent(), entQueryParam.getPageSize(),
                entPage.getTotal(), entPage.getRecords()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Ent createNew(EntCreateVo entCreateVo) {
        this.validNameUnique(entCreateVo.getName());
        this.validUniCodeUnique(entCreateVo.getUnifiedSocialCreditCode());

        Ent entity = EntConvert.INSTANCE.createParam2Entity(entCreateVo);
        if (ArrayUtils.isNotEmpty(entCreateVo.getCategorys())) {
            entity.setCategoryIds(String.join(",", entCreateVo.getCategorys()));
        }
        entity.setName(entCreateVo.getName());
        this.save(entity);
        if (!CollectionUtils.isEmpty(entCreateVo.getPartners())) {
            for (PartnerVo partnerVo : entCreateVo.getPartners()) {
                if (StringUtils.hasText(partnerVo.getName())
                        && partnerVo.getProportion() != null) {
                    EntPartner entPartner = EntPartnerConvert.INSTANCE.createParam2Entity(partnerVo);
                    entPartner.setEntId(entity.getId());
                    this.entPartnerService.save(entPartner);
                }
            }
        }

        return entity;
    }

    @Override
    public EntVo detail(String id) {
        Ent ent = this.getById(id);
        EntVo entVo = EntConvert.INSTANCE.entity2Vo(ent);
        if (ent.getCategoryIds() != null) {
            String[] split = ent.getCategoryIds().split(",");
            StringBuilder strBuilder=new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                strBuilder.append(this.categoryService.getById(split[i]).getName()).append(",");
            }
            String substring = strBuilder.substring(0, strBuilder.length() - 1);
            entVo.setCategoryNames(substring);

        }
        List<EntPartner> partners = this.entPartnerService.search(entVo.getId());
        entVo.setPartners(partners);
        List<ContractVo> contracts = this.contractService.getByListEntId(ent.getId());
        entVo.setContracts(contracts);
        //企业事项列表
        List<EntMatters> entMatters = this.entMattersService.getByEntId(entVo.getId());
        entVo.setMatters(entMatters);
        for (EntMatters entMatters1:entMatters){
            entMatters1.setFile(sysFileService.getById(entMatters1.getFileId()));
        }

        //曾用名处理
        QueryWrapper<EntUsedName> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EntUsedName::getEntId, entVo.getId());
        List<EntUsedName> entUsedNamePage = this.entUsedNameService.list(wrapper);
        if (CollUtil.isNotEmpty(entUsedNamePage)){
           entVo.setUsedName(entUsedNamePage.stream().map(EntUsedName::getUsedName).collect(Collectors.joining(","))) ;
        }
        return entVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(EntUpdateVo entUpdateVo) {
        Ent entity = EntConvert.INSTANCE.updateParam2Entity(entUpdateVo);
        List<EntPartner> entPartners = this.entPartnerService.search(entUpdateVo.getId());

        Ent oldEntity = this.getById(entUpdateVo.getId());
        EntVo oldEntityVo = EntConvert.INSTANCE.entity2Vo(oldEntity);
        oldEntityVo.setPartners(entPartners);

        // 记录变更
        EntChangeHistory entChangeHistory = new EntChangeHistory();
        entChangeHistory.setEntId(entUpdateVo.getId());
        this.entChangeHistoryService.save(entChangeHistory);

        this.recordChangeDetail(oldEntity, entity, entChangeHistory.getId(), entUpdateVo.getPartners(), entUpdateVo.getCategorys());

        // 处理股东信息
        for (EntPartner entPartner : entPartners) {
            this.entPartnerService.removeById(entPartner.getId());
        }
        for (PartnerVo partnerVo : entUpdateVo.getPartners()) {
            EntPartner entPartner = EntPartnerConvert.INSTANCE.createParam2Entity(partnerVo);
            entPartner.setEntId(entity.getId());
            this.entPartnerService.save(entPartner);
        }
        entity.setCategoryIds(String.join(",", entUpdateVo.getCategorys()));
        //设置以前的NewSettle
        entity.setNewSettle(oldEntity.getNewSettle());

        //如果名字更改了，合同保存的名字也要改
        if (!org.apache.commons.lang3.StringUtils.equals(oldEntity.getName(),entity.getName())){
            List<Contract> contracts = this.contractService.getBaseMapper().selectList(new QueryWrapper<Contract>().lambda().eq(Contract::getEntId, entity.getId()));
            if (!CollectionUtils.isEmpty(contracts)){
                for (Contract contract:contracts) {
                    contract.setEntName(entity.getName());
                    this.contractService.getBaseMapper().updateById(contract);
                }
            }
        }

        return this.updateById(entity);
    }

    @Override
    public boolean delete(Long id) {
        List<ContractVo> contracts = this.contractService.getByListEntId(id);
        if (contracts.size() != 0) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("企业下存在合同，不允许删除"));
        }
        return this.removeById(id);
    }

    private void recordChangeDetail(Ent oldEntity, Ent entity, Long id, List<PartnerVo> partners, String[] categorys) {
        if (!org.apache.commons.lang3.StringUtils.equals(oldEntity.getMainIndustry(),entity.getMainIndustry()) ) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("主要从事行业类别");
            detail.setBeforeValue(oldEntity.getMainIndustry());
            detail.setAfterValue(entity.getMainIndustry());
            this.entChangeHistoryDetailService.save(detail);
        }

        if (!org.apache.commons.lang3.StringUtils.equals(oldEntity.getName(),entity.getName()) ) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("企业名称");
            detail.setBeforeValue(oldEntity.getName());
            detail.setAfterValue(entity.getName());
            this.entChangeHistoryDetailService.save(detail);
        }
        if (!org.apache.commons.lang3.StringUtils.equals(oldEntity.getRegisteredCapital(),entity.getRegisteredCapital()))  {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("单位注册资本(万元)");
            detail.setBeforeValue(oldEntity.getRegisteredCapital());
            detail.setAfterValue(entity.getRegisteredCapital());
            this.entChangeHistoryDetailService.save(detail);
        }
        if (!org.apache.commons.lang3.StringUtils.equals(oldEntity.getRegisteredArea(),entity.getRegisteredArea()) ) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("注册所在区");
            detail.setBeforeValue(oldEntity.getRegisteredArea());
            detail.setAfterValue(entity.getRegisteredArea());
            this.entChangeHistoryDetailService.save(detail);
        }
        if (!org.apache.commons.lang3.StringUtils.equals(oldEntity.getRegisteredStreet(),entity.getRegisteredStreet())) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("注册所在街道");
            detail.setBeforeValue(oldEntity.getRegisteredStreet());
            detail.setAfterValue(entity.getRegisteredStreet());
            this.entChangeHistoryDetailService.save(detail);
        }
        if (! org.apache.commons.lang3.StringUtils.equals(oldEntity.getUnifiedSocialCreditCode(),entity.getUnifiedSocialCreditCode()) ) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("信用代码");
            detail.setBeforeValue(oldEntity.getUnifiedSocialCreditCode());
            detail.setAfterValue(entity.getUnifiedSocialCreditCode());
            this.entChangeHistoryDetailService.save(detail);
        }
        if (!org.apache.commons.lang3.StringUtils.equals(oldEntity.getRegistrationType(),entity.getRegistrationType())) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("登记注册类型");
            detail.setBeforeValue(oldEntity.getRegistrationType());
            detail.setAfterValue(entity.getRegistrationType());
            this.entChangeHistoryDetailService.save(detail);
        }
        if (! org.apache.commons.lang3.StringUtils.equals(oldEntity.getRegistrationTime(),entity.getRegistrationTime())) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("注册时间");
            detail.setBeforeValue(oldEntity.getRegistrationTime());
            detail.setAfterValue(entity.getRegistrationTime());
            this.entChangeHistoryDetailService.save(detail);
        }
        String oldCategory = this.getCategoryByEnt(oldEntity);
        String newCategory = "";
        for (String category : categorys) {
            newCategory += this.categoryService.getById(Long.parseLong(category)).getName() + ",";
        }
        newCategory = newCategory.substring(0, newCategory.length() - 1);
        if (!org.apache.commons.lang3.StringUtils.equals(oldCategory,newCategory)) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("企业领域");
            detail.setBeforeValue(oldCategory);
            detail.setAfterValue(newCategory);
            this.entChangeHistoryDetailService.save(detail);
        }
        if (!org.apache.commons.lang3.StringUtils.equals(oldEntity.getLegal(),entity.getLegal())) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("法人姓名");
            detail.setBeforeValue(oldEntity.getLegal());
            detail.setAfterValue(entity.getLegal());
            this.entChangeHistoryDetailService.save(detail);
        }
        if (!org.apache.commons.lang3.StringUtils.equals(oldEntity.getLegalPhoneNumber(),entity.getLegalPhoneNumber())) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("法人电话");
            detail.setBeforeValue(oldEntity.getLegalPhoneNumber());
            detail.setAfterValue(entity.getLegalPhoneNumber());
            this.entChangeHistoryDetailService.save(detail);
        }
        if (! org.apache.commons.lang3.StringUtils.equals(oldEntity.getLegalIDNumber(),entity.getLegalIDNumber()) ) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("法人身份证");
            detail.setBeforeValue(oldEntity.getLegalIDNumber());
            detail.setAfterValue(entity.getLegalIDNumber());
            this.entChangeHistoryDetailService.save(detail);
        }
        if (! org.apache.commons.lang3.StringUtils.equals(oldEntity.getContact(),entity.getContact())   ) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("联系人");
            detail.setBeforeValue(oldEntity.getContact());
            detail.setAfterValue(entity.getContact());
            this.entChangeHistoryDetailService.save(detail);
        }
        if (! org.apache.commons.lang3.StringUtils.equals(oldEntity.getContactPhoneNumber(),entity.getContactPhoneNumber())) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("联系人电话");
            detail.setBeforeValue(oldEntity.getContactPhoneNumber());
            detail.setAfterValue(entity.getContactPhoneNumber());
            this.entChangeHistoryDetailService.save(detail);
        }
        if (! org.apache.commons.lang3.StringUtils.equals(oldEntity.getContactIDNumber(),entity.getContactIDNumber())) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("联系人身份证");
            detail.setBeforeValue(oldEntity.getContactIDNumber());
            detail.setAfterValue(entity.getContactIDNumber());
            this.entChangeHistoryDetailService.save(detail);
        }
        String oldPartner = org.apache.commons.lang3.StringUtils.defaultString(this.getPartnerByEnt(oldEntity));
        String newPartner = "";
        if (!CollectionUtils.isEmpty(partners)) {
            newPartner = partners.stream().map(item -> String.join("", item.getName(), "-", item.getProportion() + "%"))
                    .collect(Collectors.joining(","));
        }

        /*for (PartnerVo partner : partners) {
            newPartner += partner.getName() + "-" + partner.getProportion() + "%,";
        }
        newPartner = newPartner.substring(0, newPartner.length() - 1);*/
        if (!org.apache.commons.lang3.StringUtils.equals(oldPartner,newPartner)) {
            EntChangeHistoryDetail detail = new EntChangeHistoryDetail();
            detail.setChangeId(id);
            detail.setFiledName("股东信息");
            detail.setBeforeValue(oldPartner);
            detail.setAfterValue(newPartner);
            this.entChangeHistoryDetailService.save(detail);
        }
    }

    private String getPartnerByEnt(Ent ent) {
        List<EntPartner> partners = this.entPartnerService.search(ent.getId());
        if (CollectionUtils.isEmpty(partners)) {
            return "";
        }
        return partners.stream().map(item -> String.join("", item.getName(), "-", item.getProportion() + "%"))
                .collect(Collectors.joining(","));
    }

    private String getCategoryByEnt(Ent oldEntity) {
        String[] ids = oldEntity.getCategoryIds().split(",");
        StringBuilder categoryNames = new StringBuilder();
        for (String id : ids) {
            EntCategory category = this.categoryService.getById(Long.parseLong(id));
            categoryNames.append(category.getName()).append(",");
        }
        if (categoryNames.length() == 0)
            return "";
        else
            return categoryNames.substring(0, categoryNames.length() - 1);
    }

    @Override
    public PageVo<EntVo> duplicate(EntQueryParam entQueryParam) {
        Page<Ent> page = new Page<>(entQueryParam.getCurrent(), entQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        EntMapper.QueryParam params = new EntMapper.QueryParam();
        if (entQueryParam.getName() != null) {
            params.setName(Filter.LikeValue.both(entQueryParam.getName()));
        }
        if (entQueryParam.getLegal() != null) {
            params.setLegal(Filter.LikeValue.both(entQueryParam.getLegal()));
        }
        if (entQueryParam.getLegalPhoneNumber() != null) {
            params.setLegalPhoneNumber(Filter.LikeValue.both(entQueryParam.getLegalPhoneNumber()));
        }
        if (entQueryParam.getUnifiedSocialCreditCode() != null) {
            params.setUnifiedSocialCreditCode(Filter.LikeValue.both(entQueryParam.getUnifiedSocialCreditCode()));
        }
        Page<EntVo> entPage = new Page<>();
        if (entQueryParam.getName() != null || entQueryParam.getLegal() != null
                || entQueryParam.getLegalPhoneNumber() != null || entQueryParam.getUnifiedSocialCreditCode() != null) {
            entPage = this.getBaseMapper().selectByQueryParam(page, params);
        }

        return PageVo.create(
                entQueryParam.getCurrent(), entQueryParam.getPageSize(),
                entPage.getTotal(), entPage.getRecords()
        );
    }

    @Override
    public Boolean updateName(EntUpdateNameVo entUpdateNameVo) {
        QueryWrapper<Ent> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Ent::getName, entUpdateNameVo.getNewName());
        Ent existEnt = this.getOne(wrapper);
        ValidationUtil.notNull(existEnt, "企业改名", "企业名称", entUpdateNameVo.getNewName());
        Ent ent = this.getById(entUpdateNameVo.getId());
        EntUsedName entUsedName = this.saveUsedName(ent, entUpdateNameVo);
        ent.setName(entUpdateNameVo.getNewName());
        this.updateById(ent);
        //处理附件
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(entUpdateNameVo.getFiles())){
            List<SysFile> files = entUpdateNameVo.getFiles();

            List<Long> fileIds = files.stream().map(SysFile::getId).collect(Collectors.toList());
            sysFileService.updateFileForBusId(fileIds,entUsedName.getId(), SysFileType.OLDNAMEFILE.getValue());
            //删除与当前单据无关的附件
            sysFileService.deleteByBusinessId(fileIds,entUsedName.getId(), SysFileType.OLDNAMEFILE.getValue());

        }

        //如果名字更改了，合同保存的名字也要改
        if (!org.apache.commons.lang3.StringUtils.equals(entUpdateNameVo.getNewName(),ent.getName())){
            List<Contract> contracts = this.contractService.getBaseMapper().selectList(new QueryWrapper<Contract>().lambda().eq(Contract::getEntId, ent.getId()));
            if (!CollectionUtils.isEmpty(contracts)){
                for (Contract contract:contracts) {
                    contract.setEntName(entUpdateNameVo.getNewName());
                    this.contractService.getBaseMapper().updateById(contract);
                }
            }
        }
        return true;
    }

    @Override
    public PageVo<EntUsedName> queryUsedName(EntUsedNameQueryParam entUsedNameQueryParam) {
        Page<EntUsedName> page = new Page<>(entUsedNameQueryParam.getCurrent(), entUsedNameQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        QueryWrapper<EntUsedName> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EntUsedName::getEntId, entUsedNameQueryParam.getEntId());
        Page<EntUsedName> entUsedNamePage = this.entUsedNameService.page(page, wrapper);
        if (!CollectionUtils.isEmpty(entUsedNamePage.getRecords())){
            for (int i = 0; i < entUsedNamePage.getRecords().size(); i++) {
                EntUsedName entUsedName = entUsedNamePage.getRecords().get(i);
                entUsedName.setFiles(this.sysFileService.query().eq("business_id",entUsedName.getId()).eq("type",SysFileType.OLDNAMEFILE.getValue()).list());
            }
        }
        return PageVo.create(entUsedNameQueryParam.getCurrent(), entUsedNameQueryParam.getPageSize(),
                entUsedNamePage.getTotal(), entUsedNamePage.getRecords());
    }

    @Override
    public PageVo<EntChangeHistoryVo> queryHistory(EntChangeHistoryQueryParam entChangeHistoryQueryParam) {
        Page<EntChangeHistory> page = new Page<>(entChangeHistoryQueryParam.getCurrent(), entChangeHistoryQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        QueryWrapper<EntChangeHistory> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EntChangeHistory::getEntId, entChangeHistoryQueryParam.getEntId());
        Page<EntChangeHistory> entChangeHistoryPage = this.entChangeHistoryService.page(page, wrapper);
        List<EntChangeHistoryVo> list = new ArrayList<>();
        for (EntChangeHistory record : entChangeHistoryPage.getRecords()) {
            EntChangeHistoryVo entChangeHistoryVo = new EntChangeHistoryVo();
            Ent ent = this.getById(record.getEntId());
            entChangeHistoryVo.setId(record.getId());
            entChangeHistoryVo.setEntName(ent.getName());
            entChangeHistoryVo.setCreateTime(record.getCreateTime().toString().replace("T", " "));
            entChangeHistoryVo.setCreateBy(record.getCreateBy());
            list.add(entChangeHistoryVo);
        }
        return PageVo.create(entChangeHistoryQueryParam.getCurrent(), entChangeHistoryQueryParam.getPageSize(),
                entChangeHistoryPage.getTotal(), list);
    }

    @Override
    public PageVo<EntChangeHistoryDetailVo> queryChangeDetail(EntChangeHistoryDetailQueryParam entChangeHistoryDetailQueryParam) {
        Page<EntChangeHistoryDetail> page = new Page<>(entChangeHistoryDetailQueryParam.getCurrent(), entChangeHistoryDetailQueryParam.getPageSize());
        QueryWrapper<EntChangeHistoryDetail> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EntChangeHistoryDetail::getChangeId, entChangeHistoryDetailQueryParam.getChangeId());
        Page<EntChangeHistoryDetail> entChangeHistoryDetailPage = this.entChangeHistoryDetailService.page(page, wrapper);
        return PageVo.create(entChangeHistoryDetailQueryParam.getCurrent(), entChangeHistoryDetailQueryParam.getPageSize(),
                entChangeHistoryDetailPage.getTotal(),
                EntChangeHistoryDetailConvert.INSTANCE.entity2ListItemBatch(entChangeHistoryDetailPage.getRecords()));
    }

    @Override
    public Ent getByName(String entName) {
        QueryWrapper<Ent> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Ent::getName, entName.trim());
        return this.getOne(wrapper);
    }

    @Override
    public ProtraitVo getProtriatVo(Long entId) {
        Ent ent = this.getById(entId);
        if (ent == null) {
            return new ProtraitVo();
        }
        ProtraitVo protraitVo = EntConvert.INSTANCE.entity2ProtraitVo(ent);
        List<EntPartner> partners = this.entPartnerService.search(entId);
        protraitVo.setPartners(partners);
        List<EntMatters> entMatters = this.mattersService.getByEntId(entId);
        protraitVo.setMatters(entMatters);
        List<EntUsedName> usedNames = this.entUsedNameService.getByEntId(entId);
        List<String> names = new ArrayList<>();
        for (EntUsedName usedName : usedNames) {
            names.add(usedName.getUsedName());
        }
        protraitVo.setUsedNames(Joiner.on(",").join(names));

        if (!StringUtils.hasText(ent.getCategoryIds())) {
            String[] categoryIds = ent.getCategoryIds().split(",");
            String[] categoryNameList = new String[categoryIds.length];
            for (int index = 0; index < categoryIds.length; index++) {
                EntCategory category = this.categoryService.getById(categoryIds[index]);
                categoryNameList[index] = category.getName();
                protraitVo.setCategoryNames(String.join(",", categoryNameList));
            }
        }

        List<ContractVo> contracts = this.contractService.getByListEntId(ent.getId());
        protraitVo.setContracts(contracts);

        return protraitVo;
    }

    @Override
    public List<KeyValueVo> searchNewSettleList() {
        List<KeyValueVo> list = new ArrayList<>();
        QueryWrapper<Ent> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Ent::getNewSettle, NewSettleStatus.yes);
        List<Ent> ents = this.list(wrapper);
        for (Ent ent : ents) {
            KeyValueVo vo = new KeyValueVo();
            vo.setKey(ent.getId());
            vo.setValue(ent.getName());
            list.add(vo);
        }
        return list;
    }

    @Override
    public PageVo<EntVo> duplicate_excel(PageParams params, List[] results) {
        List list = results[0];

        List<String> entNames = new ArrayList<>();
        List<String> codes = new ArrayList<>();
        List<String> legals = new ArrayList<>();
        List<String> legalPhoneNumbers = new ArrayList<>();

        for (int index = 0; index < list.size(); index++) {
            entNames.add(((DuplicateSearch) list.get(index)).getName());
            codes.add(((DuplicateSearch) list.get(index)).getCode());
            legals.add(((DuplicateSearch) list.get(index)).getLegal());
            legalPhoneNumbers.add(((DuplicateSearch) list.get(index)).getPhone());
        }

        Page<Ent> page = new Page<>(params.getCurrent(), params.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        QueryWrapper<Ent> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(Ent::getName, entNames)
                .or().in(Ent::getUnifiedSocialCreditCode, codes)
                .or().in(Ent::getLegal, legals)
                .or().in(Ent::getLegalPhoneNumber, legalPhoneNumbers);

        Page<Ent> entPage = this.page(page, wrapper);
        return PageVo.create(params.getCurrent(), params.getPageSize(),
                entPage.getTotal(), EntConvert.INSTANCE.entity2ListItemBatch(entPage.getRecords()));
    }

    @Override
    public List<KeyValueVo> getKeyValueList() {
        List<KeyValueVo> list = new ArrayList<>();
        List<Ent> ents = this.list();
        for (Ent ent : ents) {
            KeyValueVo vo = new KeyValueVo();
            vo.setKey(ent.getId());
            vo.setValue(ent.getName());
            list.add(vo);
        }
        return list;
    }

    @Override
    public List<KeyValueVo> searchOldSettleList() {
        List<KeyValueVo> list = new ArrayList<>();
        QueryWrapper<Ent> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Ent::getNewSettle, NewSettleStatus.no);
        List<Ent> ents = this.list(wrapper);
        for (Ent ent : ents) {
            KeyValueVo vo = new KeyValueVo();
            vo.setKey(ent.getId());
            vo.setValue(ent.getName());
            list.add(vo);
        }
        return list;
    }

    @Override
    public void clearEntById(Long entId) {
        this.removeById(entId);
        this.entPartnerService.remove(
                new UpdateWrapper<EntPartner>()
                        .lambda()
                        .eq(EntPartner::getEntId, entId)
        );
    }

    @Override
    public boolean validateName(Long entId, String name) {
        if (!StringUtils.hasText(name)) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.DATE_NULL).message("企业名称不能为空"));
        }
        LambdaQueryWrapper<Ent> wrapper = new QueryWrapper<Ent>().lambda().eq(Ent::getName, name);

        if (entId != null) {
            wrapper.ne(Ent::getId, entId);
        }
        int count = count(wrapper);
        return count == 0;
    }

    @Override
    public List<PartnerVo> getPartnerByEntId(Long entId) {
        List<EntPartner> search = this.entPartnerService.search(entId);
        if (CollectionUtils.isEmpty(search)) {
            return Collections.emptyList();
        }
        return search.stream().map(item -> new PartnerVo().setName(item.getName()).setProportion(item.getProportion())).collect(Collectors.toList());
    }

    @Override
    public void searchExport(EntQueryParam entQueryParam, HttpServletResponse response) {
        entQueryParam.setPageSize(10000);
        entQueryParam.setCurrent(1);
        PageVo<EntVo> search = search(entQueryParam);
        if (CollUtil.isNotEmpty(search.getContent())){
            //数据不为空则开始生成表
            ExcelWriter writer = new ExcelWriter(true);
            writer.setOnlyAlias(true);
            //创建标题行 和列名
            ReportServiceImpl.createExportTitleAndHeadByClass(writer, EntVo.class, "企业列表");
            //添加行，并且强制输出行
            writer.write(search.getContent(), true);
            //设置标题行的样式，行高
            writer.setRowHeight(0, 25);
            writer.setRowHeight(1, 18);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ EscapeUtil.escape("企业列表") +".xlsx");
            try {
                ServletOutputStream out = response.getOutputStream();
                writer.flush(out, true);
                // 关闭writer，释放内存
                writer.close();
                //此处记得关闭输出Servlet流
                IoUtil.close(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存企业曾用名
     *
     * @param ent
     * @param entUpdateNameVo
     */
    private EntUsedName saveUsedName(Ent ent, EntUpdateNameVo entUpdateNameVo) {
        EntUsedName entUsedName = new EntUsedName();
        entUsedName.setEntId(ent.getId());
        entUsedName.setNewName(entUpdateNameVo.getNewName());
        entUsedName.setUsedName(ent.getName());
        this.entUsedNameService.save(entUsedName);
        return entUsedName;
    }

    /**
     * 校验名称唯一
     *
     * @param name 名称
     */
    private void validNameUnique(String name) {
        if (StringUtils.hasText(name)) {
            QueryWrapper<Ent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(Ent::getName, name);
            Ent entity = this.getOne(wrapper);
            ValidationUtil.notNull(entity, "企业", "名称", name);
        }

    }

    /**
     * 校验统一社会代码唯一
     *
     * @param unifiedSocialCreditCode
     */
    private void validUniCodeUnique(String unifiedSocialCreditCode) {
        if (StringUtils.hasText(unifiedSocialCreditCode)) {
            QueryWrapper<Ent> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(Ent::getUnifiedSocialCreditCode, unifiedSocialCreditCode);
            Ent entity = this.getOne(wrapper);
            ValidationUtil.notNull(entity, "企业", "统一社会代码", unifiedSocialCreditCode);
        }

    }

}
