package com.example.googleapi.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.googleapi.Models.QuanAn;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {

    private static final FirebaseHelper instance = new FirebaseHelper();

    private FirebaseHelper(){}

    public static FirebaseHelper getInstance(){
        return instance;
    }

    public Continuation<QuerySnapshot,QuanAn> AllShookUp(List<QuanAn> quanAns){
        return new AllShookUp(quanAns);
    }


    public class AllShookUp implements Continuation<QuerySnapshot, QuanAn> {
        private List<QuanAn> quanAns = new ArrayList<>();
        public AllShookUp(List<QuanAn> quanAns)
        {
            this.quanAns = quanAns;
        }
        @Override
        public QuanAn then(@NonNull @NotNull Task<QuerySnapshot> task) throws Exception {
            Log.d("Firebase","fuck size = "+quanAns.size());
            return new QuanAn();
        }
    }


}
