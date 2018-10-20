package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private static JTextArea textArea;
    private static JTextField textField;
    private static BufferedReader reader;
    private static PrintWriter writer;
    private static String nick;

    public static void main(String[] args) {

        nick = JOptionPane.showInputDialog("Enter nick");
        start();

    }

    private static void start() {

        ImageIcon imageIcon = new ImageIcon("src/main/java/logo.gif");
        JOptionPane.showMessageDialog(null, imageIcon, "Messenger", JOptionPane.WARNING_MESSAGE);

        JFrame frame = new JFrame("ClientMessenger");
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        textArea = new JTextArea(15, 30);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textField = new JTextField(20);
        JButton send = new JButton("Send");
        JButton refresh = new JButton("Refresh");

        send.addActionListener(new Sender());

        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writer.print("");
            }
        });



        panel.add(scrollPane);
        panel.add(textField);
        panel.add(send);
        setConnection();

        Thread thread = new Thread(new Listener());
        thread.start();

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.NORTH, refresh);
        frame.setSize(400, 360);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private static class Listener  implements Runnable {
        public void run() {
            String messageGet;
            try {
                while ((messageGet = reader.readLine()) != null) {
                    textArea.append(messageGet + "\n");
                }
            } catch (IOException e) {

            }
        }
    }

    private static class Sender implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String message = nick + ": " + textField.getText();
            writer.println(message);
            writer.flush();

            textField.setText("");
            textField.requestFocus();
        }
    }

    private static void setConnection() {
        try {
            Socket socket = new Socket("127.0.0.1", 8081);
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(inputStreamReader);
            writer = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
