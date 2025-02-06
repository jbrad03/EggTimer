package com.example.eggtimer.hbactivities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.CountDownTimer;
import android.animation.ObjectAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private ImageView chickenImageView;
    private ObjectAnimator chickenAnimator;
    private long chickenCurrentPosition = 0L;  // Tracks chicken’s animation progress for pausing/resuming

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runny);

        timerTextView = findViewById(R.id.timerTextView);
        startPauseButton = findViewById(R.id.startPauseButton);
        resetButton = findViewById(R.id.resetButton);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        chickenImageView = findViewById(R.id.chickenImageView);

        updateTimerText();
        updateProgressBar();

        // Initialize the chicken animation (smoothly moving across the screen)
        chickenAnimator = ObjectAnimator.ofFloat(chickenImageView, "translationX", 0f, getScreenWidth()); // Adjust 800f as necessary
        chickenAnimator.setDuration(6000);  // Matches the total duration of the animation
        chickenAnimator.setRepeatCount(ObjectAnimator.INFINITE);  // Keep looping
        chickenAnimator.setInterpolator(null);  // Smooth linear animation

        startPauseButton.setOnClickListener(v -> {
            if (timerRunning) {
                pauseTimer();
                pauseChickenAnimation();
            } else {
                startTimer();
                resumeChickenAnimation();
            }
        });

        resetButton.setOnClickListener(v -> {
            resetTimer();
            resetChickenPosition();
        });
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
        chickenAnimator.start();  // Ensure the chicken starts moving when the timer starts
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
        resetChickenPosition();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    private void updateProgressBar() {
        int progress = (int) (10000 * timeLeftInMillis / totalTimeInMillis);
        circularProgressBar.setProgress(progress);
    }

    private void pauseChickenAnimation() {
        // Save the current animation position and pause it
        chickenCurrentPosition = chickenAnimator.getCurrentPlayTime();
        chickenAnimator.pause();
    }

    private void resumeChickenAnimation() {
        // Resume animation from the saved position
        chickenAnimator.setCurrentPlayTime(chickenCurrentPosition);
        chickenAnimator.resume();
    }

    private void resetChickenPosition() {
        chickenAnimator.cancel();  // Cancel any ongoing animation
        chickenImageView.setTranslationX(0f);  // Move the chicken back to the start
        chickenCurrentPosition = 0L;  // Reset the tracked animation position
    }
    private float getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }
}