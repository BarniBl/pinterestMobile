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
import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.SolarDatabase;
import com.solar.pinterest.solarmobile.storage.DBInterface;
import com.solar.pinterest.solarmobile.storage.SolarRepo;

import java.net.HttpCookie;

public class MainActivity extends AppCompatActivity implements DBInterface.Listener{

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
                confirmInput(v);
            }
        });

        test();
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
            return  true;
        }
    }

    public void confirmInput(View v) {
        if (!emailValidation() | !passwordValidation()) {
            return;
        }

        String input = textInputEmail.getEditText().getText().toString();
        input += "\n";
        input += textInputPassword.getEditText().getText().toString();



        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReadUser(DBSchema.User user) {
        DBSchema.User ds = user;
        Log.d("Solar", "Got values");
    }

    void test() {
        SolarDatabase.get(getApplication()).putUser(
                new DBSchema.User(129, "Tamerlanchik", "Name", "Sur",
                        "aaa@ss.er", 123, "Alive", "dwe/dwedwe.jpg",
                        true, "2019-12-14 15:21", true)
        );
        SolarDatabase.get(getApplication()).getUser(129, this);
        HttpCookie cookie = new HttpCookie(getApplicationContext().getString(R.string.session_cookie), "ffwfewfef");
        SolarRepo.get(getApplication()).setSessionCookie(cookie);
        HttpCookie cookie2 = SolarRepo.get(getApplication()).getSessionCookie();

    }
}
