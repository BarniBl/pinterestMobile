package com.solar.pinterest.solarmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.google.android.material.textfield.TextInputLayout;
import com.solar.pinterest.solarmobile.network.models.Pin;
import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.StatusEntity;

import java.util.List;

public class CreatePinFragment extends Fragment {

    public static final int PICK_IMAGE = 1;
    String[] listItems;
    DBSchema.Board listBoardItem;
    int currentBoardID;

    Button closeButton;
    Button appPinImageButton;
    Button dialogWithBoards;
    Button toCreateBoard;
    Button okButton;

    TextView boardTitleView;
    ImageView pinImageView;

    TextInputLayout textInputTitle;
    TextInputLayout textInputDiscription;
    TextView errorTextView;

    List<DBSchema.Board> listOfUsersBoards;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_pin, container, false);
        getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.GONE);

        closeButton = view.findViewById(R.id.create_pin_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment();
            }
        });

        BitmapDrawable draw = (BitmapDrawable) pinImageView.getDrawable();
        Bitmap bitmap = draw.getBitmap();

        pinImageView = view.findViewById(R.id.create_pin_image_pin);
        toCreateBoard = view.findViewById(R.id.create_pin_go_to_create_board);
        boardTitleView = view.findViewById(R.id.create_pin_board_for_pin);

        textInputTitle = view.findViewById(R.id.create_pin_name_field);
        textInputDiscription = view.findViewById(R.id.create_pin_description_field);
        errorTextView = view.findViewById(R.id.create_pin_error_field);

        appPinImageButton = view.findViewById(R.id.create_pin_add_image_button);
        appPinImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] mimeTypes = {"image/jpeg", "image/png"};

                Intent intent = new Intent();
                intent.setType("image/*").putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        dialogWithBoards = view.findViewById(R.id.create_pin_choose_board);
        dialogWithBoards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showBoardsDialog();
            }
        });

        okButton = view.findViewById(R.id.create_pin_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = confirmInput(v);
                if (flag) {
                    Pin pin = new Pin(currentBoardID, textInputTitle)
                    ((YourProfileActivity) getActivity()).getViewModel().createPin();

                    replaceFragment();
                }
            }
        });

        LiveData<Pair<List<DBSchema.Board>, StatusEntity>> liveBoards = ((YourProfileActivity) getActivity()).getViewModel().getMyBoards();
        liveBoards.observe(getViewLifecycleOwner(), pair -> {
            onBoardsLoaded(pair);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && data != null) {
            pinImageView.setImageURI(data.getData());
        }
    }

    private void showBoardsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Выберите доску");

        if (listOfUsersBoards.size() == 0) {
            alertDialog.setMessage("Создайте доску");
        }

        alertDialog.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                listBoardItem = listOfUsersBoards.get(i);
                boardTitleView.setText(listBoardItem.getTitle());
                currentBoardID = listBoardItem.getId();
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("Назад", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
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

    private boolean boardValidation() {
        String board = boardTitleView.getText().toString().trim();
        if (board.isEmpty()) {
            errorTextView.setText("Выберите доску");
            return false;
        }
        return true;
    }

    private boolean pinImageValidation() {
        Log.d("CreatePin", "" + pinImageView.getDrawable());
        if (pinImageView.getDrawable() == null) {
            errorTextView.setText("Выберите изображение");
            return false;
        }
        return true;
    }

    private boolean confirmInput(View v) {
        errorTextView.setText("");

        if (!titleValidation() | !boardValidation() | !pinImageValidation()) {
            return false;
        }

        String input = textInputTitle.getEditText().getText().toString().trim();
        input += "\n";
        input += textInputDiscription.getEditText().getText().toString().trim();
        input += "\n";
        input += boardTitleView.getText().toString().trim();

        Log.d("CreatePin", input);
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

    public void onBoardsLoaded(Pair<List<DBSchema.Board>, StatusEntity> pair) {
        switch (pair.second.getStatus()) {
            case FAILED:

                break;
            case EMPTY:

                break;
            case SUCCESS:
                listOfUsersBoards = pair.first;
            default:
                break;
        }
    }
}
