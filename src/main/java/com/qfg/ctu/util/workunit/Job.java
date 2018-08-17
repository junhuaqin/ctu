package com.santaba.server.util.workunit;

/**
 * Represents a work unit, which should be completed totally or dropped totally.
 * Created by Robert Qin on 29/03/2018.
 */
public interface Job {
    /**
     * Represents an operation that preCheck if some condition satisfied and returns no
     * result. it is expected to operate via none side-effects.
     *
     * <p>This is a functional interface</a>
     * whose functional method is {@link #apply()}.
     *
     */
    @FunctionalInterface
    interface PreCheck {
        void apply() throws Exception;
    }

    /**
     * Represents an operation that notify something happened and returns no
     * result. it is expected to operate via side-effects.
     *
     * <p>This is a functional interface</a>
     * whose functional method is {@link #apply()}.
     *
     */
    @FunctionalInterface
    interface Notification {
        void apply();
    }
}
