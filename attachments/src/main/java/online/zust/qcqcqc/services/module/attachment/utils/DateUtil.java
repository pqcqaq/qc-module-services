package online.zust.qcqcqc.services.module.attachment.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qcqcqc
 * Date: 2024/4/12
 * Time: 10:31
 */
public class DateUtil {
    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date date() {
        return new Date();
    }
}
