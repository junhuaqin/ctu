package com.santaba.server.util.workunit;

import com.santaba.common.util.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
     * Could execute other work by invoke {@link PreChecker#runOnDB(Jobs.ConsumerDBProcedure)}
     *
     * @return PreChecker
     */
    public static PreChecker preCheck(Job.PreCheck preCheck) {
        return new PreChecker(preCheck);
    }

    /**
     * Do something without consume anything and returns no result
     * Could got a notification when the job complete {@link DBJobNoInputAndReturn#onCommitted(Job.Notification)}
     *
     * @return DBJobNoInputAndReturn
     */
    public static DBJobNoInputAndReturn runOnDBNoInputAndReturn(Jobs.DBProcedureNoReturn procedure) {
        return new DBJobNoInputAndReturn(checkNothing(), procedure, nothingCared());
    }

    /**
     * Do something without consume anything and returns result
     * Could got a notification when the job complete {@link DBJobNoInput#onCommitted(Consumer)}
     *
     * @param <R> the return type
     * @return DBJobNoInput
     */
    public static <R> DBJobNoInput<R> runOnDBNoInput(Jobs.DBProcedure<R> procedure) {
        return new DBJobNoInput<>(checkNothing(), procedure, consumeNothing());
    }

    /**
     * Do something with consume anything and returns no result
     * Could got a notification when the job complete {@link DBJobNoReturn#onCommitted(Job.Notification)}
     *
     * @param <T> the parameter type
     * @return DBJobNoReturn
     */
    public static <T> DBJobNoReturn<T> runOnDBNoReturn(Jobs.ConsumerDBProcedureNoReturn<T> procedure) {
        return new DBJobNoReturn<>(checkNothing(), procedure, nothingCared());
    }

    /**
     * Do something with consume anything and returns result
     * Could got a notification when the job complete {@link DBJob#onCommitted(Consumer)}
     *
     * @param <T> the parameter type
     * @param <R> the return type
     * @return DBJob
     */
    public static <T, R> DBJob<T, R> runOnDB(Jobs.ConsumerDBProcedure<T, R> procedure) {
        return new DBJob<>(checkNothing(), procedure, consumeNothing());
    }

    /**
     * Do nothing but got a notification when the job complete
     *
     * @return GeneralJobNoInputAndReturn
     */
    public static DBJobNoInputAndReturn onCommitted(Job.Notification notification) {
        return new DBJobNoInputAndReturn(checkNothing(), conn -> {}, notification);
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
     * Could got a notification when the job complete {@link GeneralJobNoInputAndReturn#onComplete(Job.Notification)}
     *
     * @return GeneralJobNoInputAndReturn
     */
    public static GeneralJobNoInputAndReturn runNoInputAndReturn(Jobs.ProcedureNoReturn procedure) {
        return new GeneralJobNoInputAndReturn(checkNothing(), procedure, nothingCared());
    }

    /**
     * Do something without consume anything and returns result
     * Could got a notification when the job complete {@link GeneralJobNoInput#onComplete(Consumer)}
     *
     * @param <R> the return type
     * @return GeneralJobNoInput
     */
    public static <R> GeneralJobNoInput<R> runNoInput(Jobs.Procedure<R> procedure) {
        return new GeneralJobNoInput<>(checkNothing(), procedure, consumeNothing());
    }

    /**
     * Do something with consume anything and returns no result
     * Could got a notification when the job complete {@link GeneralJobNoReturn#onComplete(Job.Notification)}
     *
     * @param <T> the parameter type
     * @return GeneralJobNoReturn
     */
    public static <T> GeneralJobNoReturn<T> runNoReturn(Jobs.ConsumerProcedureNoReturn<T> procedure) {
        return new GeneralJobNoReturn<>(checkNothing(), procedure, nothingCared());
    }

    /**
     * Do something with consume anything and returns result
     * Could got a notification when the job complete {@link GeneralJob#onComplete(Consumer)}
     *
     * @param <T> the parameter type
     * @param <R> the return type
     * @return GeneralJob
     */
    public static <T, R> GeneralJob<T, R> run(Jobs.ConsumerProcedure<T, R> procedure) {
        return new GeneralJob<>(checkNothing(), procedure, consumeNothing());
    }

    /**
     * Do nothing but got a notification when the job complete
     *
     * @return GeneralJobNoInputAndReturn
     */
    public static GeneralJobNoInputAndReturn onComplete(Job.Notification notification) {
        return new GeneralJobNoInputAndReturn(checkNothing(), () -> {}, notification);
    }

    /*
    Internal usage
     */

    static <R> R execute(Supplier<Connection> connSupplier,
                         Consumer<Connection> connCloser,
                         Job.PreCheck preCheck,
                         Jobs.DBProcedure<R> procedure,
                         Consumer<R> onCommitted) throws Exception {
        preCheck.apply();

        Connection conn = connSupplier.get();
        if (Objects.isNull(conn)) {
            throw new IllegalStateException("Failed to get database connection");
        }
        boolean oldAutoCommit = DbUtil.setAutoCommit(conn, false);

        R r;
        try {
            r = procedure.apply(conn);
            conn.commit();
        }
        catch (Exception e) {
            DbUtil.rollbackQuietly(conn);
            throw e;
        }
        finally {
            try {
                DbUtil.setAutoCommit(conn, oldAutoCommit);
            }
            catch (SQLException ignored) {
            }
            connCloser.accept(conn);
        }

        onCommitted.accept(r);
        return r;
    }
}
