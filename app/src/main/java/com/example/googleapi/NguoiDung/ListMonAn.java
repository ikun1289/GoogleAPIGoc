package com.example.googleapi.NguoiDung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.googleapi.Adapter.MonAnAdapter;
import com.example.googleapi.Adapter.MonAnAdapter2;
import com.example.googleapi.Adapter.ViewPagerAdapter;
import com.example.googleapi.ChuQuan.FoodListFragmentChuQuan;
import com.example.googleapi.ChuQuan.HomeFegmentChuQuan;
import com.example.googleapi.ChuQuan.RestauDetailFragmentChuQuan;
import com.example.googleapi.DatabaseHelper;
import com.example.googleapi.Models.MonAn;
import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListMonAn extends AppCompatActivity {

    ViewPagerAdapter adapter;
    public static String IDQuanAn = "";
    QuanAn quanAn;

    DatabaseHelper myDB;
    TextView txtTenQuan, txtMoTa, txtGioHoatDong, txtLatlng;
    ImageButton btnback, btnFav;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView imageView;
    boolean fav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mon_an);
        getSupportActionBar().hide();

        AnhXa();

        checkFav();


        btnback.setOnClickListener(v -> {
            this.onBackPressed();
        });
        btnFav.setOnClickListener(v -> {
            setFavQuanAn();
        });

        //
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null)
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        setTitle("Chi Tiết Quán Ăn");


//        monAnList = new ArrayList<>();
//        myDB = new DatabaseHelper(this);
//        quanAnRef = firebaseFirestore.collection("QuanAn").document(IDQuanAn);

        //adapter = new MonAnAdapter(this, R.layout.dong_monan, monAnList);
//        adapter2 = new MonAnAdapter2(this,monAnList,this::onItemClick);
//        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);;
//        lvMonAn.setLayoutManager(gridLayoutManager);
//        lvMonAn.setAdapter(adapter2);
//        LayDanhSachMonAn();


        //set up viewpager
        setUpViewPager();


        //Đưa thông tin quán ăn lên view
        txtTenQuan.setText(quanAn.TenQuan);
        txtMoTa.setText("Mô tả : " + quanAn.MoTa);
        txtGioHoatDong.setText("Giờ mở cửa : " + quanAn.GioMoCua + "-" + quanAn.GioDongCua);
        txtLatlng.setText(quanAn.Lat + " - " + quanAn.Lng);
        Glide.with(this).load(quanAn.imgQuanAn).placeholder(R.drawable.foodimg).into(imageView);


    }

    private void setUpViewPager(){
        List<Fragment> fragments;
        fragments = new ArrayList<>();
        fragments.add(new ThucDonFragment());
        fragments.add(new BinhLuanFragment());

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Thực đơn");
        tabLayout.getTabAt(1).setText("Bình luận");
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void checkFav() {
        DocumentReference document = FirebaseFirestore.getInstance().collection("Users")
                                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .collection("Fav")
                                                            .document(IDQuanAn);
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists())
                    {
                        btnFav.setImageResource(R.drawable.ic_baseline_favorite_24);
                        fav = true;
                    }
                    else fav = false;
                }
            }
        });
    }

    private void setFavQuanAn() {

        if(!fav)
        {
            updateFav(true);
        }
        else {
            updateFav(false);
        }


    }

    private void updateFav(boolean fav1) {

        if(fav1)
        {
            DocumentReference document = FirebaseFirestore.getInstance().collection("Users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("Fav")
                    .document(IDQuanAn);
            Map<String,String> h = new HashMap<>();
            h.put("IDQuanAn",IDQuanAn);

            document.set(h).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        btnFav.setImageResource(R.drawable.ic_baseline_favorite_24);
                        fav = true;
                        Toast.makeText(getApplicationContext(),"Đã thêm vào danh sách yêu thích <3",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            DocumentReference document = FirebaseFirestore.getInstance().collection("Users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("Fav")
                    .document(IDQuanAn);
            document.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        btnFav.setImageResource(R.drawable.ic_baseline_un_fav_24);
                        fav = false;
                        Toast.makeText(getApplicationContext(),"Đã bỏ ra khỏi danh sách yêu thích",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void AnhXa() {
        //lvMonAn = (RecyclerView) findViewById(R.id.gridviewMonAn);
        txtTenQuan = findViewById(R.id.txtTenQuan);
        txtMoTa = findViewById(R.id.txtMoTa);
        txtGioHoatDong = findViewById(R.id.txtGioHoatDong);
        txtLatlng = findViewById(R.id.txtViTri);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            IDQuanAn = b.getString("IDQuanAn");
            quanAn = (QuanAn) b.get("QuanAn");
        }
        btnback = findViewById(R.id.btnBack);
        btnFav = findViewById(R.id.btnFav);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.listmonan_viewpager);
        imageView = findViewById(R.id.quananImg);

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


//    public void onItemClick(int position) {
////        Intent intent = new Intent(view.getContext(), DetailActivity.class);
////        intent.putExtra("detail-back", 1);
////        Bundle bundle = new Bundle();
////        bundle.putSerializable("item", listRecipes.get(position));
////        intent.putExtras(bundle);
////        startActivity(intent);
//
//        Toast.makeText(getApplicationContext(),monAnList.get(position).MoTa,Toast.LENGTH_SHORT).show();
//    }

}