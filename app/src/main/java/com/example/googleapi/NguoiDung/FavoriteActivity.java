package com.example.googleapi.NguoiDung;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.googleapi.Adapter.QuanAnAdapter;
import com.example.googleapi.Models.MonAn;
import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity
{
    private final String TAG = "FavoriteActivity";

    ImageButton btnBack;
    ListView lvquanan;
    List<QuanAn> quanAns;
    QuanAnAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeu_thich);
        getSupportActionBar().hide();

        mapping();

        btnBack.setOnClickListener(v -> {
            this.onBackPressed();
        });

        quanAns = new ArrayList<>();
        adapter = new QuanAnAdapter(this, R.layout.dong_quanan, quanAns);
        lvquanan.setAdapter(adapter);
        layDanhSachQuanAnYeuThich();


        lvquanan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FavoriteActivity.this, ListMonAn.class);
                Bundle b = new Bundle();
                QuanAn quanAn = quanAns.get(position);
                b.putString("IDQuanAn", quanAns.get(position).ID);
                b.putSerializable("QuanAn", quanAn);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }

    private void layDanhSachQuanAnYeuThich() {

        List<String> idList = new ArrayList<>();
        CollectionReference collection = FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Fav");

        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String quanAnID = documentSnapshot.getId();
                        idList.add(quanAnID);
                        Log.d(TAG, "quan an yeu thich "+quanAnID);
                    }
                    if(idList.size()>0)
                    {
                        layDanhSachQuanAn(idList);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void layDanhSachQuanAn(List<String> idList) {
        CollectionReference collection = FirebaseFirestore.getInstance().collection("QuanAn");
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String ID = documentSnapshot.getId();
                        if(idList.contains(ID))
                        {
                            QuanAn quanAn = documentSnapshot.toObject(QuanAn.class);
                            quanAn.ID = ID;
                            quanAns.add(quanAn);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void mapping() {
        btnBack = findViewById(R.id.btnBack);
        lvquanan = findViewById(R.id.listQuanAn);
    }

}

