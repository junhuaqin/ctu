package com.santaba.server.util.workunit;

import com.santaba.common.util.DbUtil;

import java.sql.Connection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.santaba.server.util.workunit.Functions.consumeNothing;

/**
 * Created by Robert Qin on 29/03/2018.
 */
public class DBJob<T, R> implements Job {
    private final PreCheck _preCheck;
    private final Jobs.ConsumerDBProcedure<T, R> _procedure;
    private final Consumer<R> _notification;
    private R _result = null;

    DBJob(PreCheck preCheck, Jobs.ConsumerDBProcedure<T, R> procedure, Consumer<R> notification) {
        Objects.requireNonNull(preCheck);
        Objects.requireNonNull(procedure);
        Objects.requireNonNull(notification);

        this._preCheck = preCheck;
        this._procedure = procedure;
        this._notification = notification;
    }

    public DBJob<T, R> onCommitted(Consumer<R> notification) {
        return new DBJob<>(_preCheck, _procedure, notification);
    }

    public DBJob<T, R> then(PreChecker job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDB(this::run)
                .onCommitted(r -> onCommitted());
    }

    public DBJobNoReturn<T> then(DBJobNoInputAndReturn job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T>runOnDBNoReturn((conn, t) -> {
                    run(conn, t);
                    job.run(conn);
                })
                .onCommitted(() -> {
                    onCommitted();
                    job.onCommitted();
                });
    }

    public DBJobNoReturn<T> then(GeneralJobNoInputAndReturn job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T>runOnDBNoReturn((conn, t) -> {
                    run(conn, t);
                    job.run();
                })
                .onCommitted(() -> {
                    onCommitted();
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
                    run(conn, t);
                    return job.run(conn);
                })
                .onCommitted(r -> {
                    onCommitted();
                    job.onCommitted();
                });
    }

    public <U> DBJob<T, U> then(GeneralJobNoInput<U> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T, U>runOnDB((conn, t) -> {
                    run(conn, t);
                    return job.run();
                })
                .onCommitted(r -> {
                    onCommitted();
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
                    R r = run(conn, t);
                    job.run(conn, r);
                })
                .onCommitted(() -> {
                    onCommitted();
                    job.onCommitted();
                });
    }

    public DBJobNoReturn<T> then(GeneralJobNoReturn<R> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T>runOnDBNoReturn((conn, t) -> {
                    R r = run(conn, t);
                    job.run(r);
                })
                .onCommitted(() -> {
                    onCommitted();
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
                    R r = run(conn, t);
                    return job.run(conn, r);
                })
                .onCommitted(r -> {
                    onCommitted();
                    job.onCommitted();
                });
    }

    public <U> DBJob<T, U> then(GeneralJob<R, U> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .<T, U>runOnDB((conn, t) -> {
                    R r = run(conn, t);
                    return job.run(r);
                })
                .onCommitted(r -> {
                    onCommitted();
                    job.onComplete();
                });
    }

    public DBJobNoInput<R> bind(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);

        return Jobs.preCheck(this::preCheck)
                .runOnDBNoInput(conn -> run(conn, supplier.get()))
                .onCommitted(r -> onCommitted());
    }

    public R execute(T t) throws Exception {
        return Jobs.execute(() -> DbUtil.getConnection(false),
                DbUtil::closeQuietly,
                this::preCheck,
                conn -> run(conn, t),
                r -> onCommitted());
    }

    public R execute(Connection conn, T t) throws Exception {
        Objects.requireNonNull(conn);
        return Jobs.execute(() -> conn,
                consumeNothing(),
                this::preCheck,
                connection -> run(connection, t),
                r -> onCommitted());
    }

    void preCheck() throws Exception {
        _preCheck.apply();
    }

    R run(Connection conn, T t) throws Exception {
        _result = _procedure.apply(conn, t);
        return _result;
    }

    void onCommitted() {
        _notification.accept(_result);
    }
}
