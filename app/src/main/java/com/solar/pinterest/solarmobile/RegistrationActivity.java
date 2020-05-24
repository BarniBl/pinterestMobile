package com.solar.pinterest.solarmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "Solar.RegistrationActiv";

    Button toLoginBtn;
    Button registrationBtn;

    TextInputLayout textInputEmail;
    TextInputLayout textInputNickname;
    TextInputLayout textInputPassword;
    TextView errorTextView;

    AuthViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        mViewModel.getAuthStatus().observe(this, statusEntity -> {
            switch (statusEntity.getStatus()) {
                case FAILED:
                    errorTextView.setText(statusEntity.getMessage());
                    break;
                case SUCCESS:
                    Intent intent = new Intent(RegistrationActivity.this, YourProfileActivity.class);
                    startActivity(intent);
                    break;
                default:
                    Log.e(TAG, "Unexpected authStatus value: " + statusEntity.getStatus().toString());
            }

        });

        textInputEmail = findViewById(R.id.registration_view_email_layout);
        textInputNickname = findViewById(R.id.registration_view_nickname_layout);
        textInputPassword = findViewById(R.id.registration_view_password_layout);
        errorTextView = findViewById(R.id.registration_error_text_under_title);

        toLoginBtn = findViewById(R.id.registration_to_login_button);
        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        registrationBtn = findViewById(R.id.registration_view_button);
        registrationBtn.setOnClickListener(v -> {
            if (!confirmInput(v)) {
                return;
            }
            mViewModel.register(
                    textInputEmail.getEditText().getText().toString(),
                    textInputNickname.getEditText().getText().toString(),
                    textInputPassword.getEditText().getText().toString()
            );
        });
    }

    private boolean emailValidation() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError(getString(R.string.field_must_be_filled));
            return false;
        } else if(!emailInput.matches("^.+@.+\\.[a-zA-Z]+$")){
            textInputEmail.setError(getString(R.string.please_give_right_email));
            return false;
        }

        textInputEmail.setError(null);
        return true;
    }

    private boolean nicknameValidation() {
        String nicknameInput = textInputNickname.getEditText().getText().toString().trim();

        if (nicknameInput.isEmpty()) {
            textInputNickname.setError(getString(R.string.field_must_be_filled));
            return false;
        } else if (nicknameInput.length() < 3 || nicknameInput.length() > 30) {
            textInputNickname.setError(getString(R.string.nickname_length_range));
            return false;
        } else if (!nicknameInput.matches("^[a-zA-Z0-9_]{3,30}$")) {
            textInputNickname.setError(getString(R.string.nickname_wrong_symbols));
            return false;
        }

        textInputNickname.setError(null);
        return  true;
    }

    private boolean passwordValidation() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError(getString(R.string.field_must_be_filled));
            return false;
        } else if (passwordInput.length() < 6 || passwordInput.length() > 30) {
            textInputPassword.setError(getString(R.string.password_length_range));
            return false;
        }

        textInputPassword.setError(null);
        return  true;
    }

    public boolean confirmInput(View v) {
        if (!emailValidation() | !nicknameValidation() | !passwordValidation()) {
            return false;
        }

        String input = textInputEmail.getEditText().getText().toString();
        input += "\n";
        input += textInputNickname.getEditText().getText().toString();
        input += "\n";
        input += textInputPassword.getEditText().getText().toString();

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();

        return true;
    }
}
