package com.example.googleapi.Login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.NguoiDung.MainActivity;
import com.example.googleapi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.type.DateTime;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;

public class CreateQuanAnActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1234;
    Button btnChooseImg, btnChoosePlace, btnCreate, btnBack;
    EditText edtName, edtMoTa;
    public EditText edtDiaDiem;
    TextView start, end;
    ImageView img;
    ProgressDialog progressDialog;
    String[] latlng;
    Uri imgUri = null;


    @Override
    protected void onResume() {
        super.onResume();
        edtDiaDiem = findViewById(R.id.edtDiaDiem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tao_quan_an);

        mapping();

        btnChoosePlace.setOnClickListener(v -> {
            showDialogPickPlace();
        });

        btnCreate.setOnClickListener(v -> {
            createQuanAn();
        });

        start.setOnClickListener(v -> {
            pickTime(start);
        });

        end.setOnClickListener(v -> {
            pickTime(end);
        });

        btnChooseImg.setOnClickListener(v -> {
            OpenFileChoser();
        });

        btnBack.setOnClickListener(v -> {
            this.onBackPressed();
        });

    }

    private void createQuanAn() {
        String name = edtName.getText().toString();
        String mota = edtMoTa.getText().toString();
        String diadiem = edtDiaDiem.getText().toString();
        latlng = splitString(diadiem);
        String startTime = start.getText().toString();
        String endTime = end.getText().toString();

        if(checkValidate(name, mota, diadiem, startTime, endTime))
        {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            QuanAn quanAn = new QuanAn(mAuth.getCurrentUser().getUid(),name,startTime,endTime,mota,latlng[0],latlng[1],"");

            progressDialog = new ProgressDialog(CreateQuanAnActivity.this);
            progressDialog.setContentView(R.layout.activity_loading);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            progressDialog.show();

            if(imgUri!=null)
            {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("restaurantimg");
                StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                        +"."+getFileExtension(imgUri));
                fileRef.putFile(imgUri).addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                DocumentReference documentReference = FirebaseFirestore.getInstance()
                                        .collection("QuanAn")
                                        .document(mAuth.getCurrentUser().getUid());
                                quanAn.imgQuanAn = uri.toString();
                                documentReference.set(quanAn);
                                progressDialog.dismiss();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this,"Tạo quán ăn thất bại!",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            });
                });
            }
            else
            {
                DocumentReference documentReference = FirebaseFirestore.getInstance()
                        .collection("QuanAn")
                        .document(mAuth.getCurrentUser().getUid());
                documentReference.set(quanAn);
                progressDialog.dismiss();
                finish();
            }


        }

    }

    private boolean checkValidate(String name, String mota, String diadiem, String startTime, String endTime) {

        if(name.isEmpty()) {
            edtName.setError("Không được để trống");
            return false;
        }
        else if(mota.isEmpty()) {
            edtMoTa.setError("Không được để trống");
            return false;
        }
        else if(diadiem.isEmpty()) {
            edtDiaDiem.setError("Quán ăn phải có địa điểm");
            return false;
        }
        return true;
    }

    private String[] splitString(String text)
    {
        String[] splitStr = text.split("\\s+");
        return splitStr;
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

    private void mapping() {
        btnChooseImg = findViewById(R.id.btnChooseImage);
        img = findViewById(R.id.quananImg);
        btnChoosePlace = findViewById(R.id.btnChoosePlace);
        edtName = findViewById(R.id.edtName);
        edtMoTa = findViewById(R.id.edtMoTa);
        edtDiaDiem = findViewById(R.id.edtDiaDiem);
        start = findViewById(R.id.startTime);
        end = findViewById(R.id.endTime);
        btnCreate = findViewById(R.id.btnThem);
        btnBack = findViewById(R.id.btnBack);


    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void OpenFileChoser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void showDialogPickPlace()
    {

        Intent intent = new Intent(this,PickPlaceDialog.class);

        if(!edtDiaDiem.getText().toString().isEmpty())
        {
            latlng = splitString(edtDiaDiem.getText().toString());
            double lat = Double.parseDouble(latlng[0]);
            double lng = Double.parseDouble(latlng[1]);
            Bundle b = new Bundle();
            b.putDouble("lat",lat);
            b.putDouble("lng",lng);
            intent.putExtras(b);
        }

        startActivityForResult(intent,999);

//        Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_pick_place);
//        //
//        GoogleMap mapAPI;
//        Button btnPickPlace = dialog.findViewById(R.id.btnPickPlace);
//
//        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapAPI);
//        View mMapView;
//
//        mMapView = supportMapFragment.getView();
//        supportMapFragment.getMapAsync(this);
//        //
//
//        btnPickPlace.setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();

//        Intent intent = new Intent(this,MainActivity.class);
//        startActivity(intent);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data!=null && data.getData()!=null)
        {
            imgUri = data.getData();
            img.setImageURI(imgUri);
        }
        else if(requestCode == 999 && resultCode == RESULT_OK)
        {
            edtDiaDiem.setText(data.getStringExtra("result"));
        }
    }

//    @SuppressLint("MissingPermission")
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        LatLng posisiabsen = new LatLng(10.848413, 106.773146); ////your lat lng
//        googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("Place"));
//        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.848413,
//                106.773146), 20
//        ));
//    }
}
