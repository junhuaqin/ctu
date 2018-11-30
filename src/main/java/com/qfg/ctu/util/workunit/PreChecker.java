package com.santaba.server.util.workunit;

import java.util.Objects;

import static com.santaba.server.util.workunit.Functions.ignoreAnything;
import static com.santaba.server.util.workunit.Functions.nothingCared;

/**
 * Created by Robert Qin on 31/07/2018.
 */
public class PreChecker implements Job {
    private final PreCheck _preCheck;
    PreChecker(PreCheck preCheck) {
        Objects.requireNonNull(preCheck);
        this._preCheck = preCheck;
    }

    public NoInputAndReturnJob runNoInputAndReturn(Jobs.DBProcedureNoReturn procedure) {
        Objects.requireNonNull(procedure);
        return new NoInputAndReturnJob(
                _preCheck,
                (context, t) -> {
                    procedure.apply(context.getDBConnection());
                    return null;
                },
                nothingCared(),
                ignoreAnything());
    }

    public <R> NoInputJob<R> runNoInput(Jobs.DBProcedure<R> procedure) {
        Objects.requireNonNull(procedure);
        return new NoInputJob<>(
                _preCheck,
                (context, t) -> procedure.apply(context.getDBConnection()),
                ignoreAnything(),
                ignoreAnything());
    }

    public <T> NoReturnJob<T> runNoReturn(Jobs.ConsumerDBProcedureNoReturn<T> procedure) {
        Objects.requireNonNull(procedure);
        return new NoReturnJob<>(
                _preCheck,
                (context, t) -> {
                    procedure.apply(context.getDBConnection(), t);
                    return null;
                },
                nothingCared(),
                ignoreAnything());
    }

    public <T, R> NormalJob<T, R> run(Jobs.ConsumerDBProcedure<T, R> procedure) {
        Objects.requireNonNull(procedure);
        return new NormalJob<>(
                _preCheck,
                (context, t) -> procedure.apply(context.getDBConnection(), t),
                ignoreAnything(),
                ignoreAnything());
    }

    public NoInputAndReturnJob runNoInputAndReturn(Jobs.ProcedureNoReturn procedure) {
        Objects.requireNonNull(procedure);
        return new NoInputAndReturnJob(
                _preCheck,
                (context, o) -> {
                    procedure.apply();
                    return null;
                },
                nothingCared(),
                ignoreAnything());
    }

    public <R> NoInputJob<R> runNoInput(Jobs.Procedure<R> procedure) {
        Objects.requireNonNull(procedure);
        return new NoInputJob<>(
                _preCheck,
                (context, o) -> procedure.apply(),
                ignoreAnything(),
                ignoreAnything());
    }

    public <T> NoReturnJob<T> runNoReturn(Jobs.ConsumerProcedureNoReturn<T> procedure) {
        Objects.requireNonNull(procedure);
        return new NoReturnJob<>(
                _preCheck,
                (context, t) -> {
                    procedure.apply(t);
                    return null;
                },
                nothingCared(),
                ignoreAnything());
    }

    public <T, R> NormalJob<T, R> run(Jobs.ConsumerProcedure<T, R> procedure) {
        Objects.requireNonNull(procedure);
        return new NormalJob<>(
                _preCheck,
                (context, t) -> procedure.apply(t),
                ignoreAnything(),
                ignoreAnything());
    }

    public PreChecker then(PreChecker job) {
        Objects.requireNonNull(job);
        return new PreChecker(() -> {
            _preCheck.apply();
            job._preCheck.apply();
        });
    }

    public NoInputAndReturnJob then(NoInputAndReturnJob job) {
        Objects.requireNonNull(job);
        return new NoInputAndReturnJob(trans2Identity()._then(job._impl));
    }

    public <R> NoInputJob<R> then(NoInputJob<R> job) {
        Objects.requireNonNull(job);
        return new NoInputJob<>(trans2Identity()._then(job._impl));
    }

    public <T> NoReturnJob<T> then(NoReturnJob<T> job) {
        Objects.requireNonNull(job);
        return new NoReturnJob<>(this.<T>trans2Identity()._then(job._impl));
    }

    public <T, R> NormalJob<T, R> then(NormalJob<T, R> job) {
        Objects.requireNonNull(job);
        return new NormalJob<>(this.<T>trans2Identity()._then(job._impl));
    }

    public void execute() throws Exception {
        trans2Identity().execute(null);
    }

    <T> JobImpl<T, T> trans2Identity() {
        return new JobImplLeaf<>(
                _preCheck,
                (context, t) -> t,
                ignoreAnything(),
                ignoreAnything());
    }
}
