package com.rihao.property.modules.system.service.impl;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.pojo.query.Filter;
import com.anteng.common.security.password.PasswordHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.conf.AppProperties;
import com.rihao.property.modules.establish.entity.Establish;
import com.rihao.property.modules.establish.service.IEstablishService;
import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.entity.ParkOperator;
import com.rihao.property.modules.estate.service.IBuildingService;
import com.rihao.property.modules.estate.service.IParkOperatorService;
import com.rihao.property.modules.estate.service.IParkService;
import com.rihao.property.modules.system.controller.params.AdminQueryParam;
import com.rihao.property.modules.system.convert.SysUserConvert;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.entity.SysUserOrganization;
import com.rihao.property.modules.system.mapper.SysUserMapper;
import com.rihao.property.modules.system.mapper.SysUserOrganizationMapper;
import com.rihao.property.modules.system.service.ISysRoleService;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.modules.system.vo.SysUserCreateVo;
import com.rihao.property.modules.system.vo.SysUserUpdateVo;
import com.rihao.property.modules.system.vo.SysUserVo;
import com.rihao.property.shiro.util.JwtUtil;
import com.rihao.property.shiro.vo.ChangeProfileVo;
import com.rihao.property.shiro.vo.ChangePwdVo;
import com.rihao.property.shiro.vo.CurrentUserVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private ISysRoleService roleService;

    private AppProperties appProperties;
    private SysUserOrganizationMapper userOrganizationMapper;

    private IParkService parkService;
    private IEstablishService establishService;
    private IParkOperatorService parkOperatorService;

    @Override
    public boolean createNew(SysUserCreateVo createVo) {
        SysUser sysUser = SysUserConvert.INSTANCE.createParam2Entity(createVo);
        //得到盐值
        String salt = PasswordHelper.generateSalt();
        //加密密码
        String password = PasswordHelper.encodePassword(appProperties.getInitPassword(), salt);
        sysUser.setPassword(password);
        sysUser.setSalt(salt);

        Boolean result = this.save(sysUser);
        ParkOperator parkOperator;
        if (createVo.getParkIdList() != null && createVo.getParkIdList().length > 0) {
            for (Long parkId : createVo.getParkIdList()) {
                parkOperator = new ParkOperator();
                parkOperator.setOperatorId(sysUser.getId());
                parkOperator.setParkId(parkId);
                this.parkOperatorService.save(parkOperator);
            }
        }
        return result;
    }

    @Override
    public boolean update(SysUserUpdateVo vo) {
        SysUser sysUser = this.getById(vo.getId());
        sysUser.setTelephone(vo.getTelephone());
        sysUser.setUsername(vo.getUsername());
        sysUser.setRealName(vo.getRealName());
        sysUser.setRoleId(vo.getRoleId());
        if (vo.getParkIdList() != null) {
            List<ParkOperator> parkOperators = this.parkOperatorService.getByOperatorId(sysUser.getId());
            for (ParkOperator parkOperator : parkOperators) {
                this.parkOperatorService.removeById(parkOperator.getId());
            }
            ParkOperator parkOperator;
            for (Long parkId : vo.getParkIdList()) {
                parkOperator = new ParkOperator();
                parkOperator.setOperatorId(sysUser.getId());
                parkOperator.setParkId(parkId);
                this.parkOperatorService.save(parkOperator);
            }
        }
        sysUser.setEstablishId(vo.getEstablishId());
        return this.updateById(sysUser);
    }

    @Override
    public PageVo<SysUserVo> search(SysUser user, AdminQueryParam queryParam) {
        Page<SysUser> page = new Page<>(queryParam.getCurrent(), queryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        SysUserMapper.AdminQueryParam params = new SysUserMapper.AdminQueryParam()
                .setRole(queryParam.getRole());
        if (StringUtils.hasText(queryParam.getUsername())) {
            params.setUsername(Filter.LikeValue.both(queryParam.getUsername()));
        }
        if (StringUtils.hasText(queryParam.getRealName())) {
            params.setRealName(Filter.LikeValue.both(queryParam.getRealName()));
        }
        // 非超级管理员只能查看自己的单位用户
        if (StringUtils.hasText(queryParam.getEstablishId())) {//1为超级管理员
                params.setEstablishId(queryParam.getEstablishId());
        }
        Page<SysUserVo> result = this.getBaseMapper().selectByAdminQueryParam(page, params);
        List<SysUserVo> vos = result.getRecords();
        List<SysUserVo> list = new ArrayList<>();
        for (SysUserVo vo : vos) {
            List<ParkOperator> parkOperators = this.parkOperatorService.getByOperatorId(vo.getId());
            if (parkOperators.size() != 0) {
                String[] parkNameListDetail = new String[parkOperators.size()];
                Long[] parkIdList = new Long[parkOperators.size()];
                for (int index = 0; index < parkOperators.size(); index++) {
                    //园区权限
                    Park park = this.parkService.getById(parkOperators.get(index).getParkId());
                    if (park != null) {
                        parkNameListDetail[index] = park.getName();
                        parkIdList[index] = park.getId();
                    }
                }
                vo.setParkNameListDetail(Stream.of(parkNameListDetail).filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(",")));
                vo.setParkIdList(parkIdList);
            }
            //单位名称
            if (vo.getEstablishId() != null) {
                Establish establish = this.establishService.getById(vo.getEstablishId());
                if (establish != null && establish.getName() != null) {
                    vo.setEstablish(establish.getName());
                }
            }
            //去除掉排除当前用户不显示的
            /*if (!Objects.equals(vo.getId(), JwtUtil.getCurrentUser().getId())) {

            }*/
            list.add(vo);
        }

        return PageVo.create(queryParam.getCurrent(), queryParam.getPageSize(),
                result.getTotal(), list);
    }

    @Override
    public SysUserVo findOne(Long id) {
        SysUser notice = this.getById(id);
        if (notice == null) {
            return null;
        }
        SysUserVo vo = SysUserConvert.INSTANCE.entity2ListItem(notice);
        List<ParkOperator> parkOperators = this.parkOperatorService.getByOperatorId(vo.getId());
        Long[] parkList = new Long[parkOperators.size()];
        for (int index = 0; index < parkOperators.size(); index++)
            parkList[index] = parkOperators.get(index).getParkId();
        vo.setParkIdList(parkList);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        this.userOrganizationMapper.delete(new QueryWrapper<SysUserOrganization>().lambda().eq(SysUserOrganization::getUserId, id));
        return this.removeById(id);
    }

    @Override
    public boolean resetPassword(Long id) {
        return this.changePassword(id, this.appProperties.getInitPassword());
    }

    private boolean changePassword(Long userId, String password) {
        String salt = PasswordHelper.generateSalt();
        String secret = PasswordHelper.encodePassword(password, salt);
        return this.update(new UpdateWrapper<SysUser>()
                .lambda()
                .set(SysUser::getSalt, salt)
                .set(SysUser::getPassword, secret)
                .set(SysUser::getLoginNumber,1)
                .eq(SysUser::getId, userId));
    }

    @Override
    public boolean changePassword(String username, ChangePwdVo changePwdVo) {
        SysUser sysUser = getSysUserByUsername(username);
        if (sysUser == null) {
            return false;
        }
        boolean verifyResult = PasswordHelper.verifyPassword(changePwdVo.getOldPassword(), sysUser.getPassword(), sysUser.getSalt());
        if (!verifyResult) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("原密码错误"));
        }

        if (!sysUser.getPassword().matches("^(?![A-Za-z]+$)(?![A-Z\\d]+$)(?![A-Z\\W]+$)(?![a-z\\d]+$)(?![a-z\\W]+$)(?![\\d\\W]+$)\\S{8,30}$")) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("密码格式需由数字，大写字母，小写字母，特殊符号四选三组成，且不小于8位！"));
        }
        return this.changePassword(sysUser.getId(), changePwdVo.getNewPassword());
    }

    @Override
    public CurrentUserVo getCurrentUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username不能为空");
        }
        SysUser current = getSysUserByUsername(username);
        if (current == null) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.DATE_NULL).message("用户不存在"));
        }
        CurrentUserVo currentUserVo = SysUserConvert.INSTANCE.sysUserToCurrentUserVo(current);
        if (currentUserVo.getEstablishId() != null) {
            Establish establish = this.establishService.getById(currentUserVo.getEstablishId());
            currentUserVo.setEstablishName(establish.getName());
        }

        this.modifyLastLoginTime(username);
        return currentUserVo;
    }

    @Override
    public SysUser getSysUserByUsername(String username) {
        return this.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username));
    }

    @Override
    public SysUser getByTelephone(String telephone) {
        return this.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getTelephone, telephone));
    }

    @Override
    public boolean changProfile(String username, ChangeProfileVo vo) {
        LambdaUpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<SysUser>().lambda().eq(SysUser::getUsername, username);
        return this.update(updateWrapper);
    }

    @Override
    public boolean changeState(Long id, SysUser.State state) {
        return this.update(new UpdateWrapper<SysUser>().lambda()
                .eq(SysUser::getId, id)
                .set(SysUser::getState, state)
        );
    }

    private void modifyLastLoginTime(String username) {
        this.update(new UpdateWrapper<SysUser>().lambda()
                .eq(SysUser::getUsername, username)
                .set(SysUser::getLastLoginTime, LocalDateTime.now())
        );
    }


    @Autowired
    private void setAppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Autowired
    private void setRoleService(ISysRoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    private void setUserOrganizationMapper(SysUserOrganizationMapper userOrganizationMapper) {
        this.userOrganizationMapper = userOrganizationMapper;
    }

    @Autowired
    private void setParkOperatorService(IParkOperatorService parkOperatorService) {
        this.parkOperatorService = parkOperatorService;
    }

    @Autowired
    private void setParkService(IParkService parkService) {
        this.parkService = parkService;
    }

    @Autowired
    private void setEstablishService(IEstablishService establishService) {
        this.establishService = establishService;
    }
}
