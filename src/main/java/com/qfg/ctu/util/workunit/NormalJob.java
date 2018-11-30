package com.santaba.server.util.workunit;

import io.reactivex.functions.BiFunction;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Robert Qin on 29/03/2018.
 */
public class NormalJob<T, R> implements Job {
    final JobImpl<T, R> _impl;

    NormalJob(PreCheck preCheck,
              BiFunction<ExecuteContext, T, R> procedure,
              Consumer<? super R> notification,
              Consumer<? super Exception> onError) {
        this(new JobImplLeaf<>(
                preCheck,
                procedure,
                notification,
                onError));
    }

    NormalJob(JobImpl<T, R> impl) {
        Objects.requireNonNull(impl);
        this._impl = impl;
    }

    public NormalJob<T, R> onComplete(Consumer<? super R> notification) {
        return new NormalJob<>(_impl._onComplete(notification));
    }

    public NormalJob<T, R> onError(Consumer<? super Exception> onError) {
        return new NormalJob<>(_impl._onError(onError));
    }

    public NormalJob<T, R> then(PreChecker job) {
        Objects.requireNonNull(job);
        return new NormalJob<>(_impl._then(job.trans2Identity()));
    }

    public NoReturnJob<T> then(NoInputAndReturnJob job) {
        Objects.requireNonNull(job);
        return new NoReturnJob<>(_impl._then(job._impl));
    }

    public <U> NormalJob<T, U> then(NoInputJob<U> job) {
        Objects.requireNonNull(job);
        return new NormalJob<>(_impl._then(job._impl));
    }

    public NoReturnJob<T> then(NoReturnJob<? super R> job) {
        Objects.requireNonNull(job);
        return new NoReturnJob<>(_impl._then(job._impl));
    }

    public <U> NormalJob<T, U> then(NormalJob<? super R, U> job) {
        Objects.requireNonNull(job);
        return new NormalJob<>(_impl._then(job._impl));
    }

    public NoInputJob<R> bind(Supplier<? extends T> supplier) {
        return new NoInputJob<>(_impl._bind(supplier));
    }

    public R execute(T t) throws Exception {
        return _impl.execute(t);
    }
}