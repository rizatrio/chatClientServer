package am.home.service;


import am.home.handler.ClientHandler;
import am.home.service.interfaces.AuthenticationService;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyServer {

    private static final Integer PORT = 8880;
    private static final Logger log = LogManager.getLogger(MyServer.class);

    private AuthenticationService authenticationService;
    private List<ClientHandler> handlerList;
    private ExecutorService executorService;

    public MyServer() {
        //System.out.println("Server started");
        log.info("The server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            executorService = Executors.newCachedThreadPool();
            authenticationService = new AuthenticationServiceImpl();
            authenticationService.start();
            handlerList = new ArrayList<>();
            while (true) {
                //System.out.println("Server wait connection...");
                log.info("Server wait connection...");
                Socket socket = serverSocket.accept();
                //System.out.println("Client connected");
                log.info("Client connected");
                new ClientHandler(this, socket);

            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error(e);
        } finally {
            executorService.shutdown();
            authenticationService.stop();
        }
    }

    public synchronized boolean nickNameIsBusy(String nickName) {
        return handlerList
                .stream()
                .anyMatch(clientHandler -> clientHandler.getNickName().equalsIgnoreCase(nickName));
    }

    public synchronized void sendMessageToClients(ClientHandler from, String to, String message) {
        boolean check = true;
        for (ClientHandler clientHandler : handlerList) {
            if (clientHandler.getNickName().equals(to)) {
                clientHandler.sendMessage("From " + from.getNickName() + " " + to + ": " + message);
                log.info("message From " + from.getNickName() + " " + to + ": " + message);
                from.sendMessage(from.getNickName() + " " + to + ": " + message);
                check = false;
                return;
            }
        }
        if (check) {
            from.sendMessage("User with nick " + to + " out in chat!");
            log.info("User with nick " + to + " out in chat!");
        }
    }

    public synchronized void sendMessageToClients(String message) {
        handlerList.forEach(clientHandler -> clientHandler.sendMessage(message));
    }

    public synchronized void getOnlineUsers(ClientHandler ch) {
        String s = new String("Now online:\n");
        for (ClientHandler clientHandler : handlerList) {
            if (ch.getNickName().equals(clientHandler.getNickName())) {
                continue;
            }
            s = s.concat(clientHandler.getNickName() + "\n");
        }
        ch.sendMessage(s);
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

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
