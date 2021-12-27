package am.home.service;


import am.home.handler.ClientHandler;
import am.home.service.interfaces.AuthenticationService;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private static final Integer PORT = 8880;

    private AuthenticationService authenticationService;
    private List<ClientHandler> handlerList;

    public MyServer() {
        System.out.println("Server started");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            authenticationService = new AuthenticationServiceImpl();
            authenticationService.start();
            handlerList = new ArrayList<>();
            while (true) {
                System.out.println("Server wait connection...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                new ClientHandler(this, socket);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            authenticationService.stop();
        }
    }

    public synchronized boolean nickNameIsBusy(String nickName) {
        return handlerList
                .stream()
                .anyMatch(clientHandler -> clientHandler.getNickName().equalsIgnoreCase(nickName));
    }

    public synchronized void sendMessageToClients(String message) {
        handlerList.forEach(clientHandler -> clientHandler.sendMessage(message));
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        handlerList.add(clientHandler);
    }

    public synchronized void unSubscribe(ClientHandler clientHandler) {
        handlerList.remove(clientHandler);
    }

    public AuthenticationService getAuthenticationService() {
        return this.authenticationService;
    }


}
