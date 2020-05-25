package com.solar.pinterest.solarmobile.storage;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solar.pinterest.solarmobile.EventBus.Event;
import com.solar.pinterest.solarmobile.EventBus.EventBus;
import com.solar.pinterest.solarmobile.R;
import com.solar.pinterest.solarmobile.network.Network;
import com.solar.pinterest.solarmobile.network.models.Board;
import com.solar.pinterest.solarmobile.network.models.CreateBoardData;
import com.solar.pinterest.solarmobile.network.models.CreatePinData;
import com.solar.pinterest.solarmobile.network.models.User;
import com.solar.pinterest.solarmobile.network.models.responses.CreateBoardResponse;
import com.solar.pinterest.solarmobile.network.models.LoginData;
import com.solar.pinterest.solarmobile.network.models.RegistrationData;
import com.solar.pinterest.solarmobile.network.models.responses.MyBoardsResponse;
import com.solar.pinterest.solarmobile.network.tools.TimestampConverter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BoardRepo {
    private static final String TAG = "Solar.BoardRepo";
    private static BoardRepo instance;
    private Application mContext;
    private SolarDatabase mDatabase;

    public static BoardRepo get(Application app) {
        if (instance == null) {
            instance = new BoardRepo(app);
        }
        return instance;

    }

    private BoardRepo(Application app) {
        mContext = app;

        mDatabase = SolarDatabase.get(app);
    }

    public MutableLiveData<StatusEntity> createBoard(@NonNull String title, @NonNull String description) {
        MutableLiveData<StatusEntity> progress = new MutableLiveData<>(new StatusEntity(StatusEntity.Status.IN_PROGRESS));

        CreateBoardData boardData = new CreateBoardData(title, description);
        Network.getInstance().addBoard(AuthRepo.get(mContext).getSessionCookie(), boardData,
                AuthRepo.get(mContext).getCsrfToken(), createBoardResponseCallback(progress));

        return progress;
    }

    public MutableLiveData<Pair<List<DBSchema.Board>, StatusEntity>> getMyBoards() {
        MutableLiveData<Pair<List<DBSchema.Board>, StatusEntity>> result = new MutableLiveData<>();
        Network.getInstance().getMyBoards(AuthRepo.get(mContext).getSessionCookie(), getMyBoardsResponseCallback(result));

        return result;
    }


    private Callback createBoardResponseCallback(MutableLiveData<StatusEntity> progress) {
        return new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                progress.postValue(new StatusEntity(
                        StatusEntity.Status.FAILED,
                        mContext.getString(R.string.network_answer_500)
                ));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                CreateBoardResponse createBoardResponse = gson.fromJson(response.body().string(), CreateBoardResponse.class);
                if (!createBoardResponse.body.info.equals("data successfully saved")) {
                    progress.postValue(new StatusEntity(
                            StatusEntity.Status.FAILED,
                            createBoardResponse.body.info
                    ));
                    return;
                }

                progress.postValue(new StatusEntity(StatusEntity.Status.SUCCESS));
            }
        };
    }

    private Callback getMyBoardsResponseCallback(MutableLiveData<Pair<List<DBSchema.Board>, StatusEntity>> progress) {
        return new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                progress.postValue(Pair.create(null, new StatusEntity(
                        StatusEntity.Status.FAILED,
                        mContext.getString(R.string.network_answer_500)
                )));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                MyBoardsResponse myBoardsResponse = gson.fromJson(response.body().string(), MyBoardsResponse.class);
                if (!myBoardsResponse.body.info.equals("OK")) {
                    progress.postValue(Pair.create(null, new StatusEntity(
                            StatusEntity.Status.FAILED,
                            myBoardsResponse.body.info
                    )));
                    return;
                }
                Board[] boards = myBoardsResponse.body.boards;
                List<DBSchema.Board> listOfBoard = new ArrayList<>();
                Date date = new Date();
                for (Board boa : boards) {
                    DBSchema.Board board = new DBSchema.Board(boa.id, boa.category, boa.isDeleted, boa.ownerId, boa.title, boa.description, boa.viewPin, date);
                    listOfBoard.add(board);
                }

                for (DBSchema.Board board : listOfBoard) {
                    mDatabase.putBoard(board);
                }

                progress.postValue(Pair.create(listOfBoard, new StatusEntity(StatusEntity.Status.SUCCESS)));
            }
        };
    }

    private class BoardConverter {
        public DBSchema.Board Net2DB(Board net) {
            return new DBSchema.Board(net.id, net.category, net.isDeleted, net.ownerId, net.title, net.description,
                    net.viewPin, TimestampConverter.toDate(net.createdTime));
        }
    }
}
