package org.example;

import org.example.connection.JdbcConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection db = JdbcConnection.getConnection();
        System.out.println(db);
        System.out.println("Connected with database!");
    }
}