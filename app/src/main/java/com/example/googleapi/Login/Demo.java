package com.example.googleapi.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.googleapi.R;

public class Demo extends AppCompatActivity {
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        start = (Button) findViewById(R.id.start);
        getSupportActionBar().hide();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Demo.this, HomeActivity.class);
                Demo.this.startActivity(mainIntent);
                Demo.this.finish();
            }
        });
    }
}