package org.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {

    private static Connection connection;

    public static Connection getConnection() {
        if(connection != null) return connection;

        String url = "jdbc:postgresql://localhost:5432/test?user=postgres&password=postgres";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }

}
