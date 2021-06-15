package com.example.googleapi.ChuQuan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.googleapi.Adapter.ViewPagerAdapter;
import com.example.googleapi.Login.HomeActivity;
import com.example.googleapi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainChuQuan extends FragmentActivity {

    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    List<Fragment> fragments;
    DrawerLayout drawerLayout;
    ViewPagerAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chuquan);

        //mapping
        bottomNavigationView = findViewById(R.id.bottom_nav_chuquan);
        drawerLayout = findViewById(R.id.drawer_layout);
        viewPager = findViewById(R.id.fragment_container_chuquan);
        navigationView = findViewById(R.id.navView_chuquan);
        //end mapping

        setUpViewPager();


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_home_chu_quan: viewPager.setCurrentItem(0); break;
                case R.id.nav_food_chu_quan: viewPager.setCurrentItem(1); break;
                case R.id.nav_restaurant_chu_quan: viewPager.setCurrentItem(2); break;
                default: viewPager.setCurrentItem(0); break;
            }
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chuquan,selectedFragment).commit();
            return true;
        });
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chuquan,selectedFragment).commit();



        //Navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_QuanAn:drawerLayout.closeDrawers();break;
                    case R.id.nav_about:drawerLayout.closeDrawers();break;
                    case R.id.nav_Contact:drawerLayout.closeDrawers();break;
                    case R.id.nav_DX: signOut(); drawerLayout.closeDrawers();break;
                    default:drawerLayout.closeDrawers();break;
                }
                return true;
            }
        });
        //
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void setUpViewPager(){
        fragments = new ArrayList<>();
        fragments.add(new HomeFegmentChuQuan());
        fragments.add(new FoodListFragmentChuQuan());
        fragments.add(new RestauDetailFragmentChuQuan());

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0: bottomNavigationView.getMenu()
                            .findItem(R.id.nav_home_chu_quan)
                            .setChecked(true);
                            break;
                    case 1: bottomNavigationView.getMenu()
                            .findItem(R.id.nav_food_chu_quan)
                            .setChecked(true);
                        break;
                    case 2: bottomNavigationView.getMenu()
                            .findItem(R.id.nav_restaurant_chu_quan)
                            .setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
