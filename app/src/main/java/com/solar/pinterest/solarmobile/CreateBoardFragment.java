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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solar.pinterest.solarmobile.network.Network;
import com.solar.pinterest.solarmobile.network.models.Board;
import com.solar.pinterest.solarmobile.network.models.CreateBoardData;
import com.solar.pinterest.solarmobile.network.models.CreateBoardResponse;
import com.solar.pinterest.solarmobile.network.models.LoginData;
import com.solar.pinterest.solarmobile.network.models.ProfileResponse;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.storage.AuthRepo;
import com.solar.pinterest.solarmobile.storage.DBSchema;
import com.solar.pinterest.solarmobile.storage.RepositoryInterface;
import com.solar.pinterest.solarmobile.storage.SolarRepo;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CreateBoardFragment extends Fragment implements RepositoryInterface.Listener {

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
                getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(CreateBoardFragment.this).commit();
            }
        });

        okButton = view.findViewById(R.id.create_board_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = confirmInput(v);
                if (!flag) {
                    return;
                }
                CreateBoardData createBoardData = new CreateBoardData(textInputTitle.getEditText().getText().toString(), textInputDiscription.getEditText().getText().toString());

                Callback createBoardCallback = new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        errorTextView.setText("Сервер временно недоступен");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        CreateBoardResponse createBoardResponse = gson.fromJson(response.body().string(), CreateBoardResponse.class);
                        if (!createBoardResponse.body.info.equals("data successfully saved")) {
                            errorTextView.setText(createBoardResponse.body.info);
                            return;
                        }

                        AuthRepo.get(getActivity().getApplication()).setCsrfToken(createBoardResponse.csrf_token);

                        //getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.VISIBLE);
                        getFragmentManager().beginTransaction().remove(CreateBoardFragment.this).commit();
                    }
                };

                Network.getInstance().addBoard(AuthRepo.get(getActivity().getApplication()).getSessionCookie(), createBoardData, AuthRepo.get(getActivity().getApplication()).getCsrfToken(), createBoardCallback);
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

    @Override
    public void onReadUser(DBSchema.User user) {

    }
}
