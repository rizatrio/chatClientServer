/*
 *    Rizat.Orazalina created on 21.12.2021
 */

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer extends JFrame {
    private final Integer SERVER_PORT = 8880;
    Socket socket = null;
    private JTextField msgInputField;
    private JTextArea chatArea;
    private DataInputStream dis;
    private DataOutputStream dos;

    public MyServer() {
        System.out.println("Server start");
        startServer();
        // prepareGUI();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Сервер запущен, ожидаем подключения...");
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                while (true) {
                    try {
                        String serserMessage = new BufferedReader(new InputStreamReader(System.in)).readLine();
                        dos.writeUTF("Сервер сказал: " + serserMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            while (true) {

                String clientMessage = dis.readUTF();
                System.out.println("Клиент сказал: " + clientMessage);

                if (clientMessage.equals("/finish")) {
                    closeConnection(socket, dis, dos);
                    break;
                }
                dos.writeUTF("Клиент сказал: " + clientMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(Socket socket, DataInputStream dis, DataOutputStream dos) {
        try {
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
//
//    private void sendMessageToClient() {
//        String msg = msgInputField.getText();
//        if (msg != null || !msg.trim().isEmpty()) {
//            try {
//                dos.writeUTF(msg);
//                msgInputField.setText("");
//                msgInputField.grabFocus();
//            } catch (IOException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(null, "You send incorrect message");
//            }
//        }
//    }
//
//
//    public void prepareGUI() {
//
//        // Параметры окна
//        setBounds(600, 300, 500, 500);
//        setTitle("Сервер");
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//        // Текстовое поле для вывода сообщений
//        chatArea = new JTextArea();
//        chatArea.setEditable(false);
//        chatArea.setLineWrap(true);
//        add(new JScrollPane(chatArea), BorderLayout.CENTER);
//
//        // Нижняя панель с полем для ввода сообщений и кнопкой отправки сообщений
//        JPanel bottomPanel = new JPanel(new BorderLayout());
//        JButton btnSendMsg = new JButton("Отправить");
//        bottomPanel.add(btnSendMsg, BorderLayout.EAST);
//        msgInputField = new JTextField();
//        add(bottomPanel, BorderLayout.SOUTH);
//        bottomPanel.add(msgInputField, BorderLayout.CENTER);
//        btnSendMsg.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                sendMessageToClient();
//            }
//        });
//        msgInputField.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                sendMessageToClient();
//            }
//        });
//
//        // Настраиваем действие на закрытие окна
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
//                try {
//                    dos.writeUTF("/finish");
//                } catch (IOException exc) {
//                    exc.printStackTrace();
//                }
//            }
//        });
//        setVisible(true);
//    }
//

}
