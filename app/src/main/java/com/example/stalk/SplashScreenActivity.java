package com.example.stalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.window.SplashScreen;

import com.example.stalk.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {
ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                } catch (Exception e)
                {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashScreenActivity.this,RegisterActivity.class));
                    finish();
                }
            }
        };
        thread.start();
        setUpFadeAnimation(binding.text);
    }

    private void setUpFadeAnimation(TextView textView) {
        // Start from 0.1f if you desire 90% fade animation
        final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(500);
        fadeIn.setStartOffset(600);
        // End to 0.1f if you desire 90% fade animation
        final Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(500);
        fadeOut.setStartOffset(600);

        fadeIn.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeOut when fadeIn ends (continue)
                textView.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeIn when fadeOut ends (repeat)
                textView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        textView.startAnimation(fadeOut);
    }
}