package com.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.HashMap;


public class Server {
    public static void main(String[] args){
        Server server = new Server(8000);
        server.startListening();
    }
    private int port;

    public Server(int port){
        this.port = port;
    }
     public void startListening(){
        System.out.println("[Server] Server starten");

        new Thread(new Runnable(){
            @Override
            public void run(){
                HashMap<String, String> wetterdaten = new HashMap<String, String>();
                wetterdaten.put("Leipzig", "Hagel, -5 Grad.");
                wetterdaten.put("Zeitz", "neblig, 24 Grad.");
                wetterdaten.put("Dresden", "Regen, 19 Grad.");
                wetterdaten.put("Zwickau", "windig, 0 Grad.");
                wetterdaten.put("Freiberg", "sonnig, 20 Grad.");

                while (true) {
                    try {
                        //Server lauscht auf dem Port
                        ServerSocket serverSocket = new ServerSocket(port);
                        //es wird auf Verbindung gewartet -> wenn vorhanden Verbindung akzeptieren und im remoteClientSocket speichern
                        System.out.println("[Server] Warten auf Verbindung ...");
                        Socket remoteClientSocket = serverSocket.accept();
                        System.out.println("[Server] Client verbunden: " + remoteClientSocket.getRemoteSocketAddress());
                        //Daten werden aus remoteClientSocket gelesen
                            //InputStream entspricht den, aus der Socket eingeströmten, Daten
                            //InputStream wird InputStreamReader übergeben
                            //InputStreamReader wird BufferedReader übergeben, welcher damit Daten gepuffert vorladen kann, wenn welche zur Verfügung stehen
                            //BufferedReader gibt Daten an Scanner ab, wenn dieser welche benötigt
                        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(remoteClientSocket.getInputStream())));

                        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(remoteClientSocket.getOutputStream()));
                        if(scanner.hasNextLine()) {
                            //Zeile ausdrucken
                            String outputToClient = scanner.nextLine();
                            System.out.println("[Server] Neue Nachicht vom Client: " + outputToClient);
                            if(wetterdaten.get(outputToClient) != null){
                                printWriter.println("In " + outputToClient + " ist das Wetter " + wetterdaten.get(outputToClient));
                            }else{
                                printWriter.println("Für diese Stadt gibt es leider keine Wetterdaten.");
                            }
                        }
                        printWriter.flush();
                        //Verbindung schließen
                        scanner.close();
                        printWriter.close();
                        remoteClientSocket.close();
                        serverSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
     }
}
