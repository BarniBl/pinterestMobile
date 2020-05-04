package com.solar.pinterest.solarmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;


import com.solar.pinterest.solarmobile.storage.StatusEntity;

import okhttp3.MediaType;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Solar.MainActivity";
    public static final MediaType JSON_TYPE = MediaType.parse("application/json");
    Button toRegistrationBtn;
    Button loginBtn;

    TextInputLayout textInputEmail;
    TextInputLayout textInputPassword;
    TextView errorTextView;
    AuthViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        textInputEmail = findViewById(R.id.login_view_email_layout);
        textInputPassword = findViewById(R.id.login_view_password_layout);
        errorTextView = findViewById(R.id.login_error_text_under_title);

        toRegistrationBtn = findViewById(R.id.login_to_registration_button);

        toRegistrationBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
        loginBtn = findViewById(R.id.login_view_button);
        loginBtn.setOnClickListener(v -> {
            if (!confirmInput(v)) {
                return;
            }
            LiveData<StatusEntity> authStatus = mViewModel.login(
                    textInputEmail.getEditText().getText().toString(),
                    textInputPassword.getEditText().getText().toString()
            );
            authStatus.observe(MainActivity.this, statusEntity -> {
                switch (statusEntity.getStatus()) {
                    case FAILED:
                        errorTextView.setText(statusEntity.getMessage());
                        break;
                    case SUCCESS:
                        Intent intent = new Intent(MainActivity.this, YourProfileActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        Log.e(TAG, "Unexpected authStatus value: " + statusEntity.getStatus().toString());
                }

            });
        });

        if (mViewModel.isAuthorized()) {
            Intent intent = new Intent(MainActivity.this, YourProfileActivity.class);
            startActivity(intent);
        }
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

    private boolean passwordValidation() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError(getString(R.string.field_must_be_filled));
            return false;
        }

        textInputPassword.setError(null);
        return  true;
    }

    public boolean confirmInput(View v) {
        if (!emailValidation() | !passwordValidation()) {
            return false;
        }

        String input = textInputEmail.getEditText().getText().toString();
        input += "\n";
        input += textInputPassword.getEditText().getText().toString();


        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
        return true;
    }
}
