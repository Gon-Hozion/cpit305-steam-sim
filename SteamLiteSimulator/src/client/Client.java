package client;

import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 5000);

            System.out.println("Connected to server!");

            socket.close();

        } catch (IOException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}