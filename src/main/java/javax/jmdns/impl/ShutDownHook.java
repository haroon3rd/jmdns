package javax.jmdns.impl;

import java.util.List;

public class ShutDownHook extends Thread{

    List<Terminable> tList;
    public ShutDownHook(List<Terminable> tList) {
        this.tList = tList;
    }

    @Override
    public void run() {
        for (Terminable t:tList) {
            try {
                t.terminate();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}