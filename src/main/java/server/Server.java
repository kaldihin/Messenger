package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {

    private static ArrayList<PrintWriter> streams;

    public static void main(String[] args) {
        start();
    }

    private static void start() {

        streams = new ArrayList<PrintWriter>();

        try {
            ServerSocket serverSocket = new ServerSocket(8081);
            while (true) {
                Socket socketClient = serverSocket.accept();
                System.out.println("Connection successful!");
                PrintWriter writer = new PrintWriter(socketClient.getOutputStream());
                streams.add(writer);

                Thread thread = new Thread(new Listener(socketClient));
                thread.start();
            }
        } catch (Exception e) {

        }

    }

    private static void tellEveryOne(String message) {
        int x = message.indexOf(":");
        String nick = message.substring(0, x);

        Iterator<PrintWriter> iterator = streams.iterator();
        while (iterator.hasNext()) {
            try {
                PrintWriter writer = iterator.next();
                writer.println();
                writer.flush();
            } catch (Exception e) {

            }
        }
    }

    private static class Listener implements Runnable {

        BufferedReader reader;

        Listener(Socket socket) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(inputStreamReader);
            } catch (IOException e) {

            }

        }

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println(message);
                    tellEveryOne(message);
                }
            } catch (Exception e) {

            }
        }
    }
}
