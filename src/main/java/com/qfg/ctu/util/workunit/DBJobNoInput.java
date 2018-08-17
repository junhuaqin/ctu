package com.santaba.server.util.workunit;

import com.santaba.common.util.DbUtil;

import java.sql.Connection;
import java.util.Objects;
import java.util.function.Consumer;

import static com.santaba.server.util.workunit.Functions.consumeNothing;

/**
 * Created by Robert Qin on 11/05/2018.
 */
public class DBJobNoInput<R> implements Job {
    private R _result;
    private final PreCheck _preCheck;
    private final Jobs.DBProcedure<R> _procedure;
    private final Consumer<R> _notification;

    DBJobNoInput(PreCheck preCheck, Jobs.DBProcedure<R> procedure, Consumer<R> notification) {
        Objects.requireNonNull(preCheck);
        Objects.requireNonNull(procedure);
        Objects.requireNonNull(notification);

        this._preCheck = preCheck;
        this._procedure = procedure;
        this._notification = notification;
    }

    public DBJobNoInput<R> onCommitted(Consumer<R> notification) {
        return new DBJobNoInput<>(_preCheck, _procedure, notification);
    }

    public DBJobNoInput<R> then(PreChecker job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInput(this::run)
                .onCommitted(r -> onCommitted());
    }

    public DBJobNoInputAndReturn then(DBJobNoInputAndReturn job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInputAndReturn(conn -> {
                    run(conn);
                    job.run(conn);
                })
                .onCommitted(() -> {
                    onCommitted();
                    job.onCommitted();
                });
    }

    public DBJobNoInputAndReturn then(GeneralJobNoInputAndReturn job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInputAndReturn(conn -> {
                    run(conn);
                    job.run();
                })
                .onCommitted(() -> {
                    onCommitted();
                    job.onComplete();
                });
    }

    public <U> DBJobNoInput<U> then(DBJobNoInput<U> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInput(conn -> {
                    run(conn);
                    return job.run(conn);
                })
                .onCommitted(r -> {
                    onCommitted();
                    job.onCommitted();
                });
    }

    public <U> DBJobNoInput<U> then(GeneralJobNoInput<U> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInput(conn -> {
                    run(conn);
                    return job.run();
                })
                .onCommitted(r -> {
                    onCommitted();
                    job.onComplete();
                });
    }

    public DBJobNoInputAndReturn then(DBJobNoReturn<R> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInputAndReturn(conn -> {
                    R r = run(conn);
                    job.run(conn, r);
                })
                .onCommitted(() -> {
                    onCommitted();
                    job.onCommitted();
                });
    }

    public DBJobNoInputAndReturn then(GeneralJobNoReturn<R> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInputAndReturn(conn -> {
                    R r = run(conn);
                    job.run(r);
                })
                .onCommitted(() -> {
                    onCommitted();
                    job.onComplete();
                });
    }

    public <U> DBJobNoInput<U> then(DBJob<R, U> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInput(conn -> {
                    R r = run(conn);
                    return job.run(conn, r);
                })
                .onCommitted(r -> {
                    onCommitted();
                    job.onCommitted();
                });
    }

    public <U> DBJobNoInput<U> then(GeneralJob<R, U> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInput(conn -> {
                    R r = run(conn);
                    return job.run(r);
                })
                .onCommitted(r -> {
                    onCommitted();
                    job.onComplete();
                });
    }

    public R execute() throws Exception {
        return Jobs.execute(() -> DbUtil.getConnection(false),
                DbUtil::closeQuietly,
                this::preCheck,
                this::run,
                r -> onCommitted());
    }

    public R execute(Connection conn) throws Exception {
        Objects.requireNonNull(conn);
        return Jobs.execute(() -> conn,
                consumeNothing(),
                this::preCheck,
                this::run,
                r -> onCommitted());
    }

    void preCheck() throws Exception {
        _preCheck.apply();
    }

    R run(Connection conn) throws Exception {
        _result = _procedure.apply(conn);
        return _result;
    }

    void onCommitted() {
        _notification.accept(_result);
    }
}
