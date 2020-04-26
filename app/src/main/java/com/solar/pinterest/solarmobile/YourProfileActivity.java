package com.solar.pinterest.solarmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solar.pinterest.solarmobile.network.Network;
import com.solar.pinterest.solarmobile.network.models.ProfileResponse;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.RepositoryInterface;
import com.solar.pinterest.solarmobile.storage.SolarRepo;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class YourProfileActivity extends AppCompatActivity implements RepositoryInterface.Listener {

    Button addPinsBoardsButton;
    Fragment selectedFragment;

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
        try {
            SolarRepo.get(getApplication()).getMasterUser(this);
        } catch (SQLException exception) {

            HttpCookie cookie = SolarRepo.get(getApplication()).getSessionCookie();
            if (cookie == null) {
                Intent intent = new Intent(YourProfileActivity.this, MainActivity.class);
                startActivity(intent);
                return;
            }


            Callback profileDataCallback = new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    //errorTextView.setText("Сервер временно недоступен");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    ProfileResponse profileResponse = gson.fromJson(response.body().string(), ProfileResponse.class);
                    if (!profileResponse.body.info.equals("OK")) {
                        //errorTextView.setText(profileResponse.body.info);
                        return;
                    }
                    User user = profileResponse.body.user;
                    List<HttpCookie> cookies = HttpCookie.parse(response.header("Set-Cookie"));
                    for (HttpCookie cookie : cookies) {
                        String cookieName = cookie.getName();
                        if (cookieName.equals("session_key")) {
                            SolarRepo.get(getApplication()).setSessionCookie(cookie);
                            break;
                        }
                    }

                    SolarRepo.get(getApplication()).setMasterUser(
                            new DBSchema.User(user.id, user.username, user.name, user.surname,
                                    user.email, user.age, user.status, user.avatarDir,
                                    user.isActive, user.createdTime, false));



                }
            };

            Network.getInstance().profileData(cookie, profileDataCallback);
        }
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

    @Override
    public void onReadUser(DBSchema.User user) {
        user.
                Log.d("Solar", "Got values");
    }
}
