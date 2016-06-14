package com.yidd365.utility;

import android.util.ArrayMap;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Period;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by orinchen on 16/4/5.
 */
public final class DateUtils {

    public static final String DEFAULT_TIME_ZONE_ID = "Asia/Shanghai";

    public static final long HOUR_MILLI_SECONDS = 60*60*1000;
    public static final long MINUTE_MILLI_SECONDS = 60*1000;

    public static final String DEFAULT_DATE_TIME_FORMATTER_PATTERN = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String DEFAULT_DATE_FORMATTER_PATTERN = "yyyy年MM月dd日";
    public static final String DEFAULT_TIME_FORMATTER_PATTERN = "HH时mm分ss秒";

    private static ArrayMap<String, DateTimeFormatter> formatters;

    private DateUtils(){
    }

    public static long getEpochMilli(LocalDateTime ldt, String zoneId){
        return ldt.toInstant(ZoneOffset.of(zoneId)).toEpochMilli();
    }

    public static long getEpochMilli(LocalDateTime ldt){
        return getEpochMilli(ldt, "Asia/Shanghai");
    }

    public static LocalDateTime convertToLocalDateTime(Date date){
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return res;
    }

    public static LocalDate convertToLocalDate(Date date){
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDate res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        return res;
    }

    public static LocalTime convertToLocalTime(Date date){
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
        return res;
    }

    public static Date convertToDate(LocalDateTime ldt){
        Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
        Date res = new Date(instant.toEpochMilli());
        return res;
    }

    public static Date converToDate(LocalDate ld){
        Instant instant = ld.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date res = new Date(instant.toEpochMilli());
        return res;
    }

    public static String formatDateTime(LocalDateTime ldt, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return ldt.format(formatter);
    }

    public static String formatDateTime(Date date, String pattern){
        return formatDateTime(convertToLocalDateTime(date), pattern);
    }

    public static String formatDateTime(LocalDateTime ldt){
        return formatDateTime(ldt, DEFAULT_DATE_TIME_FORMATTER_PATTERN);
    }

    public static String formatDateTime(Date date){
        return formatDateTime(convertToLocalDateTime(date));
    }

    public static String formatDate(LocalDate date, String pattern){
        DateTimeFormatter formatter = getFormatter(pattern);
        return date.format(formatter);
    }

    public static String formatDate(Date date, String pattern){
        return formatDate(convertToLocalDate(date), pattern);
    }

    public static String formatDate(LocalDate date){
        return formatDate(date, DEFAULT_DATE_FORMATTER_PATTERN);
    }

    public static String formatDate(Date date) {
        return formatDate(convertToLocalDate(date));
    }

    public static String formatTime(LocalTime date, String pattern) {
        DateTimeFormatter formatter = getFormatter(pattern);
        return date.format(formatter);
    }

    public static String formatTime(Date date, String pattern) {
        return formatTime(convertToLocalTime(date), pattern);
    }

    public static String formatTime(LocalTime date) {
        return formatTime(date, DEFAULT_TIME_FORMATTER_PATTERN);
    }

    public static String formatTime(Date date) {
        return formatTime(convertToLocalTime(date));
    }

    public static LocalDateTime fromDateTimeStr(String str, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(str, formatter);
    }

    public static LocalDateTime fromDateTimeStr(String str){
        return fromDateTimeStr(str, DEFAULT_DATE_TIME_FORMATTER_PATTERN);
    }

    public static LocalDate fromDateStr(String str, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(str, formatter);
    }

    public static LocalDate fromDateStr(String str){
        return fromDateStr(str, DEFAULT_DATE_FORMATTER_PATTERN);
    }

    public static LocalTime fromTimeStr(String str, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalTime.parse(str, formatter);
    }

    public static LocalTime fromTimeStr(String str){
        return fromTimeStr(str, DEFAULT_DATE_TIME_FORMATTER_PATTERN);
    }

    public static String friendlyTime(Date date){
        return friendlyTime(convertToLocalDateTime(date));
    }

    public static DateTimeFormatter getFormatter(String pattern){
        DateTimeFormatter dateTimeFormatter = null;
        if(formatters == null) {
           formatters = new ArrayMap<>();
        }

        if(!formatters.containsKey(pattern)){
            dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            formatters.put(pattern, dateTimeFormatter);
        }else {
            dateTimeFormatter = formatters.get(pattern);
            if(dateTimeFormatter == null){
                dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
                formatters.put(pattern, dateTimeFormatter);
            }
        }

        return dateTimeFormatter;
    }

    /**
     * 以友好的方式显示时间
     *
     * @param ldt
     * @return
     */
    public static String friendlyTime(LocalDateTime ldt) {
        if (ldt == null) {
            return "";
        }

        LocalDateTime now = LocalDateTime.now();
        boolean isFuture = ldt.isAfter(now);

        Period period = Period.between(ldt.toLocalDate(), now.toLocalDate());

        int months = period.getMonths();

        if(months > 6){
            return ldt.toString();
        }else if(months > 0){
            return months + " 个月" + (isFuture ? "后" : "前");
        }

        int days = period.getDays();
        if(days>1){
            return days + " 天" + (isFuture ? "后" : "前");
        }else if(days > 0){
            return (isFuture ? "明天" : "昨天");
        }

        long hour = Math.abs(ldt.getHour() - now.getHour());

        if(hour != 0){
            return hour + " 小时" + (isFuture ? "后" : "前");
        }

        long mins = Math.abs(ldt.getMinute() - now.getMinute());

        if(mins<=1){
            return (isFuture ? "马上" : "刚刚");
        }

        return mins + " 分钟" + (isFuture ? "后" : "前");
    }
}
