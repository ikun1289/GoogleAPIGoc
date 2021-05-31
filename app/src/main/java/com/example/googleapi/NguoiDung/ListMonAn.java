package com.example.googleapi.NguoiDung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googleapi.Adapter.MonAnAdapter;
import com.example.googleapi.Adapter.MonAnAdapter2;
import com.example.googleapi.DatabaseHelper;
import com.example.googleapi.Models.MonAn;
import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListMonAn extends AppCompatActivity {
    RecyclerView lvMonAn;
    MonAnAdapter adapter;
    MonAnAdapter2 adapter2;
    String IDQuanAn = "";
    QuanAn quanAn;
    List<MonAn> monAnList;
    DatabaseHelper myDB;
    TextView txtTenQuan, txtMoTa, txtGioHoatDong, txtLatlng;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference quanAnRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mon_an);

        AnhXa();

        //
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Chi Tiết Quán Ăn");
        monAnList = new ArrayList<>();
        myDB = new DatabaseHelper(this);
        quanAnRef = firebaseFirestore.collection("QuanAn").document(IDQuanAn);

        //adapter = new MonAnAdapter(this, R.layout.dong_monan, monAnList);
        adapter2 = new MonAnAdapter2(this,monAnList,this::onItemClick);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);;
        lvMonAn.setLayoutManager(gridLayoutManager);
        lvMonAn.setAdapter(adapter2);
        LayDanhSachMonAn();

        //Đưa thông tin quán ăn lên view
        txtTenQuan.setText(quanAn.TenQuan);
        txtMoTa.setText("Mô tả : " + quanAn.MoTa);
        txtGioHoatDong.setText("Giờ mở cửa : " + quanAn.GioMoCua + "-" + quanAn.GioDongCua);
        txtLatlng.setText(quanAn.Lat + " - " + quanAn.Lng);

    }

    private void AnhXa() {
        lvMonAn = (RecyclerView) findViewById(R.id.gridviewMonAn);
        txtTenQuan = findViewById(R.id.txtTenQuan_listmonan);
        txtMoTa = findViewById(R.id.txtMoTa_listmonan);
        txtGioHoatDong = findViewById(R.id.txtGioHoatDong_listmonan);
        txtLatlng = findViewById(R.id.txtLatlng_listmonan);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            IDQuanAn = b.getString("IDQuanAn");
            quanAn = (QuanAn) b.get("QuanAn");
        }
    }

    private void LayDanhSachMonAn() {
        quanAnRef.collection("MonAn").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                    {
                        MonAn monAn = documentSnapshot.toObject(MonAn.class);
                        monAn.IDMonAn = documentSnapshot.getId();
                        monAnList.add(monAn);
                        Log.i("Firebase","MonAn "+monAn.IDMonAn);
                    }
                    adapter2.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onItemClick(int position) {
//        Intent intent = new Intent(view.getContext(), DetailActivity.class);
//        intent.putExtra("detail-back", 1);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("item", listRecipes.get(position));
//        intent.putExtras(bundle);
//        startActivity(intent);

        Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_SHORT).show();
    }

}