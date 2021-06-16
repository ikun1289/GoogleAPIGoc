package com.example.googleapi.ChuQuan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.googleapi.Adapter.BinhLuanAdapter;
import com.example.googleapi.Models.BinhLuan;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BinhLuanActivity extends AppCompatActivity {

    List<BinhLuan> binhLuanList;
    RecyclerView recyclerView;
    BinhLuanAdapter adapter;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binh_luan);
        getSupportActionBar().hide();

        //mapping
        recyclerView = findViewById(R.id.rvBinhLuan);
        btnBack = findViewById(R.id.btnBack);
        //

        binhLuanList = new ArrayList<>();
        adapter = new BinhLuanAdapter(this,binhLuanList,this::onItemClick);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        layDanhSachBinhLuan();

        btnBack.setOnClickListener(v -> {
            this.onBackPressed();
        });


    }

    private void layDanhSachBinhLuan() {

        DocumentReference quanAnRef = FirebaseFirestore.getInstance()
                .collection("QuanAn")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        quanAnRef.collection("BinhLuan").orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    binhLuanList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        BinhLuan binhLuan = documentSnapshot.toObject(BinhLuan.class);
                        binhLuan.IDBinhLuan = documentSnapshot.getId();
                        binhLuanList.add(binhLuan);
                        Log.i("Firebase", "BinhLuan:"+binhLuan.IDNguoiGui);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void onItemClick(int position) {
//        Intent intent = new Intent(view.getContext(), DetailActivity.class);
//        intent.putExtra("detail-back", 1);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("item", listRecipes.get(position));
//        intent.putExtras(bundle);
//        startActivity(intent);

    }

}