package com.example.layoutcustomer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.layoutcustomer.R;
import com.example.layoutcustomer.autenticacao.LoginActivity;
import com.example.layoutcustomer.helper.FirebaseHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // tempo sleep da tela de splash
        new Handler(Looper.getMainLooper()).postDelayed(this::verificarLogin, 3000);

    }

    private void verificarLogin() {
        if (FirebaseHelper.getAutenticao()){
            startActivity(new Intent(this, MainActivity.class));
        }else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


}