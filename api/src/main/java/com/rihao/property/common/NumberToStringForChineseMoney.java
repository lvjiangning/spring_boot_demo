package com.rihao.property.common;

import java.math.BigDecimal;

public class NumberToStringForChineseMoney {

    static String HanDigiStr[] = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆",
            "柒", "捌", "玖"};

    static String HanDiviStr[] = new String[]{"", "拾", "佰", "仟", "万", "拾", "佰", "仟",
            "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万",
            "拾", "佰", "仟"};

    /**
     * 负责把小数点前面的数转换为大写中文
     * <p>
     * 输入字符串必须正整数，只允许前面有空格(必须右对齐)，不允许前面有零
     *
     * @param numberStr
     * @return
     */
    private static String positiveIntegerToHanString(String numberStr) {
        String RMBStr = "";
        boolean lastzero = false;
        boolean hasvalue = false; // 亿、万进位前有数值标记
        int len, n;
        len = numberStr.length();
        if (len > 15)
            return "金额过大!";
        for (int i = len - 1; i >= 0; i--) {
            if (numberStr.charAt(len - i - 1) == ' ')
                continue;
            n = numberStr.charAt(len - i - 1) - '0';
            if (n < 0 || n > 9)
                return "金额含非数字字符!";

            if (n != 0) {
                if (lastzero)
                    RMBStr += HanDigiStr[0]; // 若干零后若跟非零值，只显示一个零
                // 除了亿万前的零不带到后面
                // if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) ) //
                // 如十进位前有零也不发壹音用此行
//              if (!(n == 1 && (i % 4) == 1 && i == len - 1)) // 十进位处于第一位不发壹音
                RMBStr += HanDigiStr[n];
                RMBStr += HanDiviStr[i]; // 非零值后加进位，个位为空
                hasvalue = true; // 置万进位前有值标记

            } else {
                if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) // 亿万之间必须有非零值方显示万
                    RMBStr += HanDiviStr[i]; // “亿”或“万”
            }
            if (i % 8 == 0)
                hasvalue = false; // 万进位前有值标记逢亿复位
            lastzero = (n == 0) && (i % 4 != 0);
        }

        if (RMBStr.length() == 0)
            return HanDigiStr[0]; // 输入空字符或"0"，返回"零"
        return RMBStr;
    }

    /**
     * 输入double型数转换为大写中文
     *
     * @param doubleValue
     * @return 大写中文
     */
    public static String getChineseMoneyStringForDoubleVal(double doubleValue) {
        String SignStr = "";
        String TailStr = "";
        long fraction, integer;
        int jiao, fen;

        if (doubleValue < 0) {
            doubleValue = -doubleValue;
            SignStr = "负";
        }
        if (doubleValue > 99999999999999.999
                || doubleValue < -99999999999999.999)
            return "金额数值位数过大!";
        // 四舍五入到分
        long temp = Math.round(doubleValue * 100);
        integer = temp / 100;
        fraction = temp % 100;
        jiao = (int) fraction / 10;
        fen = (int) fraction % 10;
        if (jiao == 0 && fen == 0) {
            TailStr = "整";
        } else {
            TailStr = HanDigiStr[jiao];
            if (jiao != 0)
                TailStr += "角";
            if (integer == 0 && jiao == 0) // 零圆后不写零几分
                TailStr = "";
            if (fen != 0)
                TailStr += HanDigiStr[fen] + "分";
        }

        return (doubleValue >= 1) ? (SignStr + positiveIntegerToHanString(String.valueOf(integer))
                + "圆" + TailStr) : TailStr;
    }

    /**
     * 输入BigDecimal型数转换为大写中文
     * <p>
     * 精度取决于BigDecimal 的 public double doubleValue() 方法： 是基本收缩转换。 如果此 BigDecimal
     * 的数量太大而不能表示为 double，则将其适当地转换为 Double.NEGATIVE_INFINITY 或
     * Double.POSITIVE_INFINITY。 即使在返回值为有限值的情况下，此转换也可能丢失关于 BigDecimal 值精度的信息。
     *
     * @param bigDecimalVal
     * @return 大写中文
     */
    public static String getChineseMoneyStringForBigDecimal(BigDecimal bigDecimalVal) {
        return getChineseMoneyStringForDoubleVal(bigDecimalVal
                .doubleValue());
    }
}
