package data;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/game_db";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection connect() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}