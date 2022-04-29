package cz.underthetree.meetmeattheball;

import android.app.Application;

public class MyApp extends Application {
    private int talkingCounter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setTalkingCounter(int val) {
        talkingCounter = val;
    }

    public int getTalkingCounter()
    {
        return talkingCounter;
    }
}
