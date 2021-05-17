package com.example.googleapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListMonAn extends AppCompatActivity {
    ListView lvMonAn;
    MonAnAdapter adapter;
    int IDQuanAn = -1;
    QuanAn quanAn;
    List<MonAn> monAnList;
    DatabaseHelper myDB;
    TextView txtTenQuan, txtMoTa, txtGioHoatDong, txtLatlng;

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
        LayDanhSachMonAn();
        int i;

        adapter = new MonAnAdapter(this, R.layout.dong_monan, monAnList);
        lvMonAn.setAdapter(adapter);

        //Đưa thông tin quán ăn lên view
        txtTenQuan.setText(quanAn.TenQuan);
        txtMoTa.setText("Mô tả : " + quanAn.MoTa);
        txtGioHoatDong.setText("Giờ mở cửa : " + quanAn.GioMoCua + "-" + quanAn.GioDongCua);
        txtLatlng.setText(quanAn.Lat + " - " + quanAn.Lng);

    }

    private void AnhXa() {
        lvMonAn = (ListView) findViewById(R.id.listViewMonAn);
        txtTenQuan = findViewById(R.id.txtTenQuan_listmonan);
        txtMoTa = findViewById(R.id.txtMoTa_listmonan);
        txtGioHoatDong = findViewById(R.id.txtGioHoatDong_listmonan);
        txtLatlng = findViewById(R.id.txtLatlng_listmonan);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            IDQuanAn = b.getInt("IDQuanAn");
            quanAn = (QuanAn) b.get("QuanAn");
        }
    }

    private void LayDanhSachMonAn() {
        Cursor res = myDB.getAllDataFromMonAn(IDQuanAn);
        if (res.getCount() == 0)
            Toast.makeText(ListMonAn.this, "NO DATA", Toast.LENGTH_SHORT).show();
        else
            while (res.moveToNext()) {
                monAnList.add(new MonAn(res.getInt(0)
                        , res.getString(1)
                        , res.getString(2)
                        , res.getString(3)
                        , res.getInt(4)
                ));
            }
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
}