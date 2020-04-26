package com.solar.pinterest.solarmobile;

import androidx.appcompat.app.AppCompatActivity;

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
import com.solar.pinterest.solarmobile.network.models.LoginResponse;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.RepositoryInterface;
import com.solar.pinterest.solarmobile.storage.SolarDatabase;
import com.solar.pinterest.solarmobile.storage.SolarRepo;

import java.net.HttpCookie;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements RepositoryInterface.Listener {
    Button toRegistrationBtn;
    Button loginBtn;

    TextInputLayout textInputEmail;
    TextInputLayout textInputPassword;
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        textInputEmail = findViewById(R.id.login_view_email_layout);
        textInputPassword = findViewById(R.id.login_view_password_layout);
        errorTextView = findViewById(R.id.login_error_text_under_title);

        toRegistrationBtn = findViewById(R.id.login_to_registration_button);

        toRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YourProfileActivity.class);
                startActivity(intent);
            }
        });
        loginBtn = findViewById(R.id.login_view_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!confirmInput(v)) {
                    return;
                }

                LoginData loginData = new LoginData(textInputEmail.getEditText().getText().toString(), textInputPassword.getEditText().getText().toString());

                Callback loginCallback = new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        errorTextView.setText("Сервер временно недоступен");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        LoginResponse loginResponse = gson.fromJson(response.body().string(), LoginResponse.class);
                        User user = loginResponse.body.user;

                        SolarRepo.get(getApplication()).setMasterUser(
                                new DBSchema.User(user.id, user.username, user.name, user.surname,
                                        user.email, user.age, user.status, user.avatarDir,
                                        user.isActive, user.createdTime, false));

                        Intent intent = new Intent(MainActivity.this, YourProfileActivity.class);
                        startActivity(intent);
                    }
                };

                Network.getInstance().login(loginData, loginCallback);
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
            return true;
        }
    }

    private boolean passwordValidation() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Поле должно быть заполнено");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
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

    @Override
    public void onReadUser(DBSchema.User user) {
        DBSchema.User ds = user;
        Log.d("Solar", "Got values");
    }

    void test() {
        SolarRepo.get(getApplication()).setMasterUser(
                new DBSchema.User(2222, "Tamerlanchik", "Nameqwdqwdwq", "Sur",
                        "aaa@ss.er", 123, "Alive", "dwe/dwedwe.jpg",
                        true, "2019-12-14 15:21", true)
        );
        SolarRepo.get(getApplication()).getMasterUser(this);
        HttpCookie cookie = new HttpCookie(getApplicationContext().getString(R.string.session_cookie), "ffwfewfef");
        SolarRepo.get(getApplication()).setSessionCookie(cookie);
        HttpCookie cookie2 = SolarRepo.get(getApplication()).getSessionCookie();
        int a = 1;
    }
}
