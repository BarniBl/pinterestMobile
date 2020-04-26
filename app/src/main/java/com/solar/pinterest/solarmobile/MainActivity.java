package com.solar.pinterest.solarmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.SolarDatabase;
import com.solar.pinterest.solarmobile.storage.StorageInterface;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
public class MainActivity extends AppCompatActivity implements StorageInterface.Listener{


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {
    public static final MediaType JSON_TYPE = MediaType.parse("application/json");
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
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        loginBtn = findViewById(R.id.login_view_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag = confirmInput(v);
                if (flag) {
                    Intent intent = new Intent(MainActivity.this, YourProfileActivity.class);
                    startActivity(intent);
                }

                //confirmInput(v);
                LoginData loginData = new LoginData(textInputEmail.getEditText().getText().toString(), textInputPassword.getEditText().getText().toString());

/*                Network.getInstance().getSolarSunriseApi().login(loginData).enqueue(new retrofit2.Callback<retrofit2.Response>() {
                    @Override
                    public void onResponse(retrofit2.Call<retrofit2.Response> call, retrofit2.Response<retrofit2.Response> response) {
                        System.out.println(response.body().toString());
                    }

                    @Override
                    public void onFailure(retrofit2.Call<retrofit2.Response> call, Throwable t) {
                        t.printStackTrace();
                    }
                });*/

                OkHttpClient client = new OkHttpClient();
                Gson gson = new Gson();
                String json = gson.toJson(loginData);
                RequestBody body = RequestBody.create(JSON_TYPE, json);

                Request request = new Request.Builder()
                        .url("https://solarsunrise.ru/api/v1/login")
                        .method("POST", body)
                        .build();

                Log.println(Log.DEBUG, "DEBUG", json);
                Log.println(Log.DEBUG, "DEBUG", body.toString());
                Log.println(Log.DEBUG, "DEBUG", request.toString());
                Log.println(Log.DEBUG, "DEBUG", request.body().toString());
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        System.out.println(response.body().string());
                    }
                });

            }
        });

        SolarDatabase.get(getApplication()).putUser(
                new DBSchema.User(129, "Tamerlanchik", "Name", "Sur",
                        "aaa@ss.er", 123, "Alive", "dwe/dwedwe.jpg",
                        true, "2019-12-14 15:21")
        );
        SolarDatabase.get(getApplication()).getUser(129, this);
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
}
