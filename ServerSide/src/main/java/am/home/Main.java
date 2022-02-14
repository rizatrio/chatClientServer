package am.home;

import am.home.service.db.DBCon;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) {
        try {
            connection = DBCon.getConnection();
            statement = DBCon.getStatement();
            DBCon.createTableUsers();

            //inserts...

            System.out.println(DBCon.getNickNameByLoginAndPassword("A", "A"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBCon.closeConnection();
        }

    }

}

