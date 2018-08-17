package com.santaba.server.util.workunit;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by Robert Qin on 11/05/2018.
 */
public class GeneralJobNoInput<R> implements Job {
    private R _result;
    private final PreCheck _preCheck;
    private final Jobs.Procedure<R> _procedure;
    private final Consumer<R> _notification;

    GeneralJobNoInput(PreCheck preCheck, Jobs.Procedure<R> procedure, Consumer<R> notification) {
        Objects.requireNonNull(preCheck);
        Objects.requireNonNull(procedure);
        Objects.requireNonNull(notification);

        this._preCheck = preCheck;
        this._procedure = procedure;
        this._notification = notification;
    }

    public GeneralJobNoInput<R> onComplete(Consumer<R> notification) {
        return new GeneralJobNoInput<>(_preCheck, _procedure, notification);
    }

    public GeneralJobNoInput<R> then(PreChecker job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runNoInput(this::run)
                .onComplete(r -> onComplete());
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

    public <U> GeneralJobNoInput<U> then(GeneralJobNoInput<U> job) {
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

    public <U> DBJobNoInput<U> then(DBJobNoInput<U> job) {
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

    public GeneralJobNoInputAndReturn then(GeneralJobNoReturn<R> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
            preCheck();
            job.preCheck();
        })
                .runNoInputAndReturn(() -> {
                    R r = run();
                    job.run(r);
                })
                .onComplete(() -> {
                    onComplete();
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
                    R r = run();
                    job.run(conn, r);
                })
                .onCommitted(() -> {
                    onComplete();
                    job.onCommitted();
                });
    }

    public <U> GeneralJobNoInput<U> then(GeneralJob<R, U> job) {
        Objects.requireNonNull(job);

        return Jobs.preCheck(() -> {
                    preCheck();
                    job.preCheck();
                })
                .runNoInput(() -> {
                    R r = run();
                    return job.run(r);
                })
                .onComplete(r -> {
                    onComplete();
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
                    R r = run();
                    return job.run(conn, r);
                })
                .onCommitted(r -> {
                    onComplete();
                    job.onCommitted();
                });
    }

    public R execute() throws Exception {
        preCheck();
        R r = run();
        onComplete();
        return r;
    }

    void preCheck() throws Exception {
        _preCheck.apply();
    }

    R run() throws Exception {
        _result = _procedure.apply();
        return _result;
    }

    void onComplete() {
        _notification.accept(_result);
    }
}
