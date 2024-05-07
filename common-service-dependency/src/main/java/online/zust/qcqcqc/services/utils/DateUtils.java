package online.zust.qcqcqc.services.utils;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Andy
 * @date 2023-8-11 011 13:15
 * 日期工具类
 */
@Slf4j
public class DateUtils {
    private static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter YMDHMS_FORMATTER = DateTimeFormatter.ofPattern(YMDHMS);

    private static final String YMD = "yyyy-MM-dd";

    private static final DateTimeFormatter YMD_FORMATTER = DateTimeFormatter.ofPattern(YMD);


    /**
     * 将日期格式化为默认格式
     *
     * @param date 日期
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String dateToString(Date date) {
        try {
            SimpleDateFormat f = new SimpleDateFormat(YMDHMS);
            return f.format(date);
        } catch (Exception e) {
            log.info("日期转换失败");
        }
        return null;
    }

    /**
     * 将日期格式化为无时间格式
     *
     * @param date 日期
     * @return yyyy-MM-dd
     */
    public static String dateToStringWithoutTime(Date date) {
        try {
            SimpleDateFormat f = new SimpleDateFormat(YMD);
            return f.format(date);
        } catch (Exception e) {
            log.info("日期转换失败");
        }
        return null;
    }


    /**
     * 将String转换为Date，String格式为yyyy-MM-dd HH:mm:ss或者yyyy-MM-dd
     */
    public static Date stringToDate(String dateString) {
        try {
            SimpleDateFormat f = new SimpleDateFormat(YMDHMS);
            return f.parse(dateString);
        } catch (Exception e) {
            try {
                SimpleDateFormat f = new SimpleDateFormat(YMD);
                return f.parse(dateString);
            } catch (ParseException ex) {
                log.info("日期转换失败");
            }
        }
        return null;

    }

    /**
     * 处理endTime，如果endTime为当天的0点，加上24*60*60-1秒，使得endTime为当天的23:59:59
     */
    private static Date handleEndTime(Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        if (calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.SECOND) == 0) {
            calendar.add(Calendar.SECOND, 24 * 60 * 60 - 1);
            end = calendar.getTime();
        }
        return end;
    }

    /**
     * 将String转换为Date，String格式为yyyy-MM-dd HH:mm:ss或者yyyy-MM-dd <br>
     * 如果endTime为当天的0点，加上24*60*60-1秒，使得endTime为当天的23:59:59
     * 如果String为空或者null，则对应的Date为null
     *
     * @param startTime yyyy-MM-dd HH:mm:ss或者yyyy-MM-dd
     * @param endTime   yyyy-MM-dd HH:mm:ss或者yyyy-MM-dd
     * @return List<Date>，第一个元素为startTime，第二个元素为endTime
     */
    public static List<Date> stringToDateList(String startTime, String endTime) {
        Date start = null;
        Date end = null;

        if (StringUtils.isNotEmpty(startTime)) {
            start = DateUtils.stringToDate(startTime);
        }

        if (StringUtils.isNotEmpty(endTime)) {
            // 转化为Date
            end = DateUtils.stringToDate(endTime);
            // 处理endTime
            end = handleEndTime(end);
        }
        return Arrays.asList(start, end);
    }


    /**
     * 清除时间信息
     */
    private static void clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


    public static String dateToDateString(Date createdTime) {
        // 显示当天的日期，不显示时间，如果是当天的0点，则显示前一天的日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdTime);
        clearTime(calendar);
        if (calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.SECOND) == 0) {
            calendar.add(Calendar.DATE, -1);
        }
        return dateToStringWithoutTime(calendar.getTime());
    }

    public static boolean isBetweenToday(Date date, Date date1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        clearTime(calendar);
        Date start = calendar.getTime();
        calendar.setTime(date1);
        clearTime(calendar);
        Date end = calendar.getTime();
        Date now = new Date();
        return now.after(start) && now.before(end);
    }

    public static Date addOneDay(Date val2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(val2);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
