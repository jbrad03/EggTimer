package com.example.eggtimer.hbactivities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.CountDownTimer;

import com.example.eggtimer.R;

public class RunnyActivity extends AppCompatActivity {

    private TextView timerTextView;
    private Button startPauseButton;
    private Button resetButton;

    private ProgressBar circularProgressBar;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 6 * 60 * 1000; // 6 minutes in milliseconds
    private long totalTimeInMillis = 6 * 60 * 1000; // Total time for progress calculation
    private boolean timerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runny);

        timerTextView = findViewById(R.id.timerTextView);
        startPauseButton = findViewById(R.id.startPauseButton);
        resetButton = findViewById(R.id.resetButton);
        circularProgressBar = findViewById(R.id.circularProgressBar);

        updateTimerText();
        updateProgressBar();

        startPauseButton.setOnClickListener(v -> {
            if (timerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        resetButton.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startPauseButton.setText("Start");
                updateProgressBar();
            }
        }.start();

        timerRunning = true;
        startPauseButton.setText("Pause");
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startPauseButton.setText("Start");
    }

    private void resetTimer() {
        timeLeftInMillis = totalTimeInMillis;
        updateTimerText();
        updateProgressBar();
        startPauseButton.setText("Start");
        timerRunning = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    private void updateProgressBar() {
        int progress = (int) (100 * timeLeftInMillis / totalTimeInMillis);
        circularProgressBar.setProgress(progress);
    }
}