package com.example.googleapi.ChuQuan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.googleapi.Login.HomeActivity;
import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class RestauDetailFragmentChuQuan extends Fragment {

    ImageView imgQuanAn;
    TextView txtTen, txtMoTa, txtGioHoatDong, txtDiaChi;
    ImageButton btnCmt, btnChinhsua, btnDangxuat;
    QuanAn quanAn;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_detail_chuquan,container,false);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapping(view);

        layThongTinQuanAn();

        btnDangxuat.setOnClickListener(v -> {
            signOut();
        });

        btnChinhsua.setOnClickListener(v -> {
            moChinhSuaActivitity();
        });

        btnCmt.setOnClickListener(v -> {
            moBinhLuanActivity();
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        layThongTinQuanAn();
    }

    private void moChinhSuaActivitity() {
        Intent intent = new Intent(getContext(),ChinhSuaQuanAn.class);
        Bundle b = new Bundle();
        b.putSerializable("QuanAn",quanAn);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void moBinhLuanActivity() {
        Intent intent = new Intent(getContext(),BinhLuanActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void layThongTinQuanAn() {
        DocumentReference document = FirebaseFirestore.getInstance().collection("QuanAn").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    quanAn = documentSnapshot.toObject(QuanAn.class);

                    txtTen.setText(quanAn.TenQuan);
                    txtDiaChi.setText(quanAn.Lat+" "+quanAn.Lng);
                    txtGioHoatDong.setText(quanAn.GioMoCua+"-"+quanAn.GioDongCua);
                    txtMoTa.setText(quanAn.MoTa);
                    Glide.with(getContext()).load(quanAn.imgQuanAn).placeholder(R.drawable.foodimg).into(imgQuanAn);

                }
                else Toast.makeText(getContext(),"Lấy thông tin quán ăn thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mapping(View view) {
        imgQuanAn = view.findViewById(R.id.quananImg);
        txtTen = view.findViewById(R.id.txtTenQuan);
        txtMoTa = view.findViewById(R.id.txtMoTa);
        txtGioHoatDong = view.findViewById(R.id.txtGioHoatDong);
        txtDiaChi = view.findViewById(R.id.txtViTri);
        btnChinhsua = view.findViewById(R.id.btnChinhsua);
        btnCmt = view.findViewById(R.id.btnCmt);
        btnDangxuat = view.findViewById(R.id.btnDX);
    }
}
