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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

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

        login.setOnClickListener(v -> {
            Login(email.getText().toString(), pass.getText().toString());
        });

        return view;
    }

    private void Login(String email, String pass) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(checkValidate(email,pass))
        {
            mAuth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    mainActivity.updateUI(FirebaseAuth.getInstance().getCurrentUser());
                }
            });
        }
    }

    private boolean checkValidate(String emailS, String passS) {

        if(emailS.isEmpty())
        {
            email.setError("Không được để trống!");
            return false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailS).matches())
        {
            email.setError("Email không hợp lệ");
            return false;
        }
        else if(passS.isEmpty())
        {
            pass.setError("Không được để trống");
            return false;
        }
        else if(passS.contains(" "))
        {
            pass.setError("Password chứa ký tự không hợp lệ!");
            return false;
        }
        else if(passS.length()<6)
        {
            pass.setError("Password ít nhất phải 6 ký tự");
            return false;
        }

        return true;
    }


}
