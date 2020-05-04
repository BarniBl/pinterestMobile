package com.solar.pinterest.solarmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

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


    Button addPinsBoardsButton;
    Button settingsButton;
    TextView errorTextYourProfile;

    ImageView yourProfileAvatarImage;
    TextView yourProfileNickname;
    TextView yourProfileStatus;

    Fragment selectedFragment;
    YourProfileViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_profile);
        mViewModel = new ViewModelProvider(this).get(YourProfileViewModel.class);

        errorTextYourProfile = findViewById(R.id.your_profile_view_error_field);

        yourProfileAvatarImage = findViewById(R.id.your_profile_view_image_field);
        yourProfileNickname = findViewById(R.id.your_profile_view_nickname_field);
        yourProfileStatus = findViewById(R.id.your_profile_view_status_field);

        addPinsBoardsButton = findViewById(R.id.your_profile_buttons_plus_button);
        addPinsBoardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionBox();
            }
        });

        // load profile
        LiveData<Pair<User, StatusEntity>> liveUser = mViewModel.getMasterUser();
        liveUser.observe(this, pair -> {
            if (pair.second.getStatus() == StatusEntity.Status.FAILED) {
                errorTextYourProfile.setText(pair.second.getMessage());
                return;
            } else if(pair.second.getStatus() == StatusEntity.Status.EMPTY) {
                Intent intent = new Intent(YourProfileActivity.this, MainActivity.class);
                startActivity(intent);
                return;
            }
            if (pair.second.getStatus() != StatusEntity.Status.SUCCESS) {
                return;
            }

            User user = pair.first;

            String path = getApplicationContext().getString(R.string.backend_uri) + user.avatarDir;
            Glide.with(getApplicationContext())
                    .load(path)
                    .placeholder(R.drawable.fix_user_photo)
                    .dontAnimate()  // Against the Bug with GIFs and Transition on CircleImageView
                    .into(yourProfileAvatarImage);

            yourProfileNickname.setText(user.username);
            yourProfileStatus.setText(user.status);
        });

        settingsButton = findViewById(R.id.your_profile_buttons_edit_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFragment = new YourProfileEditingFragment();
                replaceFragment(selectedFragment);
            }
        });

        findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.VISIBLE);
    }

    private void showSelectionBox() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.create_pin_board_choose_card);

        Button dialogCloseButton = (Button) dialog.findViewById(R.id.create_pin_board_choose_card_close);
        Button dialogChooseBoard = (Button) dialog.findViewById(R.id.create_pin_board_choose_card_add_board);
        Button dialogChoosePin = (Button) dialog.findViewById(R.id.create_pin_board_choose_card_add_pin);

        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogChooseBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                selectedFragment = new CreateBoardFragment();
                replaceFragment(selectedFragment);
            }
        });

        dialogChoosePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                selectedFragment = new CreatePinFragment();
                replaceFragment(selectedFragment);
            }
        });

        dialog.show();
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.your_profile_view_relativeLayout, fragment)
                .addToBackStack(null)
                .commit();
    }

}
