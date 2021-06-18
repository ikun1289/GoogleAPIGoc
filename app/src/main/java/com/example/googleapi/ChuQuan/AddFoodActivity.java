package com.example.googleapi.ChuQuan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.googleapi.Models.MonAn;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddFoodActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 999;
    String IDQuanAn;
    EditText editTextTenMon, editTextMoTa, editTextGia;
    ImageView image;
    ImageButton btnChooseImg, btnBack;
    Button btnAddFood;
    ProgressBar progressBar;
    DocumentReference quanAnRef;
    StorageReference storageReference;

    Uri imgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        getSupportActionBar().hide();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            IDQuanAn = b.getString("IDQuanAn");
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Thêm món ăn");

        //mapping
        editTextTenMon = findViewById(R.id.etxtTenMon_addfood);
        editTextMoTa = findViewById(R.id.etxtMoTa_addfood);
        editTextGia = findViewById(R.id.etxtGia_addfood);
        btnAddFood = findViewById(R.id.btnAddFood);
        image = findViewById(R.id.uploadFoodImg_addfood);
        progressBar = findViewById(R.id.progess_addfood);
        btnChooseImg = findViewById(R.id.btnChooseImage);
        btnBack = findViewById(R.id.btnBack);
        quanAnRef = FirebaseFirestore.getInstance().collection("QuanAn").document(IDQuanAn);
        storageReference = FirebaseStorage.getInstance().getReference("foodimg");
        //end mapping

        checkEmptyFunc();

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnAddFood.setOnClickListener(v -> {
            MonAn monAnToAdd = new MonAn();
            monAnToAdd.TenMonAn = editTextTenMon.getText().toString();
            monAnToAdd.MoTa = editTextMoTa.getText().toString();
            monAnToAdd.Gia = editTextGia.getText().toString();
            btnAddFood.setEnabled(false);

            StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imgUri));

            fileRef.putFile(imgUri).addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {
                Handler handler = new Handler();
                handler.postDelayed((Runnable) () -> {
                    progressBar.setProgress(0);
                },1000);
                fileRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    monAnToAdd.ImgMonAn = uri.toString();
                    UploadMonAn(monAnToAdd);
                });

            })
            .addOnFailureListener(e -> Toast.makeText(AddFoodActivity.this,"Thêm món ăn thất bại :((",Toast.LENGTH_SHORT).show())
            .addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            });
        });

        btnChooseImg.setOnClickListener(v -> {
            OpenFileChoser();
        });
    }

    private void UploadMonAn(MonAn monAnToAdd) {
        quanAnRef.collection("MonAn")
                .add(monAnToAdd)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Firebase", "Food added with ID: " + documentReference.getId());
                        Toast.makeText(AddFoodActivity.this,"Food added with ID: " + documentReference.getId(),Toast.LENGTH_SHORT).show();
                        editTextTenMon.setText("");
                        editTextMoTa.setText("");
                        editTextGia.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firebase", "Error adding food ", e);
                        Toast.makeText(AddFoodActivity.this,"Thêm món ăn thất bại :((",Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void checkEmptyFunc() {
        TextWatcher checkEmpty = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String TenMonAn = editTextTenMon.getText().toString().trim();
                String MoTa = editTextMoTa.getText().toString().trim();
                String Gia = editTextGia.getText().toString().trim();
                btnAddFood.setEnabled(!TenMonAn.isEmpty() && !MoTa.isEmpty() && !Gia.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editTextTenMon.addTextChangedListener(checkEmpty);
        editTextMoTa.addTextChangedListener(checkEmpty);
        editTextGia.addTextChangedListener(checkEmpty);

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
            image.setImageURI(imgUri);
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}