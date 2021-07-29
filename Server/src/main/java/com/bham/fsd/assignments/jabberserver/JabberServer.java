package com.bham.fsd.assignments.jabberserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class JabberServer implements Runnable {

    private static final int PORT_NUMBER = 44444;
    private static ServerSocket serverSocket = null;

    protected Thread runningThread = null;


    private ClientConnection classClientSocket;
//    here is where i will connect to the clientSocket class


    public static boolean startServer()
    {
        try{
            serverSocket = new ServerSocket(PORT_NUMBER);
            serverSocket.setSoTimeout(300);
            serverSocket.setReuseAddress(true);
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void processClientRequest()
    {
        while (true){
            //this is where i may call the clientSocket depending on how the
            Socket socketClient = null;
            try{
                socketClient = serverSocket.accept();

//                System.out.println("info client " + socketClient.getInetAddress().getCanonicalHostName());
                ClientConnection clientConnection = new ClientConnection(socketClient, new JabberDatabase());
                new Thread(clientConnection).start();

            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Client rejected");
            }

        }
    }



    public void run()
    {
        synchronized (this)
        {
            this.runningThread = Thread.currentThread();
        }
        try{
            if (startServer()){
                System.out.println("the server is running");


            }

            else
                System.out.print("server is not running");

            while (true) {
                processClientRequest();
                Thread.sleep(100);



            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args)
    {
        JabberServer jabberServer = new JabberServer();

        jabberServer.run();


    }

}
