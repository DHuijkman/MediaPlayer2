package com.desktop.duco.mediaplayer2.util;

import android.app.Activity;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.v7.app.AppCompatActivity;

import java.util.Collection;
import java.util.Iterator;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class GetTestActivity {
    public static AppCompatActivity getActivityInstance(){
        final AppCompatActivity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(() -> {
            Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
            Iterator<Activity> it = resumedActivity.iterator();
            currentActivity[0] = (AppCompatActivity) it.next();
        });

        return currentActivity[0];
    }
}
