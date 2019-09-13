package net.jcip.examples.chapter7;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/3 14:50
 * 通过重写Thread的interrupt方法来封装非标准的取消操作
 */
public class ReaderThread extends Thread {
    private static final int BUFFER_SIZE = 512;
    private final Socket socket;
    private final InputStream in;
    public ReaderThread(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
    }

    @Override
    public void interrupt() {
        //设置线程中断状态之前先关闭资源。
        try{
            socket.close();
        }catch (Exception ignored){

        }finally {
            super.interrupt();
        }
    }

    @Override
    public void run() {
        try{
            byte[] buf = new byte[BUFFER_SIZE];
            while(true){
                int count = in.read(buf);
                if(count <0){
                    break;
                }else if(count > 0){
                    processBuffer(buf, count);
                }
            }
        }catch (IOException e){
            //允许线程在此处退出
        }
    }
    public void processBuffer(byte[] buf, int count) {
    }
}
