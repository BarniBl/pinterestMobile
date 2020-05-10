package com.solar.pinterest.solarmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class YourProfileActivity extends AppCompatActivity {

    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_profile);

        selectedFragment = new YourProfileFragment();
        replaceFragment(selectedFragment);
    }

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
}
