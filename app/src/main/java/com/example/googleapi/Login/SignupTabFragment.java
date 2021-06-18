package com.example.googleapi.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.googleapi.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignupTabFragment  extends Fragment {

    RadioButton btnNguoiDung, btnChuQuan;
    EditText email,pass,cpass;
    RadioGroup Vaitro;
    Button signup;
    private FirebaseAuth mAuth;
    private static String TAG ="SIGNUP";
    HomeActivity mainActivity;
    String role = "nguoidung";
    public  SignupTabFragment(){};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_tab_fragment,container,false);
        email = view.findViewById(R.id.semail);
        pass = view.findViewById(R.id.spassword);
        cpass = view.findViewById(R.id.xd_password);
        signup = view.findViewById(R.id.s_button);
        Vaitro = view.findViewById(R.id.vaitro);
        mainActivity = (HomeActivity) getActivity();
        btnChuQuan = view.findViewById(R.id.radioButtonChuQuan);
        btnNguoiDung = view.findViewById(R.id.radioButtonNguoiDung);
        mAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nemail = email.getText().toString();
                String npass = pass.getText().toString();
                String ncpass = cpass.getText().toString();


                if(checkValidate(nemail,npass,ncpass))
                {
                    mainActivity.createAccount(nemail, npass, role);
                }
            }
        });


        btnNguoiDung.setOnClickListener(v -> {
            role = "nguoidung";
        });

        btnChuQuan.setOnClickListener(v -> {
            role = "chuquan";
        });

        return view;
    }

    private boolean checkValidate(String emailS, String passS, String ncpass) {

        if(!passS.equals(ncpass)) {
            cpass.setError("Không giống với password");
            return false;
        }
        else if(emailS.isEmpty())
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
