package com.santaba.server.util.workunit;

import java.util.function.Consumer;

/**
 * Created by Robert Qin on 29/03/2018.
 */
public class Functions {
    public static Job.PreCheck checkNothing() {
        return () -> {};
    }

    public static <T> Consumer<T> ignoreAnything() {
        return n -> {};
    }

    public static Job.Notification nothingCared() {
        return () -> {};
    }
}
