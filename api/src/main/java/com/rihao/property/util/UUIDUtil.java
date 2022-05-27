

package com.rihao.property.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * @author
 * @date 2018-11-08
 */
public class UUIDUtil {

    public static String get32UUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    public static String get36UUID(){
        return UUID.randomUUID().toString();
    }


    public static void main(String[] args) {
        /*String randomTokenId = RandomUtil.randomString(8).toUpperCase();
        System.out.println(randomTokenId);
        */
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = LocalDate.now().plusDays(10).atTime(23, 59, 59);
        System.out.println(now);
        System.out.println(endTime);
        System.out.println(endTime.until(now, ChronoUnit.DAYS));
    }
}
