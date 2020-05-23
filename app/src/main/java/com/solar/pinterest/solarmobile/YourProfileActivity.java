package com.solar.pinterest.solarmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import com.solar.pinterest.solarmobile.network.models.User;

import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.solar.pinterest.solarmobile.storage.StatusEntity;

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

        // load profile
//        LiveData<Pair<User, StatusEntity>> liveUser = mViewModel.getMasterUser();
//        liveUser.observe(this, pair -> {
//            if (pair.second.getStatus() == StatusEntity.Status.FAILED) {
//                errorTextYourProfile.setText(pair.second.getMessage());
//                return;
//            } else if(pair.second.getStatus() == StatusEntity.Status.EMPTY) {
//                Intent intent = new Intent(YourProfileActivity.this, MainActivity.class);
//                startActivity(intent);
//                return;
//            }
//            if (pair.second.getStatus() != StatusEntity.Status.SUCCESS) {
//                return;
//            }
//
//            User user = pair.first;
//
//            String path = getApplicationContext().getString(R.string.backend_uri) + user.avatarDir;
//            Glide.with(getApplicationContext())
//                    .load(path)
//                    .placeholder(R.drawable.fix_user_photo)
//                    .dontAnimate()  // Against the Bug with GIFs and Transition on CircleImageView
//                    .into(yourProfileAvatarImage);
//
//            yourProfileNickname.setText(user.username);
//            yourProfileStatus.setText(user.status);
//        });
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
}
