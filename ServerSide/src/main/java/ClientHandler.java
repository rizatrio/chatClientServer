import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler {

    private static Logger log = Logger.getLogger(MyServer.class.getName());
    private MyServer myServer;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private String name;
//
//    public ClientHandler(MyServer myServer, Socket socket) {
//
//        try {
//            this.myServer = myServer;
//            this.socket = socket;
//            this.dis = new DataInputStream(socket.getInputStream());
//            this.dos = new DataOutputStream(socket.getOutputStream());
//            this.name = "";
//            new Thread(() -> {
//                try {
//                    authentication();
//                    readMsg();
//                } catch (IOException e) {
//                    log.log(Level.INFO, "Authorization failed.", e);
//                    e.printStackTrace();
//                } finally {
//                    closeConnection();
//                }
//            }).start();
//        } catch (IOException e) {
//            log.log(Level.INFO, "Authorization failed.");
//            throw new RuntimeException("Проблемы при создании обработчика клиента.");
//
//        }
//    }
//
//    public void authentication() throws IOException {
//
//        while (true) {
//            String authStr = dis.readUTF();
//            if (authStr.startsWith("/auth")) {
//                String[] parts = authStr.split("\\s");
//                String nick = myServer.getdbAuthService().getNickname(parts[1], parts[2]);
//                if (!nick.isEmpty()) {
//                    if (!myServer.isNickBusy(nick)) {
//                        sendMsg("/authok " + nick);
//                        log.log(Level.INFO, "User logged in.");
//                        name = nick;
//                        myServer.sentMsgToClient(" зашел в чат", name);
//                        myServer.subscribe(this);
//                        return;
//                    } else {
//                        log.log(Level.INFO, "Account is already in use.");
//                        sendMsg("Учетная запись уже используется.");
//                    }
//                } else {
//                    log.log(Level.INFO, "Incorrect login or password.");
//                    sendMsg("Неверные логин или пароль.");
//                }
//            }
//        }
//    }
//
//    public void sendMsg(String msg) {
//
//        try {
//            dos.writeUTF(msg);
//        } catch (IOException e) {
//            log.log(Level.INFO, "Failure to send message.", e);
//            e.printStackTrace();
//        }
//    }
//
//    public void readMsg() throws IOException {
//
//        while (true) {
//            String msgFromClient = dis.readUTF();
//            if (msgFromClient.startsWith("/")) {
//
//                log.log(Level.INFO, "User send command.");
//
//                if (msgFromClient.startsWith("/ou")) {
//                    myServer.sendOnlineClientList(this);
//                    continue;
//                }
//                if (msgFromClient.startsWith("/pm")) {
//                    String[] parts = msgFromClient.trim().split(" ", 3);
//                    myServer.sentMsgToClient(this, parts[1], parts[2]);
//                    continue;
//                }
//                if (msgFromClient.startsWith("/cn")) {
//                    String[] parts2 = msgFromClient.trim().split(" ", 3);
//                    myServer.getdbAuthService().changeNickname(this.getName(), parts2[2].trim());
//                    continue;
//                }
//                if (msgFromClient.equals("/q")) {
//                    sendMsg(msgFromClient);
//                    return;
//                }
//            }
//            myServer.sentMsgToClient(msgFromClient, name);
//            log.log(Level.INFO, "User send a message.");
//        }
//    }
//
//    public String getName() {
//
//        return name;
//    }
//
//    public void closeConnection() {
//
//        myServer.unsubscribe(this);
//        myServer.sentMsgToClient(" вышел из чата", name);
//        try {
//            dis.close();
//        } catch (IOException e) {
//            log.log(Level.INFO, "DataInputStream close.", e);
//            e.printStackTrace();
//        }
//        try {
//            dos.close();
//        } catch (IOException e) {
//            log.log(Level.INFO, "DataOutputStream close.", e);
//            e.printStackTrace();
//        }
//        try {
//            socket.close();
//        } catch (IOException e) {
//            log.log(Level.INFO, "Socket close.", e);
//            e.printStackTrace();
//        }
//    }

    @Override
    public String toString() {
        return name;
    }
}
