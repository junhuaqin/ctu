package com.santaba.server.util.workunit;

import io.reactivex.functions.BiFunction;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Robert Qin on 18/09/2018.
 */
class JobImplLeaf<T, R> extends JobImpl<T, R> {
    private final Job.PreCheck _preCheck;
    private final BiFunction<ExecuteContext, T, R> _procedure;
    private final Consumer<? super R> _notification;
    private final Consumer<? super Exception> _onError;

    JobImplLeaf(Job.PreCheck preCheck,
                BiFunction<ExecuteContext, T, R> procedure,
                Consumer<? super R> notification,
                Consumer<? super Exception> onError) {
        Objects.requireNonNull(preCheck);
        Objects.requireNonNull(procedure);
        Objects.requireNonNull(notification);
        Objects.requireNonNull(onError);

        _preCheck = preCheck;
        _procedure = procedure;
        _notification = notification;
        _onError = onError;
    }

    @Override
    protected JobImpl<T, R> _onComplete(Consumer<? super R> notification) {
        Objects.requireNonNull(notification);
        return new JobImplLeaf<>(
                _preCheck,
                _procedure,
                r -> {
                    _notification.accept(r);
                    notification.accept(r);
                },
                _onError);
    }

    @Override
    protected JobImpl<T, R> _onError(Consumer<? super Exception> onError) {
        Objects.requireNonNull(onError);
        return new JobImplLeaf<>(
                _preCheck,
                _procedure,
                _notification,
                e -> {
                    _onError.accept(e);
                    onError.accept(e);
                });
    }

    @Override
    protected <U> JobImpl<T, U> _then(JobImpl<? super R, U> other) {
        return new JobImplLinker<>(this, other);
    }

    @Override
    protected JobImpl<Object, R> _bind(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        return new JobImplLeaf<>(
                _preCheck,
                (context, o) -> _procedure.apply(context, supplier.get()),
                _notification,
                _onError);
    }

    @Override
    protected void _preCheck(ExecuteContext context) throws Exception {
        _preCheck.apply();
    }

    @Override
    protected R _execute(ExecuteContext context, T t) throws Exception {
        R r = _procedure.apply(context, t);
        context.offer(r);
        return r;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void _onComplete(ExecuteContext context) {
        _notification.accept((R)context.poll());
    }

    @Override
    protected void _onError(ExecuteContext context, Exception e) {
        _onError.accept(e);
    }
}
