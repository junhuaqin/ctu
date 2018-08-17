package com.santaba.server.util.workunit;

import java.util.Objects;

import static com.santaba.server.util.workunit.Functions.consumeNothing;
import static com.santaba.server.util.workunit.Functions.nothingCared;

/**
 * Created by Robert Qin on 31/07/2018.
 */
public class PreChecker implements Job {
    private final PreCheck _preCheck;

    public PreChecker(PreCheck preCheck) {
        Objects.requireNonNull(preCheck);
        this._preCheck = preCheck;
    }


    public DBJobNoInputAndReturn runOnDBNoInputAndReturn(Jobs.DBProcedureNoReturn procedure) {
        return new DBJobNoInputAndReturn(_preCheck, procedure, nothingCared());
    }

    public <R> DBJobNoInput<R> runOnDBNoInput(Jobs.DBProcedure<R> procedure) {
        return new DBJobNoInput<>(_preCheck, procedure, consumeNothing());
    }

    public <T> DBJobNoReturn<T> runOnDBNoReturn(Jobs.ConsumerDBProcedureNoReturn<T> procedure) {
        return new DBJobNoReturn<>(_preCheck, procedure, nothingCared());
    }

    public <T, R> DBJob<T, R> runOnDB(Jobs.ConsumerDBProcedure<T, R> procedure) {
        return new DBJob<>(_preCheck, procedure, consumeNothing());
    }

    public GeneralJobNoInputAndReturn runNoInputAndReturn(Jobs.ProcedureNoReturn procedure) {
        return new GeneralJobNoInputAndReturn(_preCheck, procedure, nothingCared());
    }

    public <R> GeneralJobNoInput<R> runNoInput(Jobs.Procedure<R> procedure) {
        return new GeneralJobNoInput<>(_preCheck, procedure, consumeNothing());
    }

    public <T> GeneralJobNoReturn<T> runNoReturn(Jobs.ConsumerProcedureNoReturn<T> procedure) {
        return new GeneralJobNoReturn<>(_preCheck, procedure, nothingCared());
    }

    public <T, R> GeneralJob<T, R> run(Jobs.ConsumerProcedure<T, R> procedure) {
        return new GeneralJob<>(_preCheck, procedure, consumeNothing());
    }

    public PreChecker then(PreChecker job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                });
    }

    public DBJobNoInputAndReturn then(DBJobNoInputAndReturn job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInputAndReturn(job::run)
                .onCommitted(job::onCommitted);
    }

    public GeneralJobNoInputAndReturn then(GeneralJobNoInputAndReturn job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runNoInputAndReturn(job::run)
                .onComplete(job::onComplete);
    }

    public <R> DBJobNoInput<R> then(DBJobNoInput<R> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoInput(job::run)
                .onCommitted(r -> job.onCommitted());
    }

    public <R> GeneralJobNoInput<R> then(GeneralJobNoInput<R> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runNoInput(job::run)
                .onComplete(r -> job.onComplete());
    }

    public <T> DBJobNoReturn<T> then(DBJobNoReturn<T> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDBNoReturn(job::run)
                .onCommitted(job::onCommitted);
    }

    public <T> GeneralJobNoReturn<T> then(GeneralJobNoReturn<T> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runNoReturn(job::run)
                .onComplete(job::onComplete);
    }

    public <T, R> DBJob<T, R> then(DBJob<T, R> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runOnDB(job::run)
                .onCommitted(r -> job.onCommitted());
    }

    public <T, R> GeneralJob<T, R> then(GeneralJob<T, R> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .run(job::run)
                .onComplete(r -> job.onComplete());
    }

    public void execute() throws Exception {
        preCheck();
    }

    void preCheck() throws Exception {
        _preCheck.apply();
    }
}
