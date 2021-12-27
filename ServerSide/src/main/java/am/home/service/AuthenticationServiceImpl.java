package am.home.service;

import am.home.service.interfaces.AuthenticationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticationServiceImpl implements AuthenticationService {

    private List<UserEntity> userEntityList;

    public AuthenticationServiceImpl() {
        this.userEntityList = new ArrayList<>();
        userEntityList.add(new UserEntity("A","A","A"));
        userEntityList.add(new UserEntity("B","B","B"));
        userEntityList.add(new UserEntity("C","C","C"));
    }

    @Override
    public void start() {
        System.out.println("Authentication service start");
    }

    @Override
    public void stop() {
        System.out.println("Authentication service stop");
    }

    @Override
    public String getNickNameByLoginAndPassword(String login, String password) {

        for (UserEntity entity : userEntityList) {
            if (entity.login.equals(login) && entity.password.equals(password)) {
                return  entity.nickName;
            }
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
