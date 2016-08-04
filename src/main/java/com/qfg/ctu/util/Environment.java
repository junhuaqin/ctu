package com.qfg.ctu.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rbtq on 7/31/16.
 */
public class Environment {
    private final static Logger LOGGER = Logger.getLogger(Environment.class.getName());

    public void reBuildDB() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/com/qfg/ctu/initfiles/create_ctu.sql");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String command = "";
        String line;

        Connection conn = DbUtil.atomicGetConnection();
        try {
            while ((line = reader.readLine()) != null) {
                line = StringUtil.strip(line);
                if (line.length() == 0 || line.startsWith("--")) {
                    continue;
                }

                command += line + "\n";
                if (line.endsWith(";")) {
                    try {
                        DbUtil.executeUpdate(conn, command);
                    } catch (SQLException e) {
                        LOGGER.log(Level.WARNING, "Failed to run SQL: " + command);
                        throw e;
                    }
                    command = ""; // prepare for next command
                }
            }

            if (command.length() != 0) {
                DbUtil.executeUpdate(conn, command);
            }
        } finally {
            reader.close();
            DbUtil.closeQuietly(conn);
        }
    }
}
