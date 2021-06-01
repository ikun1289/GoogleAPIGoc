package com.example.googleapi.Login;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.googleapi.R;
import com.google.firebase.auth.FirebaseAuth;

public class tt extends AppCompatActivity {
TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt);
        getSupportActionBar().hide();
        textView = (TextView) findViewById(R.id.lg);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                updateUI();
            }
        });



    }
    public void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();

        // [END auth_sign_out]
    }
    private void updateUI() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
}