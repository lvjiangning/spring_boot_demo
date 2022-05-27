package com.rihao.property.util.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.read.metadata.holder.ReadHolder;
import com.alibaba.excel.read.metadata.property.ExcelReadHeadProperty;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangxiaoxiong
 */
public class ExcelListener<T> extends AnalysisEventListener<T> {

    private final List<Object> dataList = new ArrayList<>();

    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     */
    @Override
    public void invoke(T object, AnalysisContext context) {
        if(!checkObjAllFieldsIsNull(object)) {
            dataList.add(object);
        }
    }

    /**
     * 此方法用来校验表头是否和下载模板一致
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        //获取对象head名称
        List<String> expectHeadList = Optional.ofNullable(context)
                .map(AnalysisContext::currentReadHolder)
                .map(ReadHolder::excelReadHeadProperty)
                .map(ExcelReadHeadProperty::getHeadMap)
                .map(Map::values)
                .orElse(Collections.emptyList())
                .stream().map(Head::getHeadNameList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        //表头数量检测
        if (!Objects.equals(headMap.size(), expectHeadList.size())) {
            throw new  ExcelAnalysisException("导入模板与系统中的不一致");
        }
        //表头名称检测
        for (int i = 0; i < headMap.size(); i++) {
            if (!Objects.equals(headMap.get(i), expectHeadList.get(i))) {
                throw new ExcelAnalysisException("导入模板与系统中的不一致");
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //do something
    }

    private static final String SERIAL_VERSION_UID = "serialVersionUID";

    /**
     * 判断对象中属性值是否全为空
     */
    private static boolean checkObjAllFieldsIsNull(Object object) {
        if (null == object) {
            return true;
        }
        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                //只校验带ExcelProperty注解的属性
                ExcelProperty property = f.getAnnotation(ExcelProperty.class);
                if(property == null || SERIAL_VERSION_UID.equals(f.getName())){
                    continue;
                }
                if (f.get(object) != null && MyStringUtils.isNotBlank(f.get(object).toString())) {
                    return false;
                }
            }
        } catch (Exception e) {
            //do something
        }
        return true;
    }

    public List<?> getDataList() {
        return dataList;
    }
}