package com.santaba.server.util.workunit;

import io.reactivex.functions.BiFunction;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Robert Qin on 11/05/2018.
 */
public class NoReturnJob<T> implements Job {
    final JobImpl<T, Object> _impl;
    NoReturnJob(PreCheck preCheck, BiFunction<ExecuteContext, T, Object> procedure, Notification notification, Consumer<? super Exception> onError) {
        this(new JobImplLeaf<>(
                preCheck,
                procedure,
                r -> notification.apply(),
                onError));
    }

    NoReturnJob(JobImpl<T, Object> impl) {
        Objects.requireNonNull(impl);
        this._impl = impl;
    }

    public NoReturnJob<T> onComplete(Notification notification) {
        Objects.requireNonNull(notification);
        return new NoReturnJob<>(_impl._onComplete(r -> notification.apply()));
    }

    public NoReturnJob<T> onError(Consumer<? super Exception> onError) {
        return new NoReturnJob<>(_impl._onError(onError));
    }

    public NoReturnJob<T> then(PreChecker job) {
        Objects.requireNonNull(job);
        return new NoReturnJob<>(_impl._then(job.trans2Identity()));
    }

    public NoReturnJob<T> then(NoInputAndReturnJob job) {
        Objects.requireNonNull(job);
        return new NoReturnJob<>(_impl._then(job._impl));
    }

    public <U> NormalJob<T, U> then(NoInputJob<U> job) {
        Objects.requireNonNull(job);
        return new NormalJob<>(_impl._then(job._impl));
    }

    public NoInputAndReturnJob bind(Supplier<? extends T> supplier) {
        return new NoInputAndReturnJob(_impl._bind(supplier));
    }

    public void execute(T t) throws Exception {
        _impl.execute(t);
    }
}
