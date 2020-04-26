package com.solar.pinterest.solarmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solar.pinterest.solarmobile.network.Network;
import com.solar.pinterest.solarmobile.network.models.ProfileResponse;
import com.solar.pinterest.solarmobile.network.models.RegistrationData;
import com.solar.pinterest.solarmobile.storage.SolarRepo;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegistrationActivity extends AppCompatActivity {

    Button toLoginBtn;
    Button registrationBtn;

    TextInputLayout textInputEmail;
    TextInputLayout textInputNickname;
    TextInputLayout textInputPassword;
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

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
        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!confirmInput(v)) {
                    return;
                }
                RegistrationData registrationData = new RegistrationData(textInputEmail.getEditText().getText().toString(), textInputPassword.getEditText().getText().toString(), textInputNickname.getEditText().getText().toString());

                Callback registrationCallback = new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        errorTextView.setText("Сервер временно недоступен");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        ProfileResponse profileResponse = gson.fromJson(response.body().string(), ProfileResponse.class);
                        if (!profileResponse.body.info.equals("OK")) {
                            errorTextView.setText(profileResponse.body.info);
                            return;
                        }
                        List<HttpCookie> cookies = HttpCookie.parse(response.header("Set-Cookie"));
                        for (HttpCookie cookie : cookies) {
                            String cookieName = cookie.getName();
                            if (cookieName.equals("session_key")) {
                                SolarRepo.get(getApplication()).setSessionCookie(cookie);
                                break;
                            }
                        }


                        Intent intent = new Intent(RegistrationActivity.this, YourProfileActivity.class);
                        startActivity(intent);
                    }
                };

                Network.getInstance().registration(registrationData, registrationCallback);
            }
        });
    }

    private boolean emailValidation() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Поле должно быть заполнено");
            return false;
        } else {
            textInputEmail.setError(null);
//            textInputEmail.setErrorEnabled(false);
            return true;
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
        } else {
            textInputNickname.setError(null);
            return  true;
        }
    }

    private boolean passwordValidation() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Поле должно быть заполнено");
            return false;
        } else if (passwordInput.length() < 6 || passwordInput.length() > 30) {
            textInputPassword.setError("Длина никнейма от 6 до 30 символов");
            return false;
        } else {
            textInputPassword.setError(null);
            return  true;
        }
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
