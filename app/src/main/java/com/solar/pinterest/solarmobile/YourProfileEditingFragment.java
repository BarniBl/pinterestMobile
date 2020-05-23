package com.solar.pinterest.solarmobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solar.pinterest.solarmobile.network.Network;
import com.solar.pinterest.solarmobile.network.models.CreateBoardData;
import com.solar.pinterest.solarmobile.network.models.CreateBoardResponse;
import com.solar.pinterest.solarmobile.network.models.EditProfile;
import com.solar.pinterest.solarmobile.network.models.EditProfileResponse;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.network.tools.TimestampConverter;
import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.RepositoryInterface;
import com.solar.pinterest.solarmobile.storage.SolarRepo;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class YourProfileEditingFragment extends Fragment implements RepositoryInterface.Listener {
    public static final int PICK_IMAGE = 1;

    Button closeSettingsButton;
    ImageView avatarImage;
    ImageButton chooseAvatarButton;
    Button okSettingsButton;
    Button exitButton;

    TextInputLayout textInputName;
    TextInputLayout textInputSurname;
    TextInputLayout textInputNickname;
    TextInputLayout textInputStatus;

    TextView errorSettingsTextView;
    TextView textName;
    TextView textSurname;
    TextView textNickname;
    TextView textStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.your_profile_editing, container, false);
        getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.GONE);

        closeSettingsButton = view.findViewById(R.id.your_profile_editing_close_button);
        closeSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(YourProfileEditingFragment.this).commit();
            }
        });

        avatarImage = view.findViewById(R.id.your_profile_editing_image);
        textInputName = view.findViewById(R.id.your_profile_editing_name_field);
        textInputSurname = view.findViewById(R.id.your_profile_editing_surname_field);
        textInputNickname = view.findViewById(R.id.your_profile_editing_nickname_field);
        textInputStatus = view.findViewById(R.id.your_profile_editing_status_field);

        errorSettingsTextView = view.findViewById(R.id.your_profile_editing_error_field);
        textName = view.findViewById(R.id.your_profile_editing_name_field_text);
        textSurname = view.findViewById(R.id.your_profile_editing_surname_field_text);
        textNickname = view.findViewById(R.id.your_profile_editing_nickname_field_text);
        textStatus = view.findViewById(R.id.your_profile_editing_status_field_text);

        chooseAvatarButton = view.findViewById(R.id.profile_editing_image_button);
        chooseAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] mimeTypes = {"image/jpeg", "image/png"};

                Intent intent = new Intent();
                intent.setType("image/*").putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        SolarRepo.get(getActivity().getApplication()).getMasterUser(this);

        okSettingsButton = view.findViewById(R.id.your_profile_editing_ok_button);
        okSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = confirmInput(v);
                if (!flag) {
                    return;
                }

                EditProfile editProfile = new EditProfile(textInputName.getEditText().getText().toString(), textInputSurname.getEditText().getText().toString(), textInputNickname.getEditText().getText().toString(), textInputStatus.getEditText().getText().toString());

                Callback editProfileCallback = new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        errorSettingsTextView.setText("Сервер временно недоступен");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        EditProfileResponse editProfileResponse = gson.fromJson(response.body().string(), EditProfileResponse.class);
                        if (!editProfileResponse.body.info.equals("data successfully saved")) {
                            errorSettingsTextView.setText(editProfileResponse.body.info);
                            return;
                        }

                        SolarRepo.get(getActivity().getApplication()).setCsrfToken(editProfileResponse.csrf_token);

                        //getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.VISIBLE);

                        getFragmentManager().beginTransaction().remove(YourProfileEditingFragment.this).commit();
                    }
                };

                Network.getInstance().editProfile(SolarRepo.get(getActivity().getApplication()).getSessionCookie(), editProfile, SolarRepo.get(getActivity().getApplication()).getCsrfToken(), editProfileCallback);

            }
        });

        exitButton = view.findViewById(R.id.your_profile_editing_exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolarRepo.get(getActivity().getApplication()).onLogout();
                Intent intent = new Intent(getContext(), MainActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && data != null) {
            avatarImage.setImageURI(data.getData());
        }
    }

    private boolean nicknameValidation() {
        String nicknameInput = textInputNickname.getEditText().getText().toString().trim();

        if (nicknameInput.isEmpty()) {
            //textInputNickname.setError("Поле должно быть заполнено");
            return true;
        } else if (nicknameInput.length() < 3 || nicknameInput.length() > 30) {
            textInputNickname.setError("Длина никнейма от 3 до 30 символов");
            return false;
        } else if (!nicknameInput.matches("^[a-zA-Z0-9_]{3,30}$")) {
            textInputNickname.setError("Только символы латинского алфавита и нижнее подчёркивание");
            return false;
        }

        textInputNickname.setError(null);
        return true;
    }

    private boolean confirmInput(View v) {
        if (!nicknameValidation()) {
            return false;
        }

        String input = textInputName.getEditText().getText().toString().trim();
        input += "\n";
        input += textInputSurname.getEditText().getText().toString().trim();
        input += "\n";
        input += textInputNickname.getEditText().getText().toString().trim();
        input += "\n";
        input += textInputStatus.getEditText().getText().toString().trim();

        Log.d("ProfileEditing", input);
        return true;
    }

    @Override
    public void onReadUser(DBSchema.User user) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String path = getActivity().getApplicationContext().getString(R.string.backend_uri) + user.getAvatar();
                Glide.with(getActivity().getApplicationContext())
                        .load(path)
                        .placeholder(R.drawable.fix_user_photo)
                        .dontAnimate()  // Against the Bug with GIFs and Transition on CircleImageView
                        .into(avatarImage);
            }
        });
        //textName.setText(user.getName());
        //textSurname.setText(user.getSurname());
        //textNickname.setText(user.getUsername());
        //textStatus.setText(user.getStatus());
    }
}
