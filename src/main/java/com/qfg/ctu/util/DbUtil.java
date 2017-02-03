package com.qfg.ctu.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rbtq on 7/23/16.
 */
public class DbUtil {
    private final static Logger LOGGER = Logger.getLogger(DbUtil.class.getName());
    public static final String MYSQL_JNDI_REF = "java:/comp/env/jdbc/ctu";
    protected static DataSource _ds;

    public static void initConnectionPoolByJndi() throws Exception {
        InitialContext ctx = new InitialContext();
        _ds = (DataSource)ctx.lookup(MYSQL_JNDI_REF);
        if(null == _ds) {
            throw new NamingException("Cannot get Datasource");
        } else {
            Connection conn = _ds.getConnection();
            if(null == conn) {
                throw new Exception("Cannot get dabatase connection");
            } else {
                DatabaseMetaData metaData = conn.getMetaData();
//                LogMsg.info(String.format("databaseURL=%s, driverClass=%s, user=%s, url=%s", new Object[]{metaData.getURL(), metaData.getDriverName(), metaData.getUserName(), metaData.getURL()}), "DbUtil.initContext");
                conn.close();
            }
        }
    }

    public static Connection atomicGetConnection() throws SQLException {
        Connection conn = _ds.getConnection();
        if(conn == null) {
            throw new SQLException("Unable to get DB connection");
        } else {
            if(!conn.getAutoCommit()) {
                conn.setAutoCommit(true);
            }

            return conn;
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = _ds.getConnection();
        if(conn == null) {
            throw new SQLException("Unable to get DB connection");
        }

        return conn;
    }

    public static boolean doesDbExist(String dbName) throws SQLException {
        int count = 0;

        try (Connection conn = atomicGetConnection()){
            String ignore = "select count(*) from information_schema.schemata where SCHEMA_NAME = ?";
            PreparedStatement ps = conn.prepareStatement(ignore);
            ps.setString(1, dbName);

            for(ResultSet rt = ps.executeQuery(); rt.next(); count = rt.getInt(1)) {
            }
        }

        return count > 0;
    }

    public static boolean changeDb(Connection conn, String dbName) throws SQLException {
        if (conn != null) {
            executeUpdate(conn, "use " + dbName);
            return true;
        }

        return false;
    }


    public static void closeQuietly(Connection conn) {
        if(conn != null) {
            try {
                if(!conn.getAutoCommit()) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException var3) {
                ;
            }

            try {
                conn.close();
            } catch (Exception var2) {
                ;
            }
        }

    }

    public static void closeQuietly(QueryResource qrs) {
        if(qrs != null) {
            closeQuietly(qrs.pstmt);
            closeQuietly(qrs.rs);
        }

    }

    private static void closeQuietly(Statement stmt) {
        try {
            if(stmt != null) {
                stmt.close();
            }
        } catch (SQLException var2) {
            ;
        }

    }

    private static void closeQuietly(ResultSet rs) {
        try {
            if(rs != null) {
                rs.close();
            }
        } catch (SQLException var2) {
            ;
        }

    }

    public static void rollbackQuietly(Connection conn) {
        if(conn != null) {
            try {
                conn.rollback();
            } catch (Exception var2) {
                ;
            }
        }

    }

    private static void _setSingleParam(PreparedStatement pstmt, int index, Object param) throws SQLException {
        if(param == null) {
            pstmt.setString(index, null);
        } else if(param instanceof Integer) {
            pstmt.setInt(index, (Integer) param);
        } else if(param instanceof Long) {
            pstmt.setLong(index, (Long) param);
        } else if(param instanceof byte[]) {
            pstmt.setBytes(index, (byte[]) param);
        } else if(param instanceof LocalDateTime){
            pstmt.setTimestamp(index, DateTimeUtil.mapLocalDateTime2Timestamp((LocalDateTime)param));
        } else {
            pstmt.setString(index, param.toString());
        }

    }

    protected static void _setParams(PreparedStatement pstmt, Object... params) throws SQLException {
        int i = 0;
        Object[] arr$ = params;
        int len$ = params.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Object o = arr$[i$];
            if(o == null) {
                pstmt.setString(i + 1, null);
                ++i;
            } else {
                int i$2;
                int var15;
                if(o instanceof int[]) {
                    int[] var14 = (int[])o;
                    var15 = var14.length;

                    for(i$2 = 0; i$2 < var15; ++i$2) {
                        int var17 = var14[i$2];
                        pstmt.setInt(i + 1, var17);
                        ++i;
                    }
                } else if(o instanceof long[]) {
                    long[] var13 = (long[])o;
                    var15 = var13.length;

                    for(i$2 = 0; i$2 < var15; ++i$2) {
                        long var16 = var13[i$2];
                        pstmt.setLong(i + 1, var16);
                        ++i;
                    }
                } else if(o instanceof Object[]) {
                    Object[] var12 = (Object[])o;
                    var15 = var12.length;

                    for(i$2 = 0; i$2 < var15; ++i$2) {
                        Object item1 = var12[i$2];
                        _setSingleParam(pstmt, i + 1, item1);
                        ++i;
                    }
                } else if(o instanceof Collection) {
                    for(Iterator i$1 = ((Collection)o).iterator(); i$1.hasNext(); ++i) {
                        Object item = i$1.next();
                        _setSingleParam(pstmt, i + 1, item);
                    }
                } else {
                    _setSingleParam(pstmt, i + 1, o);
                    ++i;
                }
            }
        }

    }

    public static QueryResource executeUpdate(Connection conn, String sql, Object... params) throws SQLException {
        QueryResource qrs = new QueryResource();

        try {
            if(conn == null) {
                throw new SQLException("Connection object is null");
            } else {
                qrs.pstmt = conn.prepareStatement(sql);
                _setParams(qrs.pstmt, params);
                qrs.rt = qrs.pstmt.executeUpdate();
                return qrs;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, String.format("sql=%s, err:%s", sql, e.getMessage()));
            throw e;
        }
    }

    public static QueryResource executeQuery(Connection conn, String sql, Object... params) throws SQLException {
        QueryResource qrs = new QueryResource();

        try {
            if(conn == null) {
                throw new SQLException("Connection object is null");
            } else {
                qrs.pstmt = conn.prepareStatement(sql);
                _setParams(qrs.pstmt, params);
                qrs.rs = qrs.pstmt.executeQuery();
                return qrs;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, String.format("sql=%s, err:%s", sql, e.getMessage()));
            throw e;
        }
    }

    public static boolean next(QueryResource qrs) throws SQLException {
        if ((qrs != null) && (qrs.rs != null)) {
            return qrs.rs.next();
        } else {
            throw new SQLException("SQL Error, Null ResordSet");
        }
    }

    public static class QueryResource {
        public PreparedStatement pstmt = null;
        public ResultSet rs = null;
        public int rt = 0;

        public QueryResource() {
        }
    }
}
