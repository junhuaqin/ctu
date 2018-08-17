package com.santaba.server.util.workunit;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Robert Qin on 29/03/2018.
 */
public class GeneralJob<T, R> implements Job {
    private final PreCheck _preCheck;
    private final Jobs.ConsumerProcedure<T, R> _procedure;
    private final Consumer<R> _notification;
    private R _result = null;

    GeneralJob(PreCheck preCheck, Jobs.ConsumerProcedure<T, R> procedure, Consumer<R> notification) {
        Objects.requireNonNull(preCheck);
        Objects.requireNonNull(procedure);
        Objects.requireNonNull(notification);

        this._preCheck = preCheck;
        this._procedure = procedure;
        this._notification = notification;
    }

    public GeneralJob<T, R> onComplete(Consumer<R> notification) {
        return new GeneralJob<>(_preCheck, _procedure, notification);
    }

    public GeneralJob<T, R> then(PreChecker job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .run(this::run)
                .onComplete(r -> onComplete());
    }

    public GeneralJobNoReturn<T> then(GeneralJobNoInputAndReturn job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T>runNoReturn(t -> {
                    run(t);
                    job.run();
                })
                .onComplete(() -> {
                    onComplete();
                    job.onComplete();
                });
    }

    public DBJobNoReturn<T> then(DBJobNoInputAndReturn job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T>runOnDBNoReturn((conn, t) -> {
                    run(t);
                    job.run(conn);
                })
                .onCommitted(() -> {
                    onComplete();
                    job.onCommitted();
                });
    }

    public <U> GeneralJob<T, U> then(GeneralJobNoInput<U> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T, U>run(t -> {
                    run(t);
                    return job.run();
                })
                .onComplete(r -> {
                    onComplete();
                    job.onComplete();
                });
    }

    public <U> DBJob<T, U> then(DBJobNoInput<U> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T, U>runOnDB((conn, t) -> {
                    run(t);
                    return job.run(conn);
                })
                .onCommitted(r -> {
                    onComplete();
                    job.onCommitted();
                });
    }

    public GeneralJobNoReturn<T> then(GeneralJobNoReturn<R> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T>runNoReturn(t -> {
                    R r = run(t);
                    job.run(r);
                })
                .onComplete(() -> {
                    onComplete();
                    job.onComplete();
                });
    }

    public DBJobNoReturn<T> then(DBJobNoReturn<R> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T>runOnDBNoReturn((conn, t) -> {
                    R r = run(t);
                    job.run(conn, r);
                })
                .onCommitted(() -> {
                    onComplete();
                    job.onCommitted();
                });
    }

    public <U> GeneralJob<T, U> then(GeneralJob<R, U> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T, U>run(t -> {
                    R r = run(t);
                    return job.run(r);
                })
                .onComplete(r -> {
                    onComplete();
                    job.onComplete();
                });
    }

    public <U> DBJob<T, U> then(DBJob<R, U> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T, U>runOnDB((conn, t) -> {
                    R r = run(t);
                    return job.run(conn, r);
                })
                .onCommitted(r -> {
                    onComplete();
                    job.onCommitted();
                });
    }

    public GeneralJobNoInput<R> bind(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);

        return Jobs.preCheck(this::preCheck)
                .runNoInput(() -> run(supplier.get()))
                .onComplete(r -> onComplete());
    }

    public R execute(T t) throws Exception {
        preCheck();
        R r = run(t);
        onComplete();
        return r;
    }

    void preCheck() throws Exception {
        _preCheck.apply();
    }

    R run(T t) throws Exception {
        _result = _procedure.apply(t);
        return _result;
    }

    void onComplete() {
        _notification.accept(_result);
    }
}
