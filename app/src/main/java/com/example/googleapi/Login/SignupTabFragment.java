package com.example.googleapi.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.googleapi.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignupTabFragment  extends Fragment {
    EditText email,pass,cpass;
    Button signup;
    private FirebaseAuth mAuth;
    private static String TAG ="SIGNUP";
    HomeActivity mainActivity;
    public  SignupTabFragment(){};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_tab_fragment,container,false);
        email = view.findViewById(R.id.semail);
        pass = view.findViewById(R.id.spassword);
        cpass = view.findViewById(R.id.xd_password);
        signup = view.findViewById(R.id.s_button);
        mainActivity = (HomeActivity) getActivity();
        mAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nemail = email.getText().toString();
                String npass = pass.getText().toString();
                String ncpass = cpass.getText().toString();

                if(npass.equals(ncpass)) {
                    mainActivity.createAccount(nemail, npass);
                    Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "no", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
