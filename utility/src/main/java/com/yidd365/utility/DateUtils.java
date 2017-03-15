package com.yidd365.utility;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

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

    private DateUtils(){}

    public static long getEpochMilli(@NonNull LocalDateTime ldt, @NonNull String zoneId){
        assert ldt != null;
        assert StringUtils.isNullOrWhitespace(zoneId);

        return ldt.toInstant(ZoneOffset.of(zoneId)).toEpochMilli();
    }

    public static long getEpochMilli(@NonNull LocalDateTime ldt){
        assert ldt != null;
        return getEpochMilli(ldt, "Asia/Shanghai");
    }

    public static LocalDateTime convertToLocalDateTime(@NonNull Date date){
        assert date != null;
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return res;
    }

    public static LocalDate convertToLocalDate(@NonNull Date date){
        assert date != null;
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDate res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        return res;
    }

    public static LocalTime convertToLocalTime(@NonNull Date date){
        assert date != null;
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
        return res;
    }

    public static Date convertToDate(@NonNull LocalDateTime ldt){
        assert ldt != null;
        Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
        Date res = new Date(instant.toEpochMilli());
        return res;
    }

    public static Date converToDate(@NonNull LocalDate ld){
        assert ld != null;
        Instant instant = ld.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date res = new Date(instant.toEpochMilli());
        return res;
    }

    public static String formatDateTime(@Nullable LocalDateTime ldt, @NonNull String pattern){
        assert StringUtils.isNullOrWhitespace(pattern);
        if(ldt == null)
            return "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return ldt.format(formatter);
    }

    public static String formatDateTime(@Nullable Date date, @NonNull String pattern){
        assert StringUtils.isNullOrWhitespace(pattern);
        if(date == null)
            return "";

        return formatDateTime(convertToLocalDateTime(date), pattern);
    }

    public static String formatDateTime(@Nullable LocalDateTime ldt){
        return formatDateTime(ldt, DEFAULT_DATE_TIME_FORMATTER_PATTERN);
    }

    public static String formatDateTime(@Nullable Date date){
        return formatDateTime(convertToLocalDateTime(date));
    }

    public static String formatDate(@Nullable LocalDate date, @NonNull String pattern){
        assert StringUtils.isNullOrWhitespace(pattern);
        if(date == null)
            return "";

        DateTimeFormatter formatter = getFormatter(pattern);
        return date.format(formatter);
    }

    public static String formatDate(@Nullable Date date, @NonNull String pattern){
        assert StringUtils.isNullOrWhitespace(pattern);
        if(date == null)
            return "";

        return formatDate(convertToLocalDate(date), pattern);
    }

    public static String formatDate(@Nullable LocalDate date){
        return formatDate(date, DEFAULT_DATE_FORMATTER_PATTERN);
    }

    public static String formatDate(@NonNull Date date) {
        return formatDate(convertToLocalDate(date));
    }

    public static String formatTime(@Nullable LocalTime time, @NonNull String pattern) {
        assert StringUtils.isNullOrWhitespace(pattern);
        if(time == null)
            return "";

        DateTimeFormatter formatter = getFormatter(pattern);
        return time.format(formatter);
    }

    public static String formatTime(@Nullable Date date, @NonNull String pattern) {
        assert StringUtils.isNullOrWhitespace(pattern);
        if(date == null)
            return "";

        return formatTime(convertToLocalTime(date), pattern);
    }

    public static String formatTime(@Nullable LocalTime date) {
        return formatTime(date, DEFAULT_TIME_FORMATTER_PATTERN);
    }

    public static String formatTime(@Nullable Date date) {
        return formatTime(convertToLocalTime(date));
    }

    public static LocalDateTime fromDateTimeStr(@NonNull String str,@NonNull String pattern){
        assert StringUtils.isNullOrWhitespace(str);
        assert StringUtils.isNullOrWhitespace(pattern);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(str, formatter);
    }

    public static LocalDateTime fromDateTimeStr(@NonNull String str){
        assert StringUtils.isNullOrWhitespace(str);
        return fromDateTimeStr(str, DEFAULT_DATE_TIME_FORMATTER_PATTERN);
    }

    public static LocalDate fromDateStr(@NonNull String str,@NonNull String pattern){
        assert StringUtils.isNullOrWhitespace(str);
        assert StringUtils.isNullOrWhitespace(pattern);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(str, formatter);
    }

    public static LocalDate fromDateStr(@NonNull String str){
        assert StringUtils.isNullOrWhitespace(str);
        return fromDateStr(str, DEFAULT_DATE_FORMATTER_PATTERN);
    }

    public static LocalTime fromTimeStr(@NonNull String str, @NonNull String pattern){
        assert StringUtils.isNullOrWhitespace(str);
        assert StringUtils.isNullOrWhitespace(pattern);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalTime.parse(str, formatter);
    }

    public static LocalTime fromTimeStr(@NonNull String str){
        assert StringUtils.isNullOrWhitespace(str);
        return fromTimeStr(str, DEFAULT_DATE_TIME_FORMATTER_PATTERN);
    }

    public static String friendlyTime(@Nullable Date date){
        if(date == null)
            return "";
        return friendlyTime(convertToLocalDateTime(date));
    }

    public static DateTimeFormatter getFormatter(String pattern){
        DateTimeFormatter dateTimeFormatter = null;
        if(formatters == null) {
           formatters = new android.support.v4.util.ArrayMap<>();
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
    public static String friendlyTime(@Nullable LocalDateTime ldt) {
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
