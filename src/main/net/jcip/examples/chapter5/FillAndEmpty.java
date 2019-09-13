package net.jcip.examples.chapter5;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Exchanger，双方栅栏（Two party）,各方在栅栏位置交换数据。
 */

public class FillAndEmpty {
    Exchanger<DataBuffer> exchanger = new Exchanger<DataBuffer>();

    public static void main(String[] args) {
        FillAndEmpty fillAndEmpty = new FillAndEmpty();
        fillAndEmpty.start();
    }

    void start() {
        new Thread(new FillingLoop()).start();
        new Thread(new EmptyingLoop()).start();
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class DataBuffer {
        private List<String> buffers = new ArrayList<>();
        private int size;

        public boolean isFull() {
            return buffers.size() == size;
        }

        public boolean isEmpty() {
            return buffers.size() == 0;
        }
    }

    class FillingLoop implements Runnable {
        @Override
        public void run() {
            DataBuffer currentBuffer = new DataBuffer();
            currentBuffer.setSize(2);
            try {
                int i = 0;
                while (true) {
                    currentBuffer.getBuffers().add("test" + i);
                    i++;
                    if (currentBuffer.isFull()) {
                        System.out.println("filling size : " + currentBuffer.getBuffers().size());
                        currentBuffer.getBuffers().forEach(s -> System.out.println("filing: " + s));
                        //Waits for another thread to arrive at this exchange point (unless the current thread is interrupted),
                        // and then transfers the given object to it, receiving its object in return.
                        currentBuffer = exchanger.exchange(currentBuffer);
                    }
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }


    }

    class EmptyingLoop implements Runnable {
        @Override
        public void run() {
            DataBuffer currentBuffer = new DataBuffer();
            currentBuffer.setSize(2);
            try {
                while (true) {
                    if (!currentBuffer.isEmpty()) {
                        currentBuffer.getBuffers().remove(0);
                    }
                    if (currentBuffer.isEmpty()) {
                        currentBuffer = exchanger.exchange(currentBuffer);
                        System.out.println("emptying size : " + currentBuffer.getBuffers().size());
                        currentBuffer.getBuffers().forEach(s -> System.out.println("emptying: " + s));
                    }
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}