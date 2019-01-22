package com.tpillon.audioapplication;

import android.os.Handler;

public class RunnableLoop {

    private final Handler handler;
    private final Runnable runnable;

    public RunnableLoop(final Runnable runnableArg, final long loopDelayMillis)
    {
        this.handler = new Handler();
        this.runnable=new Runnable() {
            @Override
            public void run() {
                runnableArg.run();
                handler.postDelayed(this, loopDelayMillis);
            }
        };
    }

    public void run()
    {
        runnable.run();
    }

    public void close()
    {
        handler.removeCallbacks(runnable);
    }
}
