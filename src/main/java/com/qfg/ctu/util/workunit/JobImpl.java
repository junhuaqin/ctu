package com.santaba.server.util.workunit;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Robert Qin on 18/09/2018.
 */
abstract class JobImpl<T, R> {
    protected abstract JobImpl<T, R> _onComplete(Consumer<? super R> notification);
    protected abstract JobImpl<T, R> _onError(Consumer<? super Exception> onError);

    protected abstract <U> JobImpl<T, U> _then(JobImpl<? super R, U> other);
    protected abstract JobImpl<Object, R> _bind(Supplier<? extends T> supplier);

    protected abstract void _preCheck(ExecuteContext context) throws Exception;
    protected abstract R _execute(ExecuteContext context, T t) throws Exception;
    protected abstract void _onComplete(ExecuteContext context);
    protected abstract void _onError(ExecuteContext context, Exception e);

    R execute(T t) throws Exception {
        return execute(new ExecuteContext(), t);
    }

    R execute(ExecuteContext context, T t) throws Exception {
        context.onStart();
        _preCheck(context);
        try {
            R r = _execute(context, t);
            context.onComplete();
            _onComplete(context);
            return r;
        }
        catch (Exception e) {
            context.onError();
            _onError(context, e);
            throw e;
        }
        finally {
            context.onFinalize();
        }
    }
}
