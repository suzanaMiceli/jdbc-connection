package org.example.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConnection {

    private static final Logger log = LoggerFactory.getLogger("log");

    private static Connection connection = null;

    public static Connection getConnection() {
        if(connection != null) return connection;

        //String url = "jdbc:postgresql://localhost:5432/test?user=postgres&password=postgres";
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("src/main/resources/application.properties"));
        } catch (FileNotFoundException e) {
            log.error("properties files not found");
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("error while reading properties files");
            throw new RuntimeException(e);
        }

        String url = prop.getProperty("url");

        try {
            connection = DriverManager.getConnection(url);
            log.info("connected with database....");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

}
