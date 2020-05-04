package com.solar.pinterest.solarmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import com.google.gson.GsonBuilder;
import com.solar.pinterest.solarmobile.network.Network;
import com.solar.pinterest.solarmobile.network.models.LoginData;
import com.solar.pinterest.solarmobile.network.models.ProfileResponse;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.network.tools.TimestampConverter;
import com.solar.pinterest.solarmobile.storage.AuthRepo;
import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.RepositoryInterface;
import com.solar.pinterest.solarmobile.storage.SolarRepo;


import com.google.gson.Gson;
import com.solar.pinterest.solarmobile.storage.StatusEntity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.EventListener;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.solar.pinterest.solarmobile.storage.StatusEntity.*;

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
