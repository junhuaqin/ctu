package com.santaba.server.util.workunit;

import java.util.Objects;

/**
 * Created by Robert Qin on 11/05/2018.
 */
public class GeneralJobNoInputAndReturn implements Job {
    private final Job.PreCheck _preCheck;
    private final Jobs.ProcedureNoReturn _procedure;
    private final Job.Notification _notification;

    GeneralJobNoInputAndReturn(Job.PreCheck preCheck, Jobs.ProcedureNoReturn procedure, Job.Notification notification) {
        Objects.requireNonNull(preCheck);
        Objects.requireNonNull(procedure);
        Objects.requireNonNull(notification);

        _preCheck = preCheck;
        _procedure = procedure;
        _notification = notification;
    }

    public GeneralJobNoInputAndReturn onComplete(Job.Notification notification) {
        return new GeneralJobNoInputAndReturn(_preCheck, _procedure, notification);
    }

    public GeneralJobNoInputAndReturn then(PreChecker job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runNoInputAndReturn(this::run)
                .onComplete(this::onComplete);
    }

    public GeneralJobNoInputAndReturn then(GeneralJobNoInputAndReturn job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runNoInputAndReturn(() -> {
                    run();
                    job.run();
                })
                .onComplete(() -> {
                    onComplete();
                    job.onComplete();
                });
    }

    public DBJobNoInputAndReturn then(DBJobNoInputAndReturn job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInputAndReturn(conn -> {
                    run();
                    job.run(conn);
                })
                .onCommitted(() -> {
                    onComplete();
                    job.onCommitted();
                });
    }

    public <R> GeneralJobNoInput<R> then(GeneralJobNoInput<R> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runNoInput(() -> {
                    run();
                    return job.run();
                })
                .onComplete(r -> {
                    onComplete();
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
                    run();
                    return job.run(conn);
                })
                .onCommitted(r -> {
                    onComplete();
                    job.onCommitted();
                });
    }

    public void execute() throws Exception {
        preCheck();
        run();
        onComplete();
    }

    void preCheck() throws Exception {
        _preCheck.apply();
    }

    void run() throws Exception {
        _procedure.apply();
    }

    void onComplete() {
        _notification.apply();
    }
}
