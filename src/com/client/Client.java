package com.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        Client client = new Client("localhost", 8000);

        while (true) {
            try {
                Scanner in = new Scanner(System.in);
                System.out.println("Gib deine Stadt ein:");
                String stadt = in.nextLine();
                client.sendMessage(stadt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private InetSocketAddress address;

    public Client(String hostname, int port){
        address = new InetSocketAddress(hostname, port);
    }
    public void sendMessage(String message){
        try {
            System.out.println("[Client] Verbinde zu Server ...");
            Socket socket = new Socket();
            socket.connect(address, 5000);
            System.out.println("[Client] Verbunden.");

            System.out.println("[Client] Sende Nachicht ...");
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            printWriter.println(message);
            printWriter.flush();
            System.out.println("[Client] Nachicht gesendet.");

            Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            if(scanner.hasNextLine()){
                System.out.println("[Client] Antwort vom Server: " + scanner.nextLine());
            }

            //Verbindung schließen
            printWriter.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
