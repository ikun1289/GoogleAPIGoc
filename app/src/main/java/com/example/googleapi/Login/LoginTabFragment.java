package com.example.googleapi.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.googleapi.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginTabFragment extends Fragment {
    EditText email,pass;
    Button login;
    private FirebaseAuth mAuth;
    HomeActivity mainActivity;

public LoginTabFragment(){};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_tab_fragment,container,false);
        email  = view.findViewById(R.id.email);
        pass = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login_button);
        mainActivity = (HomeActivity) getActivity();

        return view;
    }


}
