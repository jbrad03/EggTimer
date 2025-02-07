package com.example.eggtimer.hbactivities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.CountDownTimer;
import android.animation.ObjectAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.graphics.drawable.AnimationDrawable;

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
    private AnimationDrawable chickenWalkingAnimation;  // Walking animation
    private AnimationDrawable chickenSleepingAnimation;  // Sleeping animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runny);

        timerTextView = findViewById(R.id.timerTextView);
        startPauseButton = findViewById(R.id.startPauseButton);
        resetButton = findViewById(R.id.resetButton);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        chickenImageView = findViewById(R.id.chickenImageView);

        // Initialize both animations
        chickenImageView.setImageResource(R.drawable.chicken_walk_animation);
        chickenWalkingAnimation = (AnimationDrawable) chickenImageView.getDrawable();

        chickenImageView.setImageResource(R.drawable.chicken_sleep_animation);
        chickenSleepingAnimation = (AnimationDrawable) chickenImageView.getDrawable();

        // Set the initial animation to sleeping
        chickenImageView.setImageDrawable(chickenSleepingAnimation);
        chickenSleepingAnimation.start();

        updateTimerText();
        updateProgressBar();

        // Initialize the chicken movement animation
        chickenAnimator = ObjectAnimator.ofFloat(chickenImageView, "translationX", 0f, getScreenWidth() - 50f);
        chickenAnimator.setDuration(6000);  // Matches the total duration of the animation
        chickenAnimator.setRepeatCount(ObjectAnimator.INFINITE);  // Keep looping
        chickenAnimator.setInterpolator(null);  // Smooth linear animation

        startPauseButton.setOnClickListener(v -> {
            if (timerRunning) {
                pauseTimer();
                pauseChickenAnimation();  // Switch to the sleeping animation when paused
            } else {
                startTimer();
                switchToWalkingAnimation();  // Switch to the walking animation when starting
            }
        });

        resetButton.setOnClickListener(v -> {
            resetTimer();
            switchToSleepingAnimation();  // Switch to sleeping animation on reset
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

                // Forcefully set the timer to 00:00 and update the progress bar
                timeLeftInMillis = 0;
                updateTimerText();
                updateProgressBar();
                switchToSleepingAnimation();
            }
        }.start();

        timerRunning = true;
        startPauseButton.setText("Pause");
        switchToWalkingAnimation();  // Ensure the walking animation starts
        chickenAnimator.start();  // Start the chicken’s movement
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

    private void switchToSleepingAnimation() {
        chickenAnimator.pause();  // Pause the chicken’s movement
        chickenImageView.setImageDrawable(chickenSleepingAnimation);  // Switch to sleeping animation
        chickenSleepingAnimation.start();  // Start the sleeping animation
    }

    private void switchToWalkingAnimation() {
        chickenImageView.setImageDrawable(chickenWalkingAnimation);  // Switch to walking animation
        chickenWalkingAnimation.start();  // Start or resume the walking animation
        chickenAnimator.setCurrentPlayTime(chickenCurrentPosition);  // Resume the chicken’s movement from where it left off
        chickenAnimator.start();
    }

    private void pauseChickenAnimation() {
        chickenCurrentPosition = chickenAnimator.getCurrentPlayTime();  // Save the current animation position
        chickenAnimator.pause();
        chickenWalkingAnimation.stop();  // Stop the walking animation
        switchToSleepingAnimation();
    }

    private void resetChickenPosition() {
        chickenAnimator.cancel();  // Cancel the movement
        chickenImageView.setTranslationX(0f);  // Move the chicken back to the start
        chickenCurrentPosition = 0L;  // Reset the tracked animation position
        switchToSleepingAnimation();  // Reset to sleeping animation
    }

    private float getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }
}