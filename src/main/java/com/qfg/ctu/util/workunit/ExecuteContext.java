package com.santaba.server.util.workunit;

import com.santaba.common.util.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Robert Qin on 05/09/2018.
 */
class ExecuteContext {
    private TransForbiddenConnection _connection = null;
    private final LinkedList<Object> _results = new LinkedList<>();

    void offer(Object result) {
        _results.offer(result);
    }

    Object poll() {
        return _results.poll();
    }

    Connection getDBConnection() throws Exception {
        if (Objects.isNull(_connection)) {
            _connection = new TransForbiddenConnection(
                    Optional.ofNullable(DbUtil.getConnection(false))
                            .orElseThrow(() -> new SQLException("Failed to get database connection"))
            );
        }

        return _connection;
    }

    void onStart() throws Exception {
    }

    void onComplete() throws Exception {
        if (Objects.nonNull(_connection)) {
            _connection.getDelegate().commit();
        }
    }

    void onError() {
        if (Objects.nonNull(_connection)) {
            DbUtil.rollbackQuietly(_connection.getDelegate());
        }
    }

    void onFinalize() {
        if (Objects.nonNull(_connection)) {
            Connection connection = _connection.getDelegate();
            try {
                _connection.release();
            }
            catch (SQLException ignore) {
            }

            _connection = null;
            DbUtil.closeQuietly(connection);
        }
    }
}
