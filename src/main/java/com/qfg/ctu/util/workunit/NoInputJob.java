package com.santaba.server.util.workunit;

import io.reactivex.functions.BiFunction;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by Robert Qin on 11/05/2018.
 */
public class NoInputJob<R> implements Job {
    final JobImpl<Object, R> _impl;
    NoInputJob(PreCheck preCheck, BiFunction<ExecuteContext, Object, R> procedure, Consumer<? super R> notification, Consumer<? super Exception> onError) {
        this(new JobImplLeaf<>(
                preCheck,
                procedure,
                notification,
                onError));
    }

    NoInputJob(JobImpl<Object, R> impl) {
        Objects.requireNonNull(impl);
        this._impl = impl;
    }

    public NoInputJob<R> onComplete(Consumer<? super R> notification) {
        return new NoInputJob<>(_impl._onComplete(notification));
    }

    public NoInputJob<R> onError(Consumer<? super Exception> onError) {
        return new NoInputJob<>(_impl._onError(onError));
    }

    public NoInputJob<R> then(PreChecker job) {
        Objects.requireNonNull(job);
        return new NoInputJob<>(_impl._then(job.trans2Identity()));
    }

    public NoInputAndReturnJob then(NoInputAndReturnJob job) {
        Objects.requireNonNull(job);
        return new NoInputAndReturnJob(_impl._then(job._impl));
    }

    public <U> NoInputJob<U> then(NoInputJob<U> job) {
        Objects.requireNonNull(job);
        return new NoInputJob<>(_impl._then(job._impl));
    }

    public NoInputAndReturnJob then(NoReturnJob<? super R> job) {
        Objects.requireNonNull(job);
        return new NoInputAndReturnJob(_impl._then(job._impl));
    }

    public <U> NoInputJob<U> then(NormalJob<? super R, U> job) {
        Objects.requireNonNull(job);
        return new NoInputJob<>(_impl._then(job._impl));
    }

    public R execute() throws Exception {
        return _impl.execute(null);
    }
}
