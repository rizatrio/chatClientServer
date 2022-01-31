
/*
 *    Rizat.Orazalina created on 21.12.2021
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class EchoClient extends JFrame {

    private final String SERVER_ADDRESS = "127.0.0.1";
    private final Integer SERVER_PORT = 8880;
    private JTextField msgInputField;
    private JTextArea chatArea;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private HistoryHandler historyHandler;

    public EchoClient() throws Exception {
        historyHandler = new HistoryHandler();
        connectionServer();
        prepareGUI();
    }

    private void connectionServer() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    String message = dis.readUTF();
                    if (message.startsWith("/start")) {
                        historyHandler.history(message.split(" ")[1] + ".txt");
                        historyHandler.getHistoryList().stream().forEach((history) -> chatArea.append(history + "\n"));
                        historyHandler.getHistoryList().clear();
                        chatArea.append(message + "\n");
                        break; //not return
                    }
                    if (message.equalsIgnoreCase("/finish")) {
                        chatArea.append(message + "\n");
                        break; //not return
                    }
                    historyHandler.writeHistory(message);
                    chatArea.append(message + "\n");

                }

                while(true) {
                    String text = dis.readUTF();
                    if (text.startsWith("/finish")) {
                        closeConnection();
                        msgInputField.setText("");
                        msgInputField.setEditable(false);
                        break;
                    }
                    historyHandler.writeHistory(text);
                    chatArea.append(text + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Wrong connection to server");
            }
        });

        thread.setDaemon(true);
        thread.start();


    }


    public void closeConnection() {
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
        historyHandler.closeStreams();
    }

    private void sendMessageToServer() {
        String msg = msgInputField.getText();
        if (msg != null || !msg.trim().isEmpty()) {
            try {
                dos.writeUTF(msg);
                msgInputField.setText("");
                msgInputField.grabFocus();
                if (msg.startsWith("/finish")) {
                    chatArea.append("вы закрыли соединение\n");
                    msgInputField.setText("");
                    msgInputField.setEditable(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "You send incorrect message");
            }
        }
    }

    public void prepareGUI() {

        // Параметры окна
        setBounds(600, 300, 300, 300);
        setTitle("Клиент");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Текстовое поле для вывода сообщений
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Нижняя панель с полем для ввода сообщений и кнопкой отправки сообщений
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton btnSendMsg = new JButton("Отправить");
        bottomPanel.add(btnSendMsg, BorderLayout.EAST);
        msgInputField = new JTextField();
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(msgInputField, BorderLayout.CENTER);
        btnSendMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToServer();
            }
        });
        msgInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToServer();
            }
        });

        // Настраиваем действие на закрытие окна
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    dos.writeUTF("/finish");
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });
        setVisible(true);
    }


}

