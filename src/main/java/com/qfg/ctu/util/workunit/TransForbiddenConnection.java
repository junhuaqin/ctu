package com.santaba.server.util.workunit;

import com.santaba.common.util.DbUtil;

import java.sql.*;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * It's a connection wrap to prevent user to misuse the connection at
 * DbJobs's DBProcedure.
 * Created by Robert Qin on 08/08/2018.
 */
class TransForbiddenConnection implements Connection {
    private final Connection _delegate;
    private final boolean _oldAutoCommit;

    TransForbiddenConnection(Connection delegate) throws SQLException {
        Objects.requireNonNull(delegate);
        _oldAutoCommit = DbUtil.setAutoCommit(delegate, false);
        this._delegate = delegate;
    }

    Connection getDelegate() {
        return _delegate;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return _delegate.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return _delegate.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return _delegate.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return _delegate.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        throw new SQLException("unsupported");
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return _delegate.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        throw new SQLException("unsupported");
    }

    @Override
    public void rollback() throws SQLException {
        throw new SQLException("unsupported");
    }

    @Override
    public void close() throws SQLException {
        throw new SQLException("unsupported");
    }

    @Override
    public boolean isClosed() throws SQLException {
        return _delegate.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return _delegate.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        _delegate.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return _delegate.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        _delegate.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return _delegate.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        _delegate.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return _delegate.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return _delegate.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        _delegate.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return _delegate.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return _delegate.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return _delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return _delegate.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        _delegate.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        _delegate.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return _delegate.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return _delegate.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return _delegate.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        _delegate.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        _delegate.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return _delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return _delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return _delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return _delegate.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return _delegate.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return _delegate.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return _delegate.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return _delegate.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return _delegate.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return _delegate.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return _delegate.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        _delegate.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        _delegate.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return _delegate.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return _delegate.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return _delegate.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return _delegate.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        _delegate.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return _delegate.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        _delegate.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        _delegate.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return _delegate.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return _delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return _delegate.isWrapperFor(iface);
    }

    void release() throws SQLException {
        DbUtil.setAutoCommit(_delegate, _oldAutoCommit);
    }
}
