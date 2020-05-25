package com.solar.pinterest.solarmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.solar.pinterest.solarmobile.ViewModels.YourProfileViewModel;

public class YourProfileActivity extends AppCompatActivity {

    Fragment selectedFragment;
    BottomNavigationView bottomNavBar;
    YourProfileViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_profile);

        mViewModel = new ViewModelProvider(this).get(YourProfileViewModel.class);

        selectedFragment = new YourProfileFragment();
        replaceFragment(selectedFragment);

        bottomNavBar = findViewById(R.id.your_profile_bottom_navigation);
        bottomNavBar.setOnNavigationItemSelectedListener(bottomNavListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()) {
                case R.id.bottom_navigation_home:
                    selectedFragment = new YourProfileFragment();
                    break;
                case R.id.bottom_navigation_plus:
                    selectedFragment = new CreatePinFragment();
                    break;
                case R.id.bottom_navigation_index:
                    selectedFragment = new IndexPageFragment();
                    break;
            }

            replaceFragment(selectedFragment);

            return true;
        }
    };

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.your_profile_view_relativeLayout, fragment)
                .addToBackStack(null)
                .commit();
    }
    
    // Для не разрешения вернуться назад по кнопке на телефоне
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    YourProfileViewModel getViewModel() {
        return mViewModel;
    }
}
