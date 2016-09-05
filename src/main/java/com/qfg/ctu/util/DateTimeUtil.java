package com.qfg.ctu.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by Administrator on 2016/8/3.
 */
public class DateTimeUtil {
    public static LocalDateTime nowInBeiJing() {
        return LocalDateTime.now();
    }

    public static Long getMilli(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime getLocalDateTime(Long milli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());
    }

    public static Timestamp mapLocalDateTime2Timestamp(LocalDateTime time) {
        return new Timestamp(getMilli(time));
    }

    public static LocalDateTime mapTimestamp2LocalDateTime(Timestamp time) {
        return time.toLocalDateTime();
    }
}
