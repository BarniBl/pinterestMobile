package com.solar.pinterest.solarmobile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class CreateBoardFragment extends Fragment {

    Button closeButton;
    Button okButton;

    TextInputLayout textInputTitle;
    TextInputLayout textInputDiscription;
    TextView errorTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_board, container, false);
        getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.GONE);

        textInputTitle = view.findViewById(R.id.create_board_title_field);
        textInputDiscription = view.findViewById(R.id.create_board_description_field);
        errorTextView = view.findViewById(R.id.create_board_error_field);

        closeButton = view.findViewById(R.id.create_board_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment();
            }
        });

        okButton = view.findViewById(R.id.create_board_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = confirmInput(v);
                if (flag) {
                    // Ваш TODO CODE
                    replaceFragment();
                }
            }
        });

        return view;
    }

    private boolean titleValidation() {
        String titleInput = textInputTitle.getEditText().getText().toString().trim();

        if (titleInput.isEmpty()) {
            textInputTitle.setError("Поле должно быть заполнено");
            return false;
        }
        textInputTitle.setError(null);
        return true;
    }

    private boolean confirmInput(View v) {
        if (!titleValidation()) {
            return false;
        }

        String input = textInputTitle.getEditText().getText().toString().trim();
        input += "\n";
        input += textInputDiscription.getEditText().getText().toString().trim();

        Log.d("CreateBoard", input);
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
}
