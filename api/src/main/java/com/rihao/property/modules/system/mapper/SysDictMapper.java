package com.rihao.property.modules.system.mapper;

import com.rihao.property.modules.system.entity.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据字典表 Mapper 接口
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    List<SysDict> selectByParentCode(@Param("code") String code);

}
