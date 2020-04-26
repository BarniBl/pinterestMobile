package com.solar.pinterest.solarmobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.solar.pinterest.solarmobile.storage.DBInterface;
import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.SolarRepo;

import de.hdodenhof.circleimageview.CircleImageView;

public class YourProfileActivity extends AppCompatActivity {

    Button addPinsBoardsButton;
    Fragment selectedFragment;
    CircleImageView mAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_profile);

        addPinsBoardsButton = findViewById(R.id.your_profile_buttons_plus_button);
        addPinsBoardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionBox();
            }
        });

        mAvatar = findViewById(R.id.your_profile_image);
        SolarRepo.get(getApplication()).getMasterUser(new DBInterface.UserListener() {
            @Override
            public void onReadUser(DBSchema.User user) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String path = getApplicationContext().getString(R.string.backend_uri) + user.getAvatar();
                        Glide.with(getApplicationContext())
                            .load(path)
                            .placeholder(R.drawable.fix_user_photo)
                            .dontAnimate()  // Against the Bug with GIFs and Transition on CircleImageView
                            .into(mAvatar);
                    }
                });
            }
        });
    }

    private void showSelectionBox() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.create_pin_board_choose_card);

        Button dialogCloseButton = (Button)dialog.findViewById(R.id.create_pin_board_choose_card_close);
        Button dialogChooseBoard = (Button)dialog.findViewById(R.id.create_pin_board_choose_card_add_board);
        Button dialogChoosePin = (Button)dialog.findViewById(R.id.create_pin_board_choose_card_add_pin);

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

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.your_profile_view_relativeLayout, selectedFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        dialogChoosePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
