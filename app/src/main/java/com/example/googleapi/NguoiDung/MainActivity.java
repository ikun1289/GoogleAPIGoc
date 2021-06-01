package com.example.googleapi.NguoiDung;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.CursorWindow;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.googleapi.Adapter.QuanAnAdapter;
import com.example.googleapi.BottomSheetListView;
import com.example.googleapi.DatabaseHelper;
import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.R;
import com.example.googleapi.StringSimilarity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    DatabaseHelper myDB;

    DrawerLayout drawerLayout;
    GoogleMap mapAPI;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    PlacesClient placesClient;
    List<AutocompletePrediction> predictions;
    Location mLastKnowLocation;
    LocationCallback locationCallback;
    List<Marker> markerList;

    MaterialSearchBar materialSearchBar;
    View mapview;
    Button btnFind;

    BottomSheetListView listViewQuanAn;
    BottomSheetDialog dialog;
    ArrayList<QuanAn> quanAnArrayList;
    ArrayList<QuanAn> quanAnSearchList;
    QuanAnAdapter adapter;
    Cursor res;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private final float DEFAULT_ZOOM = 18;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ánh xạ
        AnhXa();
        //

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            if (e!=null) {
                e.printStackTrace();
            }
        }

        quanAnSearchList = new ArrayList<>();
        myDB = new DatabaseHelper(this);
        //Tạo những thứ liên quan tới google map API
        markerList = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        Places.initialize(MainActivity.this,
                "AIzaSyCPWiPQolEIjmkoR5iw33tAxXCOWRkWqT0");
        placesClient = Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        mapview = supportMapFragment.getView();
        supportMapFragment.getMapAsync(this);

        //Navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Hiện danh sách quán ăn bằng dialog bottomsheet
        ThemVaoDachSachQuanAn();

        //listener của btn tìm quán ăn gần nhất
        btnFind.setOnClickListener(v -> {
            //thêm cái tìm quán ăn gần nhất tại đây
//            LatLng CHoRachGoi = new LatLng(9.896942, 105.664609);
//            mapAPI.addMarker(new MarkerOptions().position(CHoRachGoi).title("Chợ Rạch Gòi"));
//            mapAPI.moveCamera(CameraUpdateFactory.newLatLng(CHoRachGoi));
//            markerList.clear();
//            mapAPI.clear();
//            if (quanAnArrayList.size() == 0) {
//                Toast.makeText(HomeActivity.this, "NO DATA", Toast.LENGTH_SHORT).show();
//            } else {
//                for (int i = 0; i < quanAnArrayList.size(); i++) {
//                    QuanAn quanAn = quanAnArrayList.get(i);
//                    LatLng pos = new LatLng(Double.parseDouble(quanAn.Lat), Double.parseDouble(quanAn.Lng));
//                    markerList.add(mapAPI.addMarker(new MarkerOptions().position(pos).title(quanAn.TenQuan)));
//                }
//            }
//            mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnowLocation.getLatitude(),
//                    mLastKnowLocation.getLongitude()), DEFAULT_ZOOM));


            //cập nhật adapter bằng danh sách quán ăn mới lấy ra ở bước trên
            //

            adapter.notifyDataSetChanged();
            dialog.show();
        });

        //Xem danh sach món ăn trong quán khi ấn vào quán
        ListQuanAnItemClick();

        //Search listener
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //startSearch(text.toString(), true, null, true);
                Search(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START))
                        drawerLayout.closeDrawer(GravityCompat.START);
                    else drawerLayout.openDrawer(GravityCompat.START);

                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.disableSearch();
                }
            }
        });


        //textChangeListener tạo auto prediction place
//        materialSearchBar.addTextChangeListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                //place on google map
//                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
//                        .setCountry("VN")
//                        .setTypeFilter(TypeFilter.ADDRESS)
//                        .setSessionToken(token)
//                        .setQuery(s.toString())
//                        //.setOrigin( new LatLng( mLastKnowLocation.getLatitude(),mLastKnowLocation.getLongitude()))
//                        //.setLocationBias(getBounds(mLastKnowLocation.getLatitude(),mLastKnowLocation.getLongitude(),1000,1000))
//                        .build();
//
//                //Task<FindAutocompletePredictionsResponse> task = placesClient.findAutocompletePredictions(predictionsRequest);
//
//                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
//                    @Override
//                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
//                        if(task.isSuccessful())
//                        {
//                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
//                            if(predictionsResponse != null)
//                            {
//                                predictions = predictionsResponse.getAutocompletePredictions();
//                                List<String> suggestionList = new ArrayList<>();
//                                for(int i =0; i< predictions.size();i++)
//                                {
//                                    AutocompletePrediction prediction = predictions.get(i);
//                                    suggestionList.add(prediction.getFullText(null).toString());
//                                }
//                                materialSearchBar.updateLastSuggestions(suggestionList);
//                                if(!materialSearchBar.isSuggestionsVisible())
//                                {
//                                    materialSearchBar.showSuggestionsList();
//                                }
//                            }
//                        } else {
//                          Log.i("mytag","predition fetching task unsuccessful" + s);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    private void Search(String toString) {
        Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
        quanAnSearchList = new ArrayList<>();
        for (QuanAn i:quanAnArrayList
             ) {
            double x = StringSimilarity.similarity(toString,i.TenQuan);
            if(x > 0.6)
            {
                Log.d("Search","Giống " + x);
                quanAnSearchList.add(i);
            }
            else Log.d("Search","Không giống " + x);
        }
    }


    public void AnhXa() {
        materialSearchBar = findViewById(R.id.searchBar);
        btnFind = findViewById(R.id.btn_find);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapAPI);
        drawerLayout = findViewById(R.id.drawer_layout);

    }

    public void ThemVaoDachSachQuanAn() {
        dialog = new BottomSheetDialog(this, R.style.SheetDialog);
        dialog.setContentView(R.layout.restaurant_bottom_sheet_layout);
        listViewQuanAn = (BottomSheetListView) dialog.findViewById(R.id.listViewBtmSheet);
        quanAnArrayList = new ArrayList<>();

        firebaseFirestore.collection("QuanAn").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                    {
                        QuanAn quanAn = documentSnapshot.toObject(QuanAn.class);
                        quanAn.ID = documentSnapshot.getId();
                        quanAnArrayList.add(quanAn);
                        Log.i("Firebase","OK "+quanAn.ID);
                    }
                    adapter.notifyDataSetChanged();
                }
                else Log.i("Firebase","Không OK");
            }
        });


        adapter = new QuanAnAdapter(this, R.layout.dong_quanan, quanAnArrayList);
        listViewQuanAn.setAdapter(adapter);
    }

    public void ListQuanAnItemClick() {
        listViewQuanAn.setOnItemClickListener((parent, view, position, id) -> {
            //Mở activity xem món ăn của quán
            Intent intent = new Intent(MainActivity.this, ListMonAn.class);
            Bundle b = new Bundle();
            QuanAn quanAn = quanAnArrayList.get(position);
            b.putString("IDQuanAn", quanAnArrayList.get(position).ID);
            b.putSerializable("QuanAn", quanAn);
            intent.putExtras(b);
            startActivity(intent);
        });
    }

    //tính phạm vi hình vuông
    public static RectangularBounds getBounds(double lat0, double lng0, long dy, long dx) {
        RectangularBounds bounds;
        double lat = (180 / Math.PI) * (dy / 6378137);
        double lng = (180 / Math.PI) * (dx / 6378137) / Math.cos(lat0);
        bounds = RectangularBounds.newInstance(new LatLng(lat0 - lat, lng0 - lng)
                , new LatLng(lat0 + lat, lng0 + lng));
        return bounds;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapAPI = googleMap;

        mapAPI.setMyLocationEnabled(true);
        mapAPI.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapview != null && mapview.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapview.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);

        }

        //check gps
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(MainActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });
        task.addOnFailureListener(MainActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult(MainActivity.this, 51);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    mLastKnowLocation = task.getResult();
                    if (mLastKnowLocation != null) {
                        CameraUpdateFactory.zoomIn();
                        mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnowLocation.getLatitude(),
                                mLastKnowLocation.getLongitude()), DEFAULT_ZOOM
                        ));

                    } else {
                        final LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setInterval(10000);
                        locationRequest.setFastestInterval(5000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                if (locationResult == null) {
                                    return;
                                }
                                mLastKnowLocation = locationResult.getLastLocation();
                                CameraUpdateFactory.zoomIn();
                                mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnowLocation.getLatitude(),
                                        mLastKnowLocation.getLongitude()), DEFAULT_ZOOM
                                ));
                                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                    }
                } else {
                    Toast.makeText(MainActivity.this, "unable to get location", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

