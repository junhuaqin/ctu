package com.qfg.ctu.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by rbtq on 7/23/16.
 */
public class DbUtil {
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

    protected static Connection atomicGetConnection() throws SQLException {
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
}
