package com.kulplex.nesh.pairprogrammingswitcher;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

public class TimerActivity extends AppCompatActivity {

    EditText timerEditText;
    int durationMinutes,  durationSeconds, intervalMinutes, intervalSeconds;
    CountDownTimer Counter1;
    ImageButton pauseButton, resumeButton;

    long remainingTimer;
    boolean isTimerPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
        Counter1.cancel();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isTimerPaused = true;
        pauseButton.setVisibility(View.INVISIBLE);
        resumeButton.setVisibility(View.VISIBLE);
    }

    void OnSwapTimer(View v) {
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
                timerEditText.setText(((int)(minutesLeft/10) == 0 ? "0" : "") + minutesLeft + ":" + ((int)(secondsLeft/10) == 0 ? "0" : "") + secondsLeft);
                remainingTimer = millisUntilFinished;
            }

            public void onFinish() {
                if(swapper) {
                    SetupTimer((durationMinutes * 60 + durationSeconds) * 1000, true, false);
                }
            }

        };

        if(startTimer) {
            Counter1.start();
        }
    }
}
