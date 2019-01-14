package com.desktop.duco.mediaplayer2.util;

import static java.lang.Thread.sleep;

public class CustomSleep {
    public static void customSleep(int time){
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
