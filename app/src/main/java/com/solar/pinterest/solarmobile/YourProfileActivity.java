package com.solar.pinterest.solarmobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class YourProfileActivity extends AppCompatActivity {

    Button addPinsBoardsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_profile);

        addPinsBoardsButton = findViewById(R.id.your_profile_buttons_plus_button);
        addPinsBoardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionBox();
            }
        });
    }

    private void showSelectionBox() {
        AlertDialog.Builder showDialod = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View choose_menu = inflater.inflate(R.layout.create_pin_board_choose_card, null);

        showDialod.setTitle("Создать");
        showDialod.setView(choose_menu);
        showDialod.setNegativeButton("[X]", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        showDialod.show();
    }
}
