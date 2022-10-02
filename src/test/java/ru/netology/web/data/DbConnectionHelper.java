package ru.netology.web.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionHelper {
    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.user");
    private static final String password = System.getProperty("db.password");
    private static Connection connection;


    public static Connection getConnection() {
        connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the SQL server successfully.");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return connection;
    }


}
