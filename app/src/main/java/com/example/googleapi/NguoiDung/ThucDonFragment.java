package com.example.googleapi.NguoiDung;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.googleapi.Adapter.MonAnAdapter2;
import com.example.googleapi.Adapter.ViewPagerAdapter;
import com.example.googleapi.Models.MonAn;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThucDonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThucDonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ThucDonFragment";

    RecyclerView lvMonAn;
    List<MonAn> monAnList;
    MonAnAdapter2 adapter;
    String IDQuanAn;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference quanAnRef;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ThucDonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThucDonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThucDonFragment newInstance(String param1, String param2) {
        ThucDonFragment fragment = new ThucDonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thuc_don, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);

        IDQuanAn = ListMonAn.IDQuanAn;
        Log.d(TAG, "onViewCreated: "+IDQuanAn);

        setUpLvMonAn();
    }

    private void setUpLvMonAn() {

        monAnList = new ArrayList<>();

        quanAnRef = firebaseFirestore.collection("QuanAn").document(IDQuanAn);

        adapter = new MonAnAdapter2(getContext(),monAnList,this::onItemClick);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);;
        lvMonAn.setLayoutManager(gridLayoutManager);

        lvMonAn.setAdapter(adapter);
        LayDanhSachMonAn();

    }

    private void LayDanhSachMonAn() {
        quanAnRef.collection("MonAn").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        MonAn monAn = documentSnapshot.toObject(MonAn.class);
                        monAn.IDMonAn = documentSnapshot.getId();
                        monAnList.add(monAn);
                        Log.i("Firebase", "MonAn " + monAn.IDMonAn);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void mapping(View view) {
        monAnList = new ArrayList<>();
        lvMonAn = view.findViewById(R.id.gridviewMonAn);
    }

    public void onItemClick(int position) {
//        Intent intent = new Intent(view.getContext(), DetailActivity.class);
//        intent.putExtra("detail-back", 1);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("item", listRecipes.get(position));
//        intent.putExtras(bundle);
//        startActivity(intent);

        Toast.makeText(getApplicationContext(), monAnList.get(position).MoTa, Toast.LENGTH_SHORT).show();
    }
}