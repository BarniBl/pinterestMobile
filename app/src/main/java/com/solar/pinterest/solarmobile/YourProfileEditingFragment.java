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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class YourProfileEditingFragment extends Fragment {

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

        okSettingsButton = view.findViewById(R.id.your_profile_editing_ok_button);
        okSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = confirmInput(v);
                if (flag) {
                    getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.VISIBLE);
                    getFragmentManager().beginTransaction().remove(YourProfileEditingFragment.this).commit();
                }
            }
        });

        exitButton = view.findViewById(R.id.your_profile_editing_exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // TODO
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && data != null) {
           avatarImage.setImageURI(data.getData());
        }
    }

    private boolean nicknameValidation() {
        String nicknameInput = textInputNickname.getEditText().getText().toString().trim();

        if (nicknameInput.isEmpty()) {
            textInputNickname.setError("Поле должно быть заполнено");
            return false;
        } else if (nicknameInput.length() < 3 || nicknameInput.length() > 30) {
            textInputNickname.setError("Длина никнейма от 3 до 30 символов");
            return false;
        } else if (!nicknameInput.matches("^[a-zA-Z0-9_]{3,30}$")) {
            textInputNickname.setError("Только символы латинского алфавита и нижнее подчёркивание");
            return false;
        }

        textInputNickname.setError(null);
        return  true;
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
}
