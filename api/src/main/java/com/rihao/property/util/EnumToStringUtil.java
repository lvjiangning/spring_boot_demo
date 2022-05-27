package com.rihao.property.util;

import com.anteng.boot.pojo.enum_.MetaUtils;

/**
 * @author gaoy
 * 2020/3/28/028
 */
public class EnumToStringUtil {
    public static <T extends Enum> String convert(T t) {
        if (t == null) {
            return "";
        }
        MetaUtils.Kv kv = MetaUtils.getKv(t);
        if (kv == null) {
            return t.name();
        }
        return kv.getValue();
    }
}
