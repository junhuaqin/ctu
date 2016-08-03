package com.qfg.ctu.dao;

import com.qfg.ctu.util.DbUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rbtq on 8/2/16.
 */
public abstract class AbstractDao<T> {
    protected abstract T _loadRecord(ResultSet qrs) throws SQLException;

    protected T _get(Connection conn, String sql, Object... params) throws SQLException {
        DbUtil.QueryResource qrs = null;

        try {
            qrs = DbUtil.executeQuery(conn, sql, params);
            if(DbUtil.next(qrs)) {
                return this._loadRecord(qrs.rs);
            } else {
                return null;
            }
        } finally {
            DbUtil.closeQuietly(qrs);
        }
    }

    protected Long _getLong(Connection conn, String sql, Object... params) throws SQLException {
        DbUtil.QueryResource qrs = null;

        try {
            qrs = DbUtil.executeQuery(conn, sql, params);
            if(DbUtil.next(qrs)) {
                return qrs.rs.getLong(0);
            } else {
                return null;
            }
        } finally {
            DbUtil.closeQuietly(qrs);
        }
    }

    protected Integer _getLastId(Connection conn) throws SQLException {
        DbUtil.QueryResource qrs = null;

        try {
            qrs = DbUtil.executeQuery(conn, "SELECT last_insert_id()");
            if(DbUtil.next(qrs)) {
                return qrs.rs.getInt(0);
            } else {
                return null;
            }
        } finally {
            DbUtil.closeQuietly(qrs);
        }
    }

    protected List<T> _getArray(Connection conn, String sql, Object... params) throws SQLException {
        DbUtil.QueryResource qrs = null;

        try {
            List<T> arr = new LinkedList<>();
            qrs = DbUtil.executeQuery(conn, sql, params);

            while(DbUtil.next(qrs)) {
                arr.add(this._loadRecord(qrs.rs));
            }

            return arr;
        } finally {
            DbUtil.closeQuietly(qrs);
        }
    }

    protected int _update(Connection conn, String sql, Object... params) throws SQLException {
        DbUtil.QueryResource qrs = null;
        try {
            qrs = DbUtil.executeUpdate(conn, sql, params);
            return qrs.rt;
        } finally {
            DbUtil.closeQuietly(qrs);
        }
    }
}
