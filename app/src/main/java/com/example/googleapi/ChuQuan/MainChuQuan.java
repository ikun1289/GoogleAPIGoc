package com.example.googleapi.ChuQuan;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.googleapi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainChuQuan extends FragmentActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = new HomeFegmentChuQuan();
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chuquan);

        //mapping
        bottomNavigationView = findViewById(R.id.bottom_nav_chuquan);
        drawerLayout = findViewById(R.id.drawer_layout);
        //end mapping

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.nav_home_chu_quan: selectedFragment = new HomeFegmentChuQuan(); break;
                case R.id.nav_food_chu_quan: selectedFragment = new FoodListFragmentChuQuan(); break;
                case R.id.nav_restaurant_chu_quan: selectedFragment = new RestauDetailFragmentChuQuan(); break;
                default: selectedFragment = new HomeFegmentChuQuan(); break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chuquan,selectedFragment).commit();
            return true;
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chuquan,selectedFragment).commit();

        //Navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
}
