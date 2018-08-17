package com.santaba.server.util.workunit;

import com.santaba.common.util.DbUtil;

import java.sql.Connection;
import java.util.Objects;
import java.util.function.Supplier;

import static com.santaba.server.util.workunit.Functions.consumeNothing;

/**
 * Created by Robert Qin on 11/05/2018.
 */
public class DBJobNoReturn<T> implements Job {
    private final PreCheck _preCheck;
    private final Jobs.ConsumerDBProcedureNoReturn<T> _procedure;
    private final Notification _notification;

    DBJobNoReturn(PreCheck preCheck, Jobs.ConsumerDBProcedureNoReturn<T> procedure, Notification notification) {
        Objects.requireNonNull(preCheck);
        Objects.requireNonNull(procedure);
        Objects.requireNonNull(notification);

        this._preCheck = preCheck;
        this._procedure = procedure;
        this._notification = notification;
    }

    public DBJobNoReturn<T> onCommitted(Notification notification) {
        return new DBJobNoReturn<>(_preCheck, _procedure, notification);
    }

    public DBJobNoReturn<T> then(PreChecker job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoReturn(this::run)
                .onCommitted(this::onCommitted);
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

    public DBJobNoInputAndReturn bind(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);

        return Jobs.preCheck(this::preCheck)
                .runOnDBNoInputAndReturn(conn -> run(conn, supplier.get()))
                .onCommitted(this::onCommitted);
    }

    public void execute(T t) throws Exception {
        Jobs.execute(() -> DbUtil.getConnection(false),
                DbUtil::closeQuietly,
                this::preCheck,
                conn -> {
                    run(conn, t);
                    return 0;
                },
                r -> onCommitted());
    }

    public void execute(Connection conn, T t) throws Exception {
        Objects.requireNonNull(conn);
        Jobs.execute(() -> conn,
                consumeNothing(),
                this::preCheck,
                connection -> {
                    run(connection, t);
                    return 0;
                },
                r -> onCommitted());
    }

    void preCheck() throws Exception {
        _preCheck.apply();
    }

    void run(Connection conn, T t) throws Exception {
        _procedure.apply(conn, t);
    }

    void onCommitted() {
        _notification.apply();
    }
}
