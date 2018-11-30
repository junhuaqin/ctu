package com.santaba.server.util.workunit;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.santaba.server.util.workunit.Functions.ignoreAnything;

/**
 * Created by Robert Qin on 18/09/2018.
 */
class JobImplLinker<T, R, U> extends JobImpl<T, U> {
    private final JobImpl<T, R> _head;
    private final JobImpl<? super R, U> _next;
    private final Consumer<? super U> _notification;
    private final Consumer<? super Exception> _onError;

    JobImplLinker(JobImpl<T, R> head, JobImpl<? super R, U> next) {
        this(head, next, ignoreAnything(), ignoreAnything());
    }

    private JobImplLinker(JobImpl<T, R> head,
                          JobImpl<? super R, U> next,
                          Consumer<? super U> notification,
                          Consumer<? super Exception> onError) {
        Objects.requireNonNull(head);
        Objects.requireNonNull(next);
        Objects.requireNonNull(notification);
        Objects.requireNonNull(onError);

        this._head = head;
        this._next = next;
        this._notification = notification;
        this._onError = onError;
    }

    @Override
    protected JobImpl<T, U> _onComplete(Consumer<? super U> notification) {
        Objects.requireNonNull(notification);
        return new JobImplLinker<>(
                _head,
                _next,
                u -> {
                    _notification.accept(u);
                    notification.accept(u);
                },
                _onError);
    }

    @Override
    protected JobImpl<T, U> _onError(Consumer<? super Exception> onError) {
        Objects.requireNonNull(onError);
        return new JobImplLinker<>(
                _head,
                _next,
                _notification,
                e -> {
                    _onError.accept(e);
                    onError.accept(e);
                });
    }

    @Override
    protected <U1> JobImpl<T, U1> _then(JobImpl<? super U, U1> other) {
        return new JobImplLinker<>(this, other);
    }

    @Override
    protected JobImpl<Object, U> _bind(Supplier<? extends T> supplier) {
        return new JobImplLinker<>(_head._bind(supplier), _next);
    }

    @Override
    protected void _preCheck(ExecuteContext context) throws Exception {
        _head._preCheck(context);
        _next._preCheck(context);
    }

    @Override
    protected U _execute(ExecuteContext context, T t) throws Exception {
        U u = _next._execute(context, _head._execute(context, t));
        context.offer(u);
        return u;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void _onComplete(ExecuteContext context) {
        _head._onComplete(context);
        _next._onComplete(context);
        _notification.accept((U)context.poll());
    }

    @Override
    protected void _onError(ExecuteContext context, Exception e) {
        _head._onError(context, e);
        _next._onError(context, e);
        _onError.accept(e);
    }
}
