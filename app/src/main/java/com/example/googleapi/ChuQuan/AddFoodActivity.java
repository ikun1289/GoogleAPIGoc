package com.example.googleapi.ChuQuan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.googleapi.Models.MonAn;
import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class AddFoodActivity extends AppCompatActivity {

    private String IDQuanAn;
    EditText editTextTenMon, editTextMoTa, editTextGia;
    ImageButton btnUploadImg;
    Button btnAddFood;
    DocumentReference quanAnRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

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
        btnUploadImg = findViewById(R.id.uploadFoodImg_addfood);
        quanAnRef = FirebaseFirestore.getInstance().collection("QuanAn").document(IDQuanAn);
        //end mapping

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

        btnAddFood.setOnClickListener(v -> {
            MonAn monAnToAdd = new MonAn();
            monAnToAdd.TenMonAn = editTextTenMon.getText().toString();
            monAnToAdd.MoTa = editTextMoTa.getText().toString();
            monAnToAdd.Gia = editTextGia.getText().toString();

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
                        }
                    });

        });


    }


}