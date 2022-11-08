package com.example.nightlifekosovo.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.nightlifekosovo.Fragments.AddPostFragment;
import com.example.nightlifekosovo.Fragments.EventsFragment;
import com.example.nightlifekosovo.Fragments.ProfileFragment;
import com.example.nightlifekosovo.Fragments.SearchFragment;
import com.example.nightlifekosovo.R;
import com.example.nightlifekosovo.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private ActivityMainBinding binding;

    SharedPreferences sharedPreferences;
    Toolbar myToolbar;
    FragmentManager fm;

    BottomNavigationView bottomNavigationView;

    EventsFragment eventsFragment;
    SearchFragment searchFragment;
    AddPostFragment addPostFragment;
    ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        eventsFragment = new EventsFragment();
        searchFragment = new SearchFragment();
        addPostFragment = new AddPostFragment();
        profileFragment = new ProfileFragment();

        fm = getSupportFragmentManager();
        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        bottomNavigationView = binding.activityMainBottomNavigation;
        bottomNavigationView.setOnItemSelectedListener(bottomNavigationSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.fragment_events);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    NavigationBarView.OnItemSelectedListener bottomNavigationSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.fragment_events:
                    fm.beginTransaction().replace(R.id.activity_main_navigation_host_fragment, eventsFragment, "1").commit();
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_item_events);
                    return true;

                case R.id.fragment_search:
                    fm.beginTransaction().replace(R.id.activity_main_navigation_host_fragment, searchFragment, "2").commit();
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_item_search);
                    return true;

                case R.id.fragment_add_post:
                    fm.beginTransaction().replace(R.id.activity_main_navigation_host_fragment, addPostFragment, "3").commit();
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_item_add_post);
                    return true;

                case R.id.fragment_profile:
                    fm.beginTransaction().replace(R.id.activity_main_navigation_host_fragment, profileFragment, "4").commit();
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_item_profile);
                    return true;

            }
            return false;
        }
    };


    @Override
    public void onBackStackChanged() {
        super.onBackPressed();
    }
}