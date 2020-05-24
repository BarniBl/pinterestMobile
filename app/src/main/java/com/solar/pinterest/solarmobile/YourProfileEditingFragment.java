package com.solar.pinterest.solarmobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.solar.pinterest.solarmobile.EventBus.Event;
import com.solar.pinterest.solarmobile.EventBus.EventBus;
import com.solar.pinterest.solarmobile.network.models.EditProfile;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.storage.StatusEntity;

public class YourProfileEditingFragment extends Fragment {
    private static final String TAG = "SolarMobile.ProfileEdit";
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
                replaceFragment();
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

        LiveData<Pair<User, StatusEntity>> liveUser = ((YourProfileActivity) getActivity()).getViewModel().getMasterUser();
        liveUser.observe(getViewLifecycleOwner(), pair -> {
            onUserLoaded(pair);
        });

        okSettingsButton = view.findViewById(R.id.your_profile_editing_ok_button);
        okSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = confirmInput(v);

                if (!flag) {
                    return;
                }

                EditProfile editProfile = new EditProfile(textInputName.getEditText().getText().toString(), textInputSurname.getEditText().getText().toString(), textInputNickname.getEditText().getText().toString(), textInputStatus.getEditText().getText().toString());
                LiveData<StatusEntity> liveStatus = ((YourProfileActivity) getActivity()).getViewModel().editMasterUser(editProfile);
                liveStatus.observe(getViewLifecycleOwner(), res -> {
                    onUserEdited(res);
                });
            }
        });

        exitButton = view.findViewById(R.id.your_profile_editing_exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.get().emit(new Event(getString(R.string.event_logout)));
//                AuthRepo.get(getActivity().getApplication()).logout();
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

    public void replaceFragment() {
        Fragment fragment = new YourProfileFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.your_profile_view_relativeLayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void onUserLoaded(Pair<User, StatusEntity> pair) {
        switch (pair.second.getStatus()) {
            case FAILED:
                errorSettingsTextView.setText(pair.second.getMessage());
                break;
            case EMPTY:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
            case SUCCESS:
                User user = pair.first;

                String path = getActivity().getApplicationContext().getString(R.string.backend_uri) + user.avatarDir;
                Glide.with(getActivity().getApplicationContext())
                        .load(path)
                        .placeholder(R.drawable.fix_user_photo)
                        .dontAnimate()  // Against the Bug with GIFs and Transition on CircleImageView
                        .into(avatarImage);

                textName.setText(user.name);
                textSurname.setText(user.surname);
                textNickname.setText(user.username);
                textStatus.setText(user.status);
            default:
                break;
        }
    }

    public void onUserEdited(StatusEntity res) {
        switch (res.getStatus()) {
            case FAILED:
                errorSettingsTextView.setText(res.getMessage());
                break;
            case SUCCESS:
                replaceFragment();
                break;
            default:
                Log.e(TAG, "Unexpected authStatus value: " + res.getStatus().toString());
        }
    }
}
