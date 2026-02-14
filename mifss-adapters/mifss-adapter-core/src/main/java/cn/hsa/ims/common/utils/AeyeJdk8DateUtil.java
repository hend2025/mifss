package cn.hsa.ims.common.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AeyeJdk8DateUtil {
    public static DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("yyyyMM");
    public static DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static DateTimeFormatter DAY = DateTimeFormatter.ofPattern("dd");
    public static DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static DateTimeFormatter DATETIME_MINUTE = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    public static DateTimeFormatter DATETIME_MINUTE_CN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 特殊时间格式2021-10-21T09:26:02.000+08:00
     */
    public static DateTimeFormatter DATETIME_ISO = DateTimeFormatter.ISO_DATE_TIME;

    public static Date convertToDate(LocalDateTime dateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        return Date.from(dateTime.atZone(zoneId).toInstant());
    }

    public static Date convertToDate(LocalDate date){
        ZonedDateTime zonedDateTime = date.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static LocalDateTime convertToLocalDateTime(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 解析字符串日期为Date
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return Date
     */
    public static Date parse(String dateStr, DateTimeFormatter pattern) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, pattern);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static void main(String args[]){
//        System.out.println(AeyeJdk8DateUtil.convertToDate(LocalDateTime.parse("20000101000000", AeyeJdk8DateUtil.DATETIME)));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(AeyeJdk8DateUtil.parse("2021-10-21T09:26:02.000+08:00", AeyeJdk8DateUtil.DATETIME_ISO)));
    }
}
