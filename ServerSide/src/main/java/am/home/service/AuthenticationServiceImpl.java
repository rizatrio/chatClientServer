package am.home.service;

import am.home.service.db.DBCon;
import am.home.service.interfaces.AuthenticationService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticationServiceImpl implements AuthenticationService {

    private List<UserEntity> userEntityList;
    private static Connection connection;
    private static Statement statement;

    public AuthenticationServiceImpl() {
//        this.userEntityList = new ArrayList<>();
//        userEntityList.add(new UserEntity("A","A","A"));
//        userEntityList.add(new UserEntity("B","B","B"));
//        userEntityList.add(new UserEntity("C","C","C"));
    }

    @Override
    public void start() {
        try {
            connection = DBCon.getConnection();
            statement = DBCon.getStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            DBCon.closeConnection();
        }
        System.out.println("Authentication service start");
    }

    @Override
    public void stop() {
        System.out.println("Authentication service stop");
        DBCon.closeConnection();
    }

    @Override
    public String getNickNameByLoginAndPassword(String login, String password) {

//        for (UserEntity entity : userEntityList) {
//            if (entity.login.equals(login) && entity.password.equals(password)) {
//                return  entity.nickName;
//            }
//        }
//        return null;
        try {
            return DBCon.getNickNameByLoginAndPassword(login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class UserEntity {
        private String login;
        private String password;
        private String nickName;

        public UserEntity(String login, String password, String nickName) {
            this.login = login;
            this.password = password;
            this.nickName = nickName;
        }
    }
}
