package com.example.googleapi.NguoiDung;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.googleapi.Adapter.BinhLuanAdapter;
import com.example.googleapi.Models.BinhLuan;
import com.example.googleapi.Models.MonAn;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BinhLuanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BinhLuanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "BinhLuanFragment";

    EditText edtBinhLuan;
    Button btnHuy, btnDang;
    RecyclerView rvBinhLuan;
    BinhLuanAdapter adapter;
    List<BinhLuan> binhLuanList;
    String IDQuanAn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BinhLuanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BinhLuanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BinhLuanFragment newInstance(String param1, String param2) {
        BinhLuanFragment fragment = new BinhLuanFragment();
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
        return inflater.inflate(R.layout.fragment_binh_luan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IDQuanAn = ListMonAn.IDQuanAn;
        mapping(view);

        setUpRecycleView();

        edtBinhLuan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    btnDang.setEnabled(!s.toString().isEmpty());
                    btnHuy.setEnabled(!s.toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnHuy.setOnClickListener(v -> {
            edtBinhLuan.setText("");
        });

        btnDang.setOnClickListener(v -> {
            dangBinhLuan();
        });


    }

    private void dangBinhLuan() {
        DocumentReference quanAnRef = FirebaseFirestore.getInstance().collection("QuanAn").document(IDQuanAn);
        String noiDung = edtBinhLuan.getText().toString();
        String IDNguoiDang = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
        /*getting current date time using calendar class*/
        Calendar calobj = Calendar.getInstance();
        Date date = calobj.getTime();
        String time = df.format(calobj.getTime());

        Log.d(TAG, "dangBinhLuan: "+noiDung+" time:"+time);

        BinhLuan binhLuan = new BinhLuan(IDNguoiDang,email,noiDung,time,date);
        edtBinhLuan.setText("");

        quanAnRef.collection("BinhLuan").add(binhLuan).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Đăng thành công",Toast.LENGTH_SHORT).show();
                    layDanhSachBinhLuan();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Đăng thất bại",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpRecycleView() {

        binhLuanList = new ArrayList<>();
        adapter = new BinhLuanAdapter(getContext(),binhLuanList,this::onItemClick);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        //layoutManager.setReverseLayout(true);
        rvBinhLuan.setLayoutManager(layoutManager);
        rvBinhLuan.setAdapter(adapter);

        layDanhSachBinhLuan();

    }

    private void layDanhSachBinhLuan() {

        DocumentReference quanAnRef = FirebaseFirestore.getInstance().collection("QuanAn").document(IDQuanAn);

        quanAnRef.collection("BinhLuan").orderBy("date",Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    binhLuanList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        BinhLuan binhLuan = documentSnapshot.toObject(BinhLuan.class);
                        binhLuan.IDBinhLuan = documentSnapshot.getId();
                        binhLuanList.add(binhLuan);
                        Log.i("Firebase", "BinhLuan:"+binhLuan.IDNguoiGui);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void mapping(View view) {
        edtBinhLuan = view.findViewById(R.id.edtBinhLuan);
        btnHuy = view.findViewById(R.id.btnHuy);
        btnDang = view.findViewById(R.id.btnDang);
        rvBinhLuan = view.findViewById(R.id.rvBinhLuan);
    }

    public void onItemClick(int position) {
//        Intent intent = new Intent(view.getContext(), DetailActivity.class);
//        intent.putExtra("detail-back", 1);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("item", listRecipes.get(position));
//        intent.putExtras(bundle);
//        startActivity(intent);

    }
}