package com.rihao.property.common.enums;

import com.anteng.boot.pojo.enum_.IMEnum;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumMixTypeHandler<E extends Enum<E>> extends EnumTypeHandler<E> {
    private Class<E> type;

    public EnumMixTypeHandler(Class<E> type) {
        super(type);
        this.type = type;
    }

    private E convert(int v) {
        E[] objs = type.getEnumConstants();
        for (E em : objs) {
            if (((IMEnum) em).getValue() == v) {
                return em;
            }
        }
        return null;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (IMEnum.class.isAssignableFrom(type)) {
            ps.setInt(i, ((IMEnum) parameter).getValue());
            return;
        }
        super.setNonNullParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (IMEnum.class.isAssignableFrom(type)) {
            return convert(rs.getInt(columnName));
        }
        return super.getNullableResult(rs, columnName);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (IMEnum.class.isAssignableFrom(type)) {
            return convert(rs.getInt(columnIndex));
        }
        return super.getNullableResult(rs, columnIndex);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (IMEnum.class.isAssignableFrom(type)) {
            return convert(cs.getInt(columnIndex));
        }
        return super.getNullableResult(cs, columnIndex);
    }
}
