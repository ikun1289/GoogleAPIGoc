package com.example.googleapi.ChuQuan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChinhSuaQuanAn extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 999;
    Uri imgUri = null;
    ImageView imageView;
    ImageButton btnChooseImage, btnBack;
    EditText edtTen, edtMoTa;
    TextView txtGioMoCua, txtGioDongCua;
    QuanAn quanAn;
    Button btnCapNhat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_quan_an);
        getSupportActionBar().hide();

        mapping();

        edtTen.setText(quanAn.TenQuan);
        edtMoTa.setText(quanAn.MoTa);
        txtGioDongCua.setText(quanAn.GioDongCua);
        txtGioMoCua.setText(quanAn.GioMoCua);
        Glide.with(this).load(quanAn.imgQuanAn).placeholder(R.drawable.foodimg).into(imageView);

        btnBack.setOnClickListener(v -> {
            finish();
        });
        txtGioMoCua.setOnClickListener(v -> {
            pickTime(txtGioMoCua);
        });

        txtGioDongCua.setOnClickListener(v -> {
            pickTime(txtGioDongCua);
        });

        btnChooseImage.setOnClickListener(v -> {
            OpenFileChoser();
        });

        btnCapNhat.setOnClickListener(v -> {
            capNhatThongTin();
            btnCapNhat.setEnabled(false);
        });

    }

    private void capNhatThongTin() {
        String name = edtTen.getText().toString();
        String mota = edtMoTa.getText().toString();
        String giomocua = txtGioMoCua.getText().toString();
        String giodongcua = txtGioDongCua.getText().toString();

        QuanAn quanAnTemp = new QuanAn(quanAn);

        if(checkValidate(name, mota))
        {
            quanAnTemp.TenQuan = name;
            quanAnTemp.MoTa = mota;
            quanAnTemp.GioMoCua = giomocua;
            quanAnTemp.GioDongCua = giodongcua;
            if(imgUri==null)
            {
                capNhatThongTinTrenFirebase(quanAnTemp);
            }
            else {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("restaurantimg");
                StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                        +"."+getFileExtension(imgUri));

                fileRef.putFile(imgUri).addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {
                    fileRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                quanAnTemp.imgQuanAn = uri.toString();
                                capNhatThongTinTrenFirebase(quanAnTemp);
                            });

                });
            }
        }
    }

    private void capNhatThongTinTrenFirebase(QuanAn quanAnTemp) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("QuanAn")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.set(quanAnTemp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ChinhSuaQuanAn.this,"Chỉnh sửa thông tin thành công",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(ChinhSuaQuanAn.this,"Đổi vị trí thất bại",Toast.LENGTH_SHORT).show();
                    btnCapNhat.setEnabled(true);
                }
            }
        });
    }

    private boolean checkValidate(String name, String mota) {
        if(name.isEmpty()) {
            edtTen.setError("Tên không được để trống");
            return false;
        }
        else if(mota.isEmpty())
        {
            edtTen.setError("Mô tả không được để trống");
            return false;
        }
        return true;
    }

    private void mapping() {
        imageView = findViewById(R.id.quananImg);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnBack = findViewById(R.id.btnBack);
        edtTen = findViewById(R.id.txtTenQuan);
        edtMoTa = findViewById(R.id.txtMoTa);
        txtGioMoCua = findViewById(R.id.txtGioMoCua);
        txtGioDongCua = findViewById(R.id.txtGioHDongCua);
        btnCapNhat = findViewById(R.id.btnCapNhat);

        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            quanAn = (QuanAn) b.getSerializable("QuanAn");
        }

    }

    private void pickTime(TextView txt) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,hourOfDay,minute);
                txt.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },0,0,true);
        timePickerDialog.show();

    }

    private void OpenFileChoser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data!=null && data.getData()!=null)
        {
            imgUri = data.getData();
            imageView.setImageURI(imgUri);
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}