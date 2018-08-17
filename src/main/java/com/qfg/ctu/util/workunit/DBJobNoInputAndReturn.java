package com.santaba.server.util.workunit;

import com.santaba.common.util.DbUtil;

import java.sql.Connection;
import java.util.Objects;

import static com.santaba.server.util.workunit.Functions.consumeNothing;

/**
 * Created by Robert Qin on 11/05/2018.
 */
public class DBJobNoInputAndReturn implements Job {
    private final PreCheck _preCheck;
    private final Jobs.DBProcedureNoReturn _procedure;
    private final Notification _notification;

    DBJobNoInputAndReturn(PreCheck preCheck, Jobs.DBProcedureNoReturn procedure, Notification notification) {
        Objects.requireNonNull(preCheck);
        Objects.requireNonNull(procedure);
        Objects.requireNonNull(notification);

        _preCheck = preCheck;
        _procedure = procedure;
        _notification = notification;
    }
    public DBJobNoInputAndReturn onCommitted(Job.Notification notification) {
        return new DBJobNoInputAndReturn(_preCheck, _procedure, notification);
    }

    public DBJobNoInputAndReturn then(PreChecker job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInputAndReturn(this::run)
                .onCommitted(this::onCommitted);
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

    public <R> DBJobNoInput<R> then(DBJobNoInput<R> job) {
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

    public <R> DBJobNoInput<R> then(GeneralJobNoInput<R> job) {
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

    public void execute() throws Exception {
        Jobs.execute(() -> DbUtil.getConnection(false),
                DbUtil::closeQuietly,
                this::preCheck,
                conn -> {
                    run(conn);
                    return 0;
                },
                r -> onCommitted());
    }

    public void execute(Connection conn) throws Exception {
        Objects.requireNonNull(conn);
        Jobs.execute(() -> conn,
                consumeNothing(),
                this::preCheck,
                connection -> {
                    run(connection);
                    return 0;
                },
                r -> onCommitted());
    }

    void preCheck() throws Exception {
        _preCheck.apply();
    }

    void run(Connection conn) throws Exception {
        _procedure.apply(conn);
    }

    void onCommitted() {
        _notification.apply();
    }
}
