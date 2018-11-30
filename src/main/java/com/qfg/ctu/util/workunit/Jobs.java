package com.santaba.server.util.workunit;

import java.sql.Connection;
import java.util.Objects;
import java.util.function.Consumer;

import static com.santaba.server.util.workunit.Functions.*;

/**
 * Created by Robert Qin on 01/08/2018.
 */
public class Jobs {
    /*
    For DB
     */
    /**
     * Represents an operation that do something via non side-effects and returns
     * result. the side-effects should be applied at {@link com.santaba.server.util.workunit.Job.Notification}.
     *
     * <p>This is a functional interface</a>
     * whose functional method is {@link #apply(Connection)}.
     *
     * @param <T> the type of results returned by this procedure
     */
    @FunctionalInterface
    public interface DBProcedure<T> {
        T apply(Connection conn) throws Exception;
    }

    /**
     * Represents an operation that do something via non side-effects and returns no
     * result. the side-effects should be applied at {@link com.santaba.server.util.workunit.Job.Notification}.
     *
     * <p>This is a functional interface</a>
     * whose functional method is {@link #apply(Connection)}.
     *
     */
    @FunctionalInterface
    public interface DBProcedureNoReturn {
        void apply(Connection conn) throws Exception;
    }

    /**
     * Represents an operation that do something via non side-effects and returns
     * result. the side-effects should be applied at {@link com.santaba.server.util.workunit.Job.Notification}.
     *
     * <p>This is a functional interface</a>
     * whose functional method is {@link #apply(Connection, Object)}.
     *
     * @param <T> the type of the procedure consumed.
     * @param <R> the type of results returned by this procedure
     */
    @FunctionalInterface
    public interface ConsumerDBProcedure<T, R> {
        R apply(Connection conn, T t) throws Exception;
    }

    /**
     * Represents an operation that do something via non side-effects and returns no
     * result. the side-effects should be applied at {@link com.santaba.server.util.workunit.Job.Notification}.
     *
     * <p>This is a functional interface</a>
     * whose functional method is {@link #apply(Connection, Object)}.
     *
     * @param <T> the type of the procedure consumed.
     */
    @FunctionalInterface
    public interface ConsumerDBProcedureNoReturn<T> {
        void apply(Connection conn, T t) throws Exception;
    }

    /**
     * Just preCheck some condition if satisfied.
     * Could execute other work by invoke {@link PreChecker#run(Jobs.ConsumerDBProcedure)}
     *
     * @return PreChecker
     */
    public static PreChecker preCheck(Job.PreCheck preCheck) {
        return new PreChecker(preCheck);
    }

    /**
     * Do something without consume anything and returns no result
     * Could got a notification when the job complete {@link NoInputAndReturnJob#onComplete(Job.Notification)}
     *
     * @return NoInputAndReturnJob
     */
    public static NoInputAndReturnJob runNoInputAndReturn(Jobs.DBProcedureNoReturn procedure) {
        Objects.requireNonNull(procedure);
        return new NoInputAndReturnJob(
                checkNothing(),
                (context, t) -> {
                    procedure.apply(context.getDBConnection());
                    return null;
                },
                nothingCared(),
                ignoreAnything());
    }

    /**
     * Do something without consume anything and returns result
     * Could got a notification when the job complete {@link NoInputJob#onComplete(Consumer)}
     *
     * @param <R> the return type
     * @return NoInputJob
     */
    public static <R> NoInputJob<R> runNoInput(Jobs.DBProcedure<R> procedure) {
        Objects.requireNonNull(procedure);
        return new NoInputJob<>(
                checkNothing(),
                (context, t) -> procedure.apply(context.getDBConnection()),
                ignoreAnything(),
                ignoreAnything());
    }

    /**
     * Do something with consume anything and returns no result
     * Could got a notification when the job complete {@link NoReturnJob#onComplete(Job.Notification)}
     *
     * @param <T> the parameter type
     * @return NoReturnJob
     */
    public static <T> NoReturnJob<T> runNoReturn(Jobs.ConsumerDBProcedureNoReturn<T> procedure) {
        Objects.requireNonNull(procedure);
        return new NoReturnJob<>(
                checkNothing(),
                (context, t) -> {
                    procedure.apply(context.getDBConnection(), t);
                    return null;
                },
                nothingCared(),
                ignoreAnything());
    }

    /**
     * Do something with consume anything and returns result
     * Could got a notification when the job complete {@link NormalJob#onComplete(Consumer)}
     *
     * @param <T> the parameter type
     * @param <R> the return type
     * @return NormalJob
     */
    public static <T, R> NormalJob<T, R> run(Jobs.ConsumerDBProcedure<T, R> procedure) {
        Objects.requireNonNull(procedure);
        return new NormalJob<>(
                checkNothing(),
                (context, t) -> procedure.apply(context.getDBConnection(), t),
                ignoreAnything(),
                ignoreAnything());
    }

    /*
    For Computation
     */
    /**
     * Represents an operation that do something via non side-effects and returns
     * result. the side-effects should be applied at {@link com.santaba.server.util.workunit.Job.Notification}.
     *
     * <p>This is a functional interface</a>
     * whose functional method is {@link #apply()}.
     *
     * @param <T> the type of results returned by this procedure
     */
    @FunctionalInterface
    public interface Procedure<T> {
        T apply() throws Exception;
    }

    /**
     * Represents an operation that do something via non side-effects and returns no
     * result. the side-effects should be applied at {@link com.santaba.server.util.workunit.Job.Notification}.
     *
     * <p>This is a functional interface</a>
     * whose functional method is {@link #apply()}.
     *
     */
    @FunctionalInterface
    public interface ProcedureNoReturn {
        void apply() throws Exception;
    }

    /**
     * Represents an operation that do something via non side-effects and returns
     * result. the side-effects should be applied at {@link com.santaba.server.util.workunit.Job.Notification}.
     *
     * <p>This is a functional interface</a>
     * whose functional method is {@link #apply(Object)}.
     *
     * @param <T> the type of the procedure consumed.
     * @param <R> the type of results returned by this procedure
     */
    @FunctionalInterface
    public interface ConsumerProcedure<T, R> {
        R apply(T t) throws Exception;
    }

    /**
     * Represents an operation that do something via non side-effects and returns no
     * result. the side-effects should be applied at {@link com.santaba.server.util.workunit.Job.Notification}.
     *
     * <p>This is a functional interface</a>
     * whose functional method is {@link #apply(Object)}.
     *
     * @param <T> the type of the procedure consumed.
     */
    @FunctionalInterface
    public interface ConsumerProcedureNoReturn<T> {
        void apply(T t) throws Exception;
    }

    /**
     * Do something without consume anything and returns no result
     * Could got a notification when the job complete {@link NoInputAndReturnJob#onComplete(Job.Notification)}
     *
     * @return GeneralJobNoInputAndReturn
     */
    public static NoInputAndReturnJob runNoInputAndReturn(Jobs.ProcedureNoReturn procedure) {
        Objects.requireNonNull(procedure);
        return new NoInputAndReturnJob(
                checkNothing(),
                (context, o) -> {
                    procedure.apply();
                    return null;
                },
                nothingCared(),
                ignoreAnything());
    }

    /**
     * Do something without consume anything and returns result
     * Could got a notification when the job complete {@link NoInputJob#onComplete(Consumer)}
     *
     * @param <R> the return type
     * @return GeneralJobNoInput
     */
    public static <R> NoInputJob<R> runNoInput(Jobs.Procedure<R> procedure) {
        Objects.requireNonNull(procedure);
        return new NoInputJob<>(
                checkNothing(),
                (context, o) -> procedure.apply(),
                ignoreAnything(),
                ignoreAnything());
    }

    /**
     * Do something with consume anything and returns no result
     * Could got a notification when the job complete {@link NoReturnJob#onComplete(Job.Notification)}
     *
     * @param <T> the parameter type
     * @return GeneralJobNoReturn
     */
    public static <T> NoReturnJob<T> runNoReturn(Jobs.ConsumerProcedureNoReturn<T> procedure) {
        Objects.requireNonNull(procedure);
        return new NoReturnJob<>(
                checkNothing(),
                (context, t) -> {
                    procedure.apply(t);
                    return null;
                },
                nothingCared(),
                ignoreAnything());
    }

    /**
     * Do something with consume anything and returns result
     * Could got a notification when the job complete {@link NormalJob#onComplete(Consumer)}
     *
     * @param <T> the parameter type
     * @param <R> the return type
     * @return GeneralJob
     */
    public static <T, R> NormalJob<T, R> run(Jobs.ConsumerProcedure<T, R> procedure) {
        Objects.requireNonNull(procedure);
        return new NormalJob<>(
                checkNothing(),
                (context, t) -> procedure.apply(t),
                ignoreAnything(),
                ignoreAnything());
    }

    /**
     * Do nothing but got a notification when the job complete
     *
     * @return GeneralJobNoInputAndReturn
     */
    public static NoInputAndReturnJob onComplete(Job.Notification notification) {
        Objects.requireNonNull(notification);
        return new NoInputAndReturnJob(checkNothing(), (context, o) -> o, notification, ignoreAnything());
    }

    /**
     * Do nothing but got a notification when the job complete
     *
     * @return GeneralJobNoInputAndReturn
     */
    public static NoInputAndReturnJob onError(Consumer<Exception> onError) {
        Objects.requireNonNull(onError);
        return new NoInputAndReturnJob(checkNothing(), (context, o) -> o, nothingCared(), onError);
    }

    /**
     * A wrapper to let code looks better.
     * @param job
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> NormalJob<T, R> of(NormalJob<T, R> job) {
        return job;
    }

    public static <T> NoReturnJob<T> of(NoReturnJob<T> job) {
        return job;
    }

    public static <R> NoInputJob<R> of(NoInputJob<R> job) {
        return job;
    }

    public static NoInputAndReturnJob of(NoInputAndReturnJob job) {
        return job;
    }
}
