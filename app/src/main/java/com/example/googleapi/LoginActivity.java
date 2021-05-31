package com.example.googleapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.googleapi.ChuQuan.MainActivityChuQuan;
import com.example.googleapi.ChuQuan.MainChuQuan;
import com.example.googleapi.Firebase.FirebaseHelper;
import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.NguoiDung.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    Button btnTestND,btntestCQ, btnTestFB;
    ListView lvTest;
    ArrayAdapter<QuanAn> adapter;
    List<QuanAn> quanAnList;
    static FirebaseHelper myFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);

        btnTestND = findViewById(R.id.btnTestNguoiDung);
        btntestCQ = findViewById(R.id.btnTestChuQuan);
        btnTestFB = findViewById(R.id.btnTestFirebase);
        lvTest = findViewById(R.id.lvTest);

        quanAnList = new ArrayList<>();
        adapter = new ArrayAdapter<QuanAn>(this,R.layout.support_simple_spinner_dropdown_item,quanAnList);
        lvTest.setAdapter(adapter);
        myFirebase = FirebaseHelper.getInstance();



        btnTestND.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        btntestCQ.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainChuQuan.class);
            startActivity(intent);
        });
        btnTestFB.setOnClickListener(v -> {
            List<QuanAn> quanAns = new ArrayList<>();
            Task<QuerySnapshot> task = FirebaseFirestore.getInstance().collection("QuanAn").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){

                        for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                        {
                            QuanAn quanAn = documentSnapshot.toObject(QuanAn.class);
                            quanAnList.add(quanAn);
                            Log.i("Firebase","OK "+quanAn.ID);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });;
            task.continueWith(myFirebase.AllShookUp(quanAnList));

        });
    }

}