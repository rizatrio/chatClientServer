package am.home.service.db;

import java.sql.*;

public class DBCon {

    private static Connection connection;
    private static Statement statement;

    private DBCon() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            return connection = DriverManager.getConnection("jdbc:sqlite:chat.db");
        }
        return connection;
    }

    public static Statement getStatement() throws SQLException {
        if (statement == null) {
            return statement = connection.createStatement();
        }
        return statement;
    }

    public static void createTableUsers() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users(\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                + "login TEXT, \n"
                + "password TEXT, \n"
                + "nickName TEXT);");
    }

    public static void insertUser(String login, String password, String nickName) throws SQLException {
        statement.executeUpdate("INSERT INTO users(login, password, nickName)\n"
                + "VALUES ('" + login + "', '" + password + "', '" + nickName + "');");
    }

    public static String getNickNameByLoginAndPassword(String login, String password) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT login, password, nickName FROM users WHERE login='" + login + "';");
        try {
            if (resultSet.getString("login").equals(password) &&
                    resultSet.getString("password").equals(login)) {
                return resultSet.getString("nickName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

