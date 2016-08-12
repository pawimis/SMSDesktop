package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by pmisi on 11.08.2016.
 */
public class Server extends Thread {
    private boolean running = false;
    private int port;
    private PrintWriter mOut;



    public OnMessageReceived messageListener;
    public Server(String serverPort,OnMessageReceived messageListener){
        System.out.println("S: constructor...");
        this.port = Integer.parseInt(serverPort);
        this.messageListener = messageListener;
    }
    @Override
    public void run() {
        super.run();
        running = true;

        try{
            System.out.println("S: Connecting...");
            ServerSocket serverSocket = new ServerSocket(port);
            Socket client = serverSocket.accept();
            System.out.println("S: Receiving...");
            try{
                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

                BufferedReader mIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while (running) {
                    String message = mIn.readLine();

                    if (message != null && messageListener != null) {
                        //call the method messageReceived from ServerBoard class
                        messageListener.messageReceived(message);
                    }
                }
            }catch (Exception e) {
                System.out.println("S: Error");
                e.printStackTrace();
            }finally {
                client.close();
                System.out.println("S: Done.");
            }
        }catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
        }
    }
    public interface OnMessageReceived {
        void messageReceived(String message);
    }

    public void CloseConnection() {
        running = false;
    }
}
