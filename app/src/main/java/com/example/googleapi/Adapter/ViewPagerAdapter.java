package com.example.googleapi.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.googleapi.ChuQuan.FoodListFragmentChuQuan;
import com.example.googleapi.ChuQuan.HomeFegmentChuQuan;
import com.example.googleapi.ChuQuan.RestauDetailFragmentChuQuan;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments;

    public ViewPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior, List<Fragment> fragments) {
        super(fm, behavior);
        this.fragments = fragments;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
