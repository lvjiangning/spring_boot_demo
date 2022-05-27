package com.rihao.property.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 * 确的浮点数运算，包括加减乘除和四舍五入。
 * <p/>
 * Created by Jason on 2016/10/19.
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10; //这个类不能实例化

    private NumberUtils() {
    }

    /**
     * 提供精确的加法运算。 v1 + v2 = ?
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。 v1 - v2 = ?
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。 v1 * v2 = ?
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供乘法运算(保留指定几位小数)。 v1 * v2 = ?
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2, int scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 格式化数字为带千分位字符  1234 -> 1,234.00
     *
     * @param value
     * @param pattern
     * @return
     */
    public static String formatNumber(Double value, String pattern) {
        if (value != null) {
            DecimalFormat df = new DecimalFormat();
            if (StringUtils.isNotBlank(pattern)) {
                df.applyPattern(pattern);
            } else {
                df.applyPattern("#,##0.00");
            }
            return df.format(value);
        }
        return null;
    }

    /**
     * 格式化数字为带千分位字符  1234 -> 1,234.00
     *
     * @param value
     * @return
     */
    public static String formatNumber(Double value) {
        return formatNumber(value, "#,##0.00");
    }

    /**
     * 格式化数字为带千分位字符  1234 -> 1,234
     * 为0则不显示，不保留小数
     *
     * @param value
     * @return
     */
    public static String formatNumberWithOutZero(Double value) {
        if (value == null || value == 0) {
            return "";
        }
        return formatNumber(value, "#,##0");
    }

    /**
     * 格式化数字为带千分位字符  1234 -> 1,234.00
     * 为0则不显示,保留小数
     *
     * @param value
     * @return
     */
    public static String formatNumberWithOutZero1(Double value) {
        if (value == null || value == 0) {
            return "";
        }
        return formatNumber(value, "#,##0.00");
    }

    /**
     * double类型去掉小数点后多余的0，若全是0则.也去掉
     * @param value
     * @return
     */
    public static String formatDoubleWithOutZero(Double value){
        String string = value.toString();
        if (StringUtils.isNotBlank(string)){
            if(string.indexOf(".") > 0){
                //正则表达
                string = string.replaceAll("0+?$", "");//去掉后面无用的零
                string = string.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
            }
        }
        return string;
    }
    /**
     * 求值的绝对值
     *
     * @param value
     * @return
     */
    public static Double abs(Double value) {
        return Math.abs(value);
    }

    /**
     * 将带千分位的字符格式化为Double 例如：1,234.00 -> 1234
     *
     * @param value
     * @return
     */
    public static Double decodeFormatNumber(String value) {
        if (StringUtils.isNotBlank(value)) {
            String _value = value.replaceAll(",", "");
            if (org.apache.commons.lang3.math.NumberUtils.isNumber(_value)) {
                return Double.parseDouble(_value);
            } else {
                return null;
            }
        }
        return null;
    }

	/**
	 * 将带千分位的字符格式化为Integer 例如：1,234 -> 1234
	 *
	 * @param value
	 * @return
	 */
	public static Integer decodeFormatInteger(String value) {
		if (StringUtils.isNotBlank(value)) {
			String _value = value.replaceAll(",", "");
			if (org.apache.commons.lang3.math.NumberUtils.isNumber(_value)) {
				return Integer.valueOf(_value);
			} else {
				return null;
			}
		}
		return null;
	}

    /**
     * 判断对象类型返回double
     *
     * @param object
     * @return
     */
    public static Double getDouble(Object object) {
        if (object != null) {
            if (object instanceof BigDecimal) {
                BigDecimal value = ((BigDecimal) object);
                return value.doubleValue();
            } else if (object instanceof Double) {
                return (Double) object;
            } else if (object instanceof Integer) {
                return ((Integer) object).doubleValue();
            } else if (object instanceof String) {
                String value = (String) object;
                if (StringUtils.isNotBlank(value) ) {
                    if(isNumber(value)){
                        return Double.parseDouble(value);
                    }else if(value.contains(",")){
                        return decodeFormatNumber(value);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 判断对象类型返回double
     *
     * @param object
     * @return
     */
    public static Double getDouble(Object object,Double def) {
        Double result = getDouble(object);

        return result == null ? def:result;
    }

    /**
     * 判断字符串是否为数字类型
     *
     * @param str 要比较的字符串对象
     * @return true：可转为数字；false：不能转为数字
     */
    public static boolean isNumber(String str) {
        return org.apache.commons.lang3.math.NumberUtils.isNumber(str);
    }

    /**
     * 判断字符串是否为数字整数类型
     *
     * @param str 要比较的字符串对象
     * @return true：可转为整形数字；false：不能转为整形数字
     */
    public static boolean isDigits(String str) {
        return org.apache.commons.lang3.math.NumberUtils.isDigits(str);
    }

    /**
     * 计算v1/v2的百分比，返回结果不带%分号
     *
     * @param v1 参数1
     * @param v2 参数2
     * @return 百分比
     */
    public static String percent(Double v1, Double v2) {
        if (v1 == null || v1 <= 0 || v2 == null || v2 <= 0) {
            return "0.00";
        }
        return formatNumber(mul(div(v1, v2), 100), "0.00");
    }

    /**
     * 返回Double和Object相加的值
     * @return a + b
     */
    public static Double doubleAddObject(Double a, Object b) {

        if (a == null && b == null) return 0D;

        if (a == null) return getDouble(b);

        if (b == null) return a;

        return add(a, getDouble(b));
    }

    /**
     * 文件大小转换成可显示的B、KB、MB、GB
     * <p>
     * //@param size 文件大小，byte
     *
     * @return 文件大小加上单位，例如：2 MB
     */
    /*
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }
*/


    public static void main(String[] args) {
//        System.out.println(convertFileSize(6010));

//        System.out.println(formatNumber(null));
//        System.out.println(formatNumber(0d, null));
//        System.out.println(formatNumber(0.001d));
//        System.out.println(formatNumber(0d));
//        System.out.println(formatNumber(1324d));
//        System.out.println(formatNumber(1234.12d));
//        System.out.println(formatNumber(100d));
//        System.out.println(formatNumber(123456789123456d));
//
//        System.out.println(decodeFormatNumber("1,220.00"));

//        double a = 0.1d;
//        double b = 0.2d;
//
//        System.out.println(a + b);
//        System.out.println(add(a, b));
//
//        double c = 0.4d;
//        double d = 0.3d;
//        System.out.println(c - d);
//        System.out.println(sub(c, d));

        System.out.println(percent(100d, 0.2d));
        System.out.println(percent(0.1d, 0.2d));
        System.out.println(percent(1d, 3d));
        System.out.println(percent(3d, 10d));
    }
}
