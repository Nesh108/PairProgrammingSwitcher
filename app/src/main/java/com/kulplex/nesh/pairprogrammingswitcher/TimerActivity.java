package com.kulplex.nesh.pairprogrammingswitcher;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.Random;

public class TimerActivity extends AppCompatActivity  implements MediaPlayer.OnErrorListener {

    EditText timerEditText;
    int durationMinutes,  durationSeconds, intervalMinutes, intervalSeconds;
    CountDownTimer Counter1;
    ImageButton pauseButton, resumeButton;

    long remainingTimer;
    boolean isTimerPaused = false;

    int[][] swapSounds = {{R.raw.swap1, 12}, {R.raw.swap2, 8}, {R.raw.swap3, 6}, {R.raw.swap4, 8}, {R.raw.swap5, 10}, {R.raw.swap6, 4}, {R.raw.swap7, 12}, {R.raw.swap8, 8}, {R.raw.swap9, 8}, {R.raw.swap10, 8}, {R.raw.swap11, 8}, {R.raw.swap12, 8}};
    int[][] timesupSounds = {{R.raw.timesup1, 9},{R.raw.timesup2, 8},{R.raw.timesup3, 7},{R.raw.timesup4, 7},{R.raw.timesup5_rare, 1},{R.raw.timesup6, 8},{R.raw.timesup7, 8},{R.raw.timesup8, 8},{R.raw.timesup9, 8},{R.raw.timesup10, 7},{R.raw.timesup11, 6},{R.raw.timesup12, 6},{R.raw.timesup13, 6},{R.raw.timesup14, 2},{R.raw.timesup15, 2},{R.raw.timesup16, 2},{R.raw.timesup17, 1}};
    int[][] goSounds = {{R.raw.go1, 10},{R.raw.go2, 10},{R.raw.go3, 10},{R.raw.go4, 10},{R.raw.go5, 10},{R.raw.go6, 10},{R.raw.go7, 10},{R.raw.go8, 5},{R.raw.go9, 5},{R.raw.go10, 5},{R.raw.go11, 5},{R.raw.go12, 5},{R.raw.go13, 2},{R.raw.go14, 2},{R.raw.go15, 1}};
    int backgroundSound = R.raw.mr_sunny_face;
    MediaPlayer mp;
    Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mp = MediaPlayer.create(this, backgroundSound);
        timerEditText = findViewById(R.id.timerEditText);
        timerEditText.setKeyListener(null);
        pauseButton = findViewById(R.id.pauseButton);
        resumeButton = findViewById(R.id.resumeButton);

        durationMinutes = getIntent().getExtras().getInt("duration_minutes");
        durationSeconds = getIntent().getExtras().getInt("duration_seconds");
        intervalMinutes = getIntent().getExtras().getInt("interval_minutes");
        intervalSeconds = getIntent().getExtras().getInt("interval_seconds");

        timerEditText.setText(durationMinutes + ":" + durationSeconds);

        SetupTimer((durationMinutes * 60 + durationSeconds) * 1000, true, false);
    }

    void OnStopTimer(View v) {
        StopSound();
        Counter1.cancel();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent i = new Intent(v.getContext(), SetupActivity.class);
        i.putExtra("duration_minutes", durationMinutes);
        i.putExtra("duration_seconds", durationSeconds);
        i.putExtra("interval_minutes", intervalMinutes);
        i.putExtra("interval_seconds", intervalSeconds);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    void OnResumeTimer(View v) {
        if(isTimerPaused) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            SetupTimer(remainingTimer, true, false);
            isTimerPaused = false;
            resumeButton.setVisibility(View.INVISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
        }
    }

    void OnPauseTimer(View v) {
        StopSound();
        Counter1.cancel();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isTimerPaused = true;
        pauseButton.setVisibility(View.INVISIBLE);
        resumeButton.setVisibility(View.VISIBLE);
    }

    void OnSwapTimer(View v) {
        PlayRandomSound(swapSounds);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                PlayBackgroundSound();
            }
        });
        Counter1.cancel();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        SetupTimer((intervalMinutes * 60 + intervalSeconds) * 1000, true, true);

        if(resumeButton.getVisibility() == View.VISIBLE) {
            resumeButton.setVisibility(View.INVISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
        }
    }

    void SetupTimer(long duration, boolean startTimer, final boolean swapper) {
        Counter1 = new CountDownTimer(duration, 100) {
            public void onTick(long millisUntilFinished) {
                int minutesLeft = (int) (millisUntilFinished / 1000 / 60);
                int secondsLeft = (int) ((millisUntilFinished / 1000) - minutesLeft * 60);
                timerEditText.setText(((int) (minutesLeft / 10) == 0 ? "0" : "") + minutesLeft + ":" + ((int) (secondsLeft / 10) == 0 ? "0" : "") + secondsLeft);
                remainingTimer = millisUntilFinished;
            }

            public void onFinish() {
                if (swapper) {
                    StopSound();
                    SetupTimer((durationMinutes * 60 + durationSeconds) * 1000, true, false);
                } else {
                    PlayRandomSound(timesupSounds);
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            PlayBackgroundSound();
                            SetupTimer((intervalMinutes * 60 + intervalSeconds) * 1000, true, true);
                        }
                    });

                }
            }

        };

        if(startTimer && !swapper) {
            PlayRandomSound(goSounds);
        }
        if (startTimer) {
            Counter1.start();
        }
    }

    void PlayRandomSound(int[][] soundsList) {
        int randomValue = r.nextInt(100);
        for(int[] sounds : soundsList) {
            randomValue -= sounds[1];
            if(randomValue < 0) {
                PlaySound(sounds[0], false);
                break;
            }
        }
    }

    void PlayBackgroundSound() {
        PlaySound(backgroundSound, true);
    }

    void PlaySound(int id, boolean loop) {
        try {
            StopSound();
            mp = MediaPlayer.create(this, id);
            mp.setOnErrorListener(this);
            mp.setLooping(loop);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mp.release();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void StopSound() {
        try{
            if(mp != null && mp.isPlaying()) {
                mp.stop();
            }
        } catch (Exception e) {
        }
        if(mp != null) {
            mp.release();
            mp = null;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        StopSound();
        return false;
    }
}
