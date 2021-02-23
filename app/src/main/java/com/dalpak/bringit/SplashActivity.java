package com.dalpak.bringit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.dalpak.bringit.databinding.ActivitySplashBinding;
import com.dalpak.bringit.network.Request;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.MyExceptionHandler;
import com.dalpak.bringit.utils.SharedPrefs;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DELAY = 2 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_out);

        binding.ivLogo.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(this::checkToken, SPLASH_DELAY);
    }

    private void checkToken() {

        if (SharedPrefs.getData(Constants.TOKEN_PREF).equals("")) gotoLogin();
        else checkTokenValidation();
    }

    private void checkTokenValidation() {
        Request.getInstance().checkToken(this, isDataSuccess -> {
            if (isDataSuccess) gotoMain();
            else gotoLogin();
        });
    }

    private void gotoMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
