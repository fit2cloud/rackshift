package io.rackshift.dhcpproxy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Stack;

public class MySqlUtil {
    private static Stack<Connection> connectionStack = new Stack<>();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < 10; i++) {
                connectionStack.push(DriverManager.getConnection(ConfigurationUtil.getConfig("spring.datasource.url", "") + "&user=" + ConfigurationUtil.getConfig("spring.datasource.username", "root") + "&password=" + ConfigurationUtil.getConfig("spring.datasource.password", "admin")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Connection getCon() {
        if (connectionStack.size() > 0)
            return connectionStack.pop();
        return null;
    }

    private static void releaseCon(Connection con) {
        if (con != null)
            connectionStack.push(con);
    }

    public static ResultSet select(String sql, String... args) {
        Connection con = getCon();
        if (con != null) {
            try {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setString(i + 1, args[i]);
                }

                ResultSet r = preparedStatement.executeQuery();
                if (r != null) {
                    return r;
                }
                return null;
            } catch (Exception e) {

            } finally {
                releaseCon(con);
            }
        }
        return null;
    }
}
