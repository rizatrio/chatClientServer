package am.home.handler;

import am.home.service.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String nickName;

    public ClientHandler(MyServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    authentication();
                    receiveMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void closeConnection() {
        myServer.unSubscribe(this);
        try {
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authentication() throws Exception {
        while(true) {
            String message = dis.readUTF();
            if (message.startsWith("/start")) {
                String [] arr = message.split("-",3);
                if (arr.length != 3) {
                    throw new IllegalAccessException();
                }
                final String nick = myServer
                        .getAuthenticationService()
                        .getNickNameByLoginAndPassword(arr[1].trim(), arr[2].trim());

                if (nick != null) {
                    if (!myServer.nickNameIsBusy(nick)) {
                        sendMessage("/start " + nick);
                        this.nickName = nick;
                        myServer.sendMessageToClients(nickName + " connected to chat");
                        myServer.subscribe(this);
                          return;
                    }
                    else{
                        sendMessage("Your nick now busy!");
                    }
                } else {
                    sendMessage("Wrong login or password!");
                }

            }
        }
    }

    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage() throws IOException {
        while (true) {
            String message = dis.readUTF();
            if (message.startsWith("/finish")) {
                myServer.sendMessageToClients(nickName + "exit to chat");
                return;
            } else {
                myServer.sendMessageToClients(nickName + ": - " + message);
            }
        }
    }

    public String getNickName() {
        return this.nickName;
    }

}
