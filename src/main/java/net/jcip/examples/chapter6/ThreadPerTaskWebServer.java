package net.jcip.examples.chapter6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/2 19:06
 */
public class ThreadPerTaskWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while(true){
            Socket connection = socket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    handleRequest(connection);
                }
            };
            new Thread(task).start();

        }
    }
    private static void handleRequest(Socket connection){

    }
}
