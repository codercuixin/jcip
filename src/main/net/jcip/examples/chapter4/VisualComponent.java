package net.jcip.examples.chapter4;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/14 16:54
 * 将线程安全性委托给多个独立的状态变量
 */
public class VisualComponent {
    private final List<KeyListener> keyListeners = new CopyOnWriteArrayList<>();
    private final List<MouseListener> mouseListeners = new CopyOnWriteArrayList<>();

    public void addKeyListener(KeyListener listener){
        keyListeners.add(listener);
    }
    public void addMouseListener(MouseListener mouseListener){
        mouseListeners.add(mouseListener);
    }
    public void removeKeyListener(KeyListener listener){
        keyListeners.remove(listener);
    }
    public void removeMouseListener(MouseListener mouseListener){
        mouseListeners.remove(mouseListener);
    }

}
