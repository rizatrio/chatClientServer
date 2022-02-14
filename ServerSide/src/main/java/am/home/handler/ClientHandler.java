package am.home.handler;

import am.home.service.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ClientHandler {

    private static final Logger log = LogManager.getLogger(ClientHandler.class);
    private MyServer myServer;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String nickName;

    private boolean isDisconnected;

    public ClientHandler(MyServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    Thread.sleep(120000);
                    log.info("Client disconnected by timeout");
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    log.error(e);
                }
                if (!isDisconnected) {
                    sendMessage("/finish");
                }
            }).start();

            new Thread(() -> {
                try {
                    authentication();
                    if (isDisconnected) {
                        receiveMessage();
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    log.error(e);
                } finally {
                    closeConnection();
                    isDisconnected = true;
                }
            }).start();

        } catch (Exception e) {
            //e.printStackTrace();
            log.error(e);
        }

    }

    private void closeConnection() {
        myServer.unSubscribe(this);
        log.info("Client exited the chat");
        try {
            dis.close();
        } catch (IOException e) {
            //e.printStackTrace();
            log.error(e);
        }
        try {
            dos.close();
        } catch (IOException e) {
            //e.printStackTrace();
            log.error(e);
        }
        try {
            socket.close();
        } catch (IOException e) {
            //e.printStackTrace();
            log.error(e);
        }
    }

    public void authentication() throws Exception {
        while(true) {
            String message = dis.readUTF();
            if (message.startsWith("/start")) {
                String [] arr = message.split("-",3);
                if (arr.length != 3) {
                    isDisconnected = false;
                    throw new IllegalAccessException();
                }
                final String nick = myServer
                        .getAuthenticationService()
                        .getNickNameByLoginAndPassword(arr[1].trim(), arr[2].trim());

                if (nick != null) {
                    if (!myServer.nickNameIsBusy(nick)) {
                        isDisconnected = true;
                        sendMessage("/start " + nick);
                        this.nickName = nick;
                        myServer.sendMessageToClients(nickName + " connected to chat");
                        log.info(nickName + " connected to chat");
                        myServer.subscribe(this);
                        return;
                    }
                    else{
                        sendMessage("Your nick now busy!");
                        log.info("Your nick now busy!");
                    }
                } else {
                    sendMessage("Wrong login or password!");
                    log.info("Wrong login or password!");
                }

            }
        }
    }

    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
        } catch (IOException e) {
            //e.printStackTrace();
            log.error(e);
        }
    }

    public void receiveMessage() throws IOException {
        while (true) {
            String message = dis.readUTF();
            if (message.startsWith("/")) {
                if (message.startsWith("/finish")) {
                    myServer.unSubscribe(this);
                    sendMessage(message);
                    myServer.sendMessageToClients(nickName + " exit to chat");
                    return;
                }
                if (message.startsWith("/nick")) {
                    String to = message.split("-", 3)[1];
                    String msg = message.split("-", 3)[2];
                    myServer.sendMessageToClients(this, to, msg);
                }
                if (message.startsWith("/online")) {
                    myServer.getOnlineUsers(this);
                }
                continue;
            }
            myServer.sendMessageToClients(nickName + ": - " + message);
        }
    }

    public String getNickName() {
        return this.nickName;
    }

}
