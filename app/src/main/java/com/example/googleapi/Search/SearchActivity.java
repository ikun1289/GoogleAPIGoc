package com.example.googleapi.Search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.googleapi.Adapter.QuanAnAdapter;
import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.NguoiDung.ListMonAn;
import com.example.googleapi.NguoiDung.MainActivity;
import com.example.googleapi.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    String title = "";
    ImageButton btnBack;
    TextView txtTitle, txtKetQua;
    ListView lvquanan;
    List<QuanAn> quanAns;
    QuanAnAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        mapping();

        txtTitle.setText("Tìm kiếm: " + title);
        txtKetQua.setText("Tìm thấy " + quanAns.size() + " kết quả tìm kiếm");
        adapter = new QuanAnAdapter(this, R.layout.dong_quanan, quanAns);
        lvquanan.setAdapter(adapter);

        lvquanan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Mở activity xem món ăn của quán
                Intent intent = new Intent(SearchActivity.this, ListMonAn.class);
                Bundle b = new Bundle();
                QuanAn quanAn = quanAns.get(position);
                b.putString("IDQuanAn", quanAns.get(position).ID);
                b.putSerializable("QuanAn", quanAn);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> {
            this.onBackPressed();
        });

    }

    private void mapping() {
        btnBack = findViewById(R.id.btnBack);
        txtTitle = findViewById(R.id.txttitle);
        txtKetQua = findViewById(R.id.ketqua);
        lvquanan = findViewById(R.id.listQuanAn);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            quanAns = new ArrayList<>();
            quanAns = (List<QuanAn>) b.getSerializable("list");
            title = b.getString("title");
        } else {
            quanAns = new ArrayList<>();
        }

    }
}