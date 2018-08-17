package com.santaba.server.util.workunit;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Created by Robert Qin on 11/05/2018.
 */
public class GeneralJobNoReturn<T> implements Job {
    private final PreCheck _preCheck;
    private final Jobs.ConsumerProcedureNoReturn<T> _procedure;
    private final Notification _notification;

    GeneralJobNoReturn(PreCheck preCheck, Jobs.ConsumerProcedureNoReturn<T> procedure, Notification notification) {
        Objects.requireNonNull(preCheck);
        Objects.requireNonNull(procedure);
        Objects.requireNonNull(notification);

        this._preCheck = preCheck;
        this._procedure = procedure;
        this._notification = notification;
    }

    public GeneralJobNoReturn<T> onComplete(Notification notification) {
        return new GeneralJobNoReturn<>(_preCheck, _procedure, notification);
    }

    public GeneralJobNoReturn<T> then(PreChecker job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runNoReturn(this::run)
                .onComplete(this::onComplete);
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

    public GeneralJobNoInputAndReturn bind(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);

        return Jobs.preCheck(this::preCheck)
                .runNoInputAndReturn(() -> run(supplier.get()))
                .onComplete(this::onComplete);
    }

    public void execute(T t) throws Exception {
        preCheck();
        run(t);
        onComplete();
    }

    void preCheck() throws Exception {
        _preCheck.apply();
    }

    void run(T t) throws Exception {
        _procedure.apply(t);
    }

    void onComplete() {
        _notification.apply();
    }
}
