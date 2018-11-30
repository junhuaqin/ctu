package com.santaba.server.util.workunit;

import io.reactivex.functions.BiFunction;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by Robert Qin on 11/05/2018.
 */
public class NoInputAndReturnJob implements Job {
    final JobImpl<Object, Object> _impl;
    NoInputAndReturnJob(PreCheck preCheck, BiFunction<ExecuteContext, Object, Object> procedure, Notification notification, Consumer<? super Exception> onError) {
        this(new JobImplLeaf<>(
                preCheck,
                procedure,
                r -> notification.apply(),
                onError));
    }

    NoInputAndReturnJob(JobImpl<Object, Object> impl) {
        Objects.requireNonNull(impl);
        this._impl = impl;
    }

    public NoInputAndReturnJob onComplete(Job.Notification notification) {
        Objects.requireNonNull(notification);
        return new NoInputAndReturnJob(_impl._onComplete(r -> notification.apply()));
    }

    public NoInputAndReturnJob onError(Consumer<? super Exception> onError) {
        return new NoInputAndReturnJob(_impl._onError(onError));
    }

    public NoInputAndReturnJob then(PreChecker job) {
        Objects.requireNonNull(job);
        return new NoInputAndReturnJob(_impl._then(job.trans2Identity()));
    }

    public NoInputAndReturnJob then(NoInputAndReturnJob job) {
        Objects.requireNonNull(job);
        return new NoInputAndReturnJob(_impl._then(job._impl));
    }

    public <R> NoInputJob<R> then(NoInputJob<R> job) {
        Objects.requireNonNull(job);
        return new NoInputJob<>(_impl._then(job._impl));
    }

    public void execute() throws Exception {
        _impl.execute(null);
    }
}
