package com.qfg.ctu.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Administrator on 2016/8/3.
 */
public class DateTimeUtil {
    public static LocalDateTime nowInBeiJing() {
        return LocalDateTime.now(Constant.BEIJING_ZONE);
    }

    public static Long getMilli(LocalDateTime time) {
        return time.toInstant(Constant.BEIJING_ZONE).toEpochMilli();
    }

    public static LocalDateTime getLocalDateTime(Long milli) {
        return LocalDateTime.ofEpochSecond(milli/1000, 0, Constant.BEIJING_ZONE);
    }

    public static Timestamp mapLocalDateTime2Timestamp(LocalDateTime time) {
        return new Timestamp(getMilli(time));
    }

    public static LocalDateTime mapTimestamp2LocalDateTime(Timestamp time) {
        return time.toLocalDateTime();
    }
}
