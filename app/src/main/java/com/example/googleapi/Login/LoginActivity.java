package com.example.googleapi.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.googleapi.R;

public class LoginActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(LoginActivity.this,Demo.class);
                LoginActivity.this.startActivity(mainIntent);
                LoginActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}