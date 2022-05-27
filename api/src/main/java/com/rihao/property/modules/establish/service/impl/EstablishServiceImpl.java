package com.rihao.property.modules.establish.service.impl;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.pojo.query.Filter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rihao.property.common.oss.IOssService;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.establish.controller.params.EstablishQueryParam;
import com.rihao.property.modules.establish.convert.EstablishConvert;
import com.rihao.property.modules.establish.entity.Establish;
import com.rihao.property.modules.establish.mapper.EstablishMapper;
import com.rihao.property.modules.establish.service.IEstablishService;
import com.rihao.property.modules.establish.vo.EstablishVo;
import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.entity.ParkOperator;
import com.rihao.property.modules.estate.service.IParkOperatorService;
import com.rihao.property.modules.estate.service.IParkService;
import com.rihao.property.modules.estate.vo.ParkVo;
import com.rihao.property.modules.system.controller.params.RoleQueryParam;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.service.ISysRoleService;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.modules.system.vo.SysRoleVo;
import com.rihao.property.modules.system.vo.SysUserCreateVo;
import com.rihao.property.shiro.util.JwtUtil;
import com.rihao.property.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 单位信息表 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-23
 */
@Service
public class EstablishServiceImpl extends ServiceImpl<EstablishMapper, Establish> implements IEstablishService {

    private IParkService parkService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Override
    public PageVo<Establish> search(EstablishQueryParam establishQueryParam) {
        Page<Establish> page = new Page<>(establishQueryParam.getCurrent(), establishQueryParam.getPageSize());
        QueryWrapper<Establish> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(establishQueryParam.getName())) {
            wrapper.lambda().like(Establish::getName, Filter.LikeValue.both(establishQueryParam.getName()));
        }
        Page<Establish> establishPage = this.page(page, wrapper);
        return PageVo.create(establishQueryParam.getCurrent(), establishQueryParam.getPageSize(),
                establishPage.getTotal(), establishPage.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createNew(EstablishVo createVo) {
        this.validNameUnique(createVo.getName(), null);
        Establish establish = EstablishConvert.INSTANCE.createParam2Entity(createVo);
        Boolean result= this.save(establish);
        //新增单位的时候新增一个对应的管理员
        if (result){
            SysUserCreateVo userVo=new SysUserCreateVo();
            userVo.setEstablishId(establish.getId());
            userVo.setUsername("admin."+createVo.getContractPrefix());
            userVo.setRealName("单位管理员");
            //默认绑定一个单位管理员
            RoleQueryParam roleQueryParam = new RoleQueryParam();
            roleQueryParam.setName("单位管理员");
            PageVo<SysRoleVo> search = sysRoleService.search(roleQueryParam);
            if (search != null && search.getContent().size()>0){
                userVo.setRoleId(search.getContent().get(0).getId());
                sysUserService.createNew(userVo);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(EstablishVo updateVo) {
        this.validNameUnique(updateVo.getName(), updateVo.getId());
        Establish establish = EstablishConvert.INSTANCE.updateParam2Entity(updateVo);
        return this.updateById(establish);
    }

    @Override
    public Boolean delete(Long id) {
        List<KeyValueVo> parks = this.parkService.getByEstId(id);
        if (parks.size() != 0) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("该单位下存在园区，不允许删除"));
        }
        return this.removeById(id);
    }

    @Override
    public Establish detail(Long id) {
        return this.getById(id);
    }

    @Override
    public List<Establish> search() {
        return this.list();
    }

    @Override
    public Establish getByName(String estName) {
        QueryWrapper<Establish> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Establish::getName, estName);
        return this.getOne(wrapper);
    }

    private void validNameUnique(String name, Long id) {
        QueryWrapper<Establish> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Establish::getName, name);
        if (id != null && id != 0) {
            wrapper.lambda().ne(Establish::getId, id);
        }
        Establish entity = this.getOne(wrapper);
        ValidationUtil.notNull(entity, "单位", "名称", name);
    }

    @Autowired
    private void setParkService(IParkService parkService) {
        this.parkService = parkService;
    }
}
