package net.jcip.examples.chapter6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/2 19:04
 */
public class SingleThreadWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while(true){
            Socket connection = socket.accept();
            handleRequest(connection);
        }
    }
    private static void handleRequest(Socket connection){

    }
}
