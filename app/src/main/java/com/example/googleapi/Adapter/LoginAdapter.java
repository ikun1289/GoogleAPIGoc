package com.example.googleapi.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.googleapi.Login.LoginTabFragment;
import com.example.googleapi.Login.SignupTabFragment;


public class LoginAdapter extends FragmentPagerAdapter {
    public LoginAdapter(@NonNull  FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                LoginTabFragment loginTabFragmen = new LoginTabFragment();
                return loginTabFragmen;
            case 1:
                SignupTabFragment signupTabFragment = new SignupTabFragment();
                return signupTabFragment;
            default:
              return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                return title = "Đăng nhập";
            case 1:
                return title = "Đăng ký";
        }

        return title;
    }
}
