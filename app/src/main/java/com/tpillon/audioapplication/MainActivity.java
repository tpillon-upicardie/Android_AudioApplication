package com.tpillon.audioapplication;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;

/**
 * activity to manage song
 * can start and stop song playing
 * show the progress of the playing
 * @author tpillon
 */
public class MainActivity extends AppCompatActivity {

    /**
     * song file for playing
     * SOURCE : https://freemp3downloads.online/download?url=2GADx4Hy-Gg
     */
    private static final int _fileID = R.string.song_manager;

    /**
     * control to inform if the song is playing or not
     */
    private Switch _switchControl;

    /**
     * loop to update progress bar value regularly
     */
    private RunnableLoop _loop;

    /**
     * song player
     * SOURCE : https://developer.android.com/reference/android/media/MediaPlayer
     */
    private MediaPlayer _mediaPlayer;

    /**
     * flag to inform if player is running during last pause
     */
    private boolean _isPlayingBeforePause;

    /**
     * function called during creation of the activity
     *
     * default activity setting
     * update title in the top bar
     * instantiate media player
     * collect switch and apply checked event
     * setup progressive bar
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(_fileID);

        _mediaPlayer = MediaPlayer.create(this, R.raw.song);

        _switchControl = findViewById(R.id.runningSwitch);
        _switchControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            // Called during switch value changed
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updatePlayerState(isChecked);
            }
        });

        setProgressiveBar();
    }

    /**
     * function called during activity completely destroyed
     *
     * dispose loop
     * dispose media player
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        _loop.close();

        _mediaPlayer.stop();
        _mediaPlayer.release();
    }

    /**
     * function called during activity paused
     *
     * save current state
     * apply pause
     */
     @Override
    protected void onPause() {
        super.onPause();

        _isPlayingBeforePause = _mediaPlayer.isPlaying();
        _mediaPlayer.pause();
    }

    /**
     * function called during activity resumed
     *
     * if run during last pause
     * => start player
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (_isPlayingBeforePause) {
            _mediaPlayer.start();
        }
    }

    /**
     * Function to initialise progressive bar
     *
     * collect progress bar in view
     * update max value with media duration
     *
     * instantiate loop to update progress value
     * start loop
     */
    private void setProgressiveBar() {
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        int duration = _mediaPlayer.getDuration();
        progressBar.setMax(duration);

        _loop = new RunnableLoop(new Runnable() {
            @Override
            public void run() {
                // update progress value in the loop
                progressBar.setProgress(_mediaPlayer.getCurrentPosition());
            }
        }, 500);

        _loop.run();
    }

    /**
     * function called during click on stop button
     *
     * stop the player
     * put the position to 0
     * uncheck switch
     */
    public void clickOnStop(View view) {
        _mediaPlayer.pause();
        _mediaPlayer.seekTo(0);
        _switchControl.setChecked(false);
    }

    /**
     * Function to start or pause the player
     *
     * start player if argument is true
     * stop player if argument is false
     */
    private void updatePlayerState(boolean isRunning) {
        if(isRunning) {
            _mediaPlayer.start();
        } else {
            _mediaPlayer.pause();
        }
    }
}