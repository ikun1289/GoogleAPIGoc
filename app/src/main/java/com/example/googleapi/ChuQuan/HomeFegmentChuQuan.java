package com.example.googleapi.ChuQuan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.googleapi.Adapter.QuanAnAdapter;
import com.example.googleapi.BottomSheetListView;
import com.example.googleapi.DatabaseHelper;
import com.example.googleapi.Models.QuanAn;
import com.example.googleapi.NguoiDung.ListMonAn;
import com.example.googleapi.R;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeFegmentChuQuan extends Fragment implements OnMapReadyCallback {

    private final String TAG = "HomeFragmentChuQuan";
    private static final int RESULT_OK = -1;
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
    QuanAnAdapter adapter;
    LatLng latLng;

    private final float DEFAULT_ZOOM = 18;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_chuquan,container,false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ánh xạ
        AnhXa(view);
        //

        myDB = new DatabaseHelper(getContext());
        //Tạo những thứ liên quan tới google map API
        markerList = new ArrayList<>();
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Places.initialize(getActivity(),
                "AIzaSyCPWiPQolEIjmkoR5iw33tAxXCOWRkWqT0");
        placesClient = Places.createClient(getActivity());
//        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        mapview = supportMapFragment.getView();
        supportMapFragment.getMapAsync(this);

        //Navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    public void AnhXa(View view) {
        materialSearchBar = view.findViewById(R.id.searchBar_chuquan);
        btnFind = view.findViewById(R.id.btn_find_chuquan);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapAPI_chuquan);
        drawerLayout = view.findViewById(R.id.drawer_layout_chuquan);

    }

    public void ThemVaoDachSachQuanAn(Cursor res) {
        dialog = new BottomSheetDialog(getContext(), R.style.SheetDialog);
        dialog.setContentView(R.layout.restaurant_bottom_sheet_layout);
        listViewQuanAn = (BottomSheetListView) dialog.findViewById(R.id.listViewBtmSheet);
        quanAnArrayList = new ArrayList<>();



        adapter = new QuanAnAdapter(getContext(), R.layout.dong_quanan, quanAnArrayList);
        listViewQuanAn.setAdapter(adapter);
    }

    public void ListQuanAnItemClick() {
        listViewQuanAn.setOnItemClickListener((parent, view, position, id) -> {
            //Mở activity xem món ăn của quán
            Intent intent = new Intent(getContext(), ListMonAn.class);
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
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        if (mapview != null && mapview.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapview.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20
                    ));
                }
            });
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);

            @SuppressLint("ResourceType") View zoom = (View) mapview.findViewById(0x1);
            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) zoom.getLayoutParams();
            layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            layoutParams1.setMargins(0, 180, 40, 0);


        }

        //đặt marker quán ăn
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("QuanAn")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    QuanAn quanAn = documentSnapshot.toObject(QuanAn.class);
                    double lat = Double.parseDouble(quanAn.Lat);
                    double lng = Double.parseDouble(quanAn.Lng);
                    latLng = new LatLng(lat,lng);
                    Log.d(TAG, "onComplete: "+lat+"-"+lng);
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat,lng)).title("Place").icon(BitmapFromVector(getContext(),R.drawable.ic_google_maps)));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,
                            lng), 20
                    ));
                }
            }
        });



        //check gps
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
//        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
//        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
//        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
//            @Override
//            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                getDeviceLocation();
//            }
//        });
//        task.addOnFailureListener(getActivity(), new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                if (e instanceof ResolvableApiException) {
//                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
//                    try {
//                        resolvableApiException.startResolutionForResult(getActivity(), 51);
//                    } catch (IntentSender.SendIntentException sendIntentException) {
//                        sendIntentException.printStackTrace();
//                    }
//                }
//            }
//        });


    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 51) {
//            if (resultCode == RESULT_OK) {
//                getDeviceLocation();
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private void getDeviceLocation() {
//
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                if (task.isSuccessful()) {
//                    mLastKnowLocation = task.getResult();
//                    if (mLastKnowLocation != null) {
//                        CameraUpdateFactory.zoomIn();
//                        mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnowLocation.getLatitude(),
//                                mLastKnowLocation.getLongitude()), DEFAULT_ZOOM
//                        ));
//
//                    } else {
//                        final LocationRequest locationRequest = LocationRequest.create();
//                        locationRequest.setInterval(10000);
//                        locationRequest.setFastestInterval(5000);
//                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                        locationCallback = new LocationCallback() {
//                            @Override
//                            public void onLocationResult(LocationResult locationResult) {
//                                super.onLocationResult(locationResult);
//                                if (locationResult == null) {
//                                    return;
//                                }
//                                mLastKnowLocation = locationResult.getLastLocation();
//                                CameraUpdateFactory.zoomIn();
//                                mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnowLocation.getLatitude(),
//                                        mLastKnowLocation.getLongitude()), DEFAULT_ZOOM
//                                ));
//                                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
//                            }
//                        };
//                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
//
//                    }
//                } else {
//                    Toast.makeText(getContext(), "unable to get location", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
}
