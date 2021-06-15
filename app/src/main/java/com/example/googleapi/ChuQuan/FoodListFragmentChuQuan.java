package com.example.googleapi.ChuQuan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.googleapi.Adapter.MonAnAdapter2;
import com.example.googleapi.Models.MonAn;
import com.example.googleapi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FoodListFragmentChuQuan extends Fragment {

    String IDQuanAn = "FQrgS2LEbfrPbzczyEHP";
    DocumentReference quanAnRef;

    FloatingActionButton fabAddButton;
    RecyclerView foodView;
    MonAnAdapter2 adapter;
    List<MonAn> monAnList;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.food_list_chuquan,container,false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            IDQuanAn = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        //mapping
        fabAddButton = view.findViewById(R.id.fabAddButton);
        foodView = view.findViewById(R.id.food_list_chuquan_recyclerview);
        quanAnRef = FirebaseFirestore.getInstance().collection("QuanAn").document(IDQuanAn);
        //end mapping



        //set adapter
        monAnList = new ArrayList<>();
        adapter = new MonAnAdapter2(getContext(),monAnList,this::onItemClick);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);;
        foodView.setLayoutManager(gridLayoutManager);
        foodView.setAdapter(adapter);
        LayDanhSachMonAn();
        //



        fabAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),AddFoodActivity.class);
            Bundle b = new Bundle();
            b.putString("IDQuanAn",IDQuanAn);
            intent.putExtras(b);
            startActivity(intent);
        });


    }

    private void LayDanhSachMonAn() {

        monAnList.add(new MonAn("99","99","99","00","99"));
        monAnList.add(new MonAn("99","99","99","00","99"));
        monAnList.add(new MonAn("99","99","99","00","99"));
        monAnList.add(new MonAn("99","99","99","00","99"));
        monAnList.add(new MonAn("99","99","99","00","99"));
        monAnList.add(new MonAn("99","99","99","00","99"));
        monAnList.add(new MonAn("99","99","99","00","99"));
        monAnList.add(new MonAn("99","99","99","00","99"));
        monAnList.add(new MonAn("99","99","99","00","99"));

        quanAnRef.collection("MonAn").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                    {
                        MonAn monAn = documentSnapshot.toObject(MonAn.class);
                        monAn.IDMonAn = documentSnapshot.getId();
                        monAnList.add(monAn);
                        Log.i("Firebase","MonAn "+monAn.IDMonAn);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void onItemClick(int position) {
//        Intent intent = new Intent(view.getContext(), DetailActivity.class);
//        intent.putExtra("detail-back", 1);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("item", listRecipes.get(position));
//        intent.putExtras(bundle);
//        startActivity(intent);

        Toast.makeText(getContext(),position+"",Toast.LENGTH_SHORT).show();
    }
}
