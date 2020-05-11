package com.solar.pinterest.solarmobile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class IndexPageFragment extends Fragment {

    int NUM_COLUMNS = 2;

    private ArrayList<String> pinImagesUrls = new ArrayList<>();
    private ArrayList<String> pinTitles = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.index_page, container, false);
        addData();
        createRecyclerView(view, container.getContext());

        return view;
    }

    private void addData() {
        pinImagesUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        pinTitles.add("Привет!");

        pinImagesUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        pinTitles.add("");

        pinImagesUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        pinTitles.add("Что нового?");

        pinImagesUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        pinTitles.add("Интересная погода!");

        pinImagesUrls.add("https://solarsunrise.ru/static/pin/d7/d76dd9d60ca86d2781308fc9a09e114e.jpg");
        pinTitles.add("Привет!");

        pinImagesUrls.add("https://weneedfun.com/wp-content/uploads/2016/10/Vintage-Photography-Desktop-Wallpapers-17-1024x576.jpg");
        pinTitles.add("");

        pinImagesUrls.add("https://i.pinimg.com/736x/3a/84/ad/3a84ad5639df6dea8ab49fe299a3926a.jpg");
        pinTitles.add("Что нового?");

        pinImagesUrls.add("https://solarsunrise.ru/static/pin/d7/d76dd9d60ca86d2781308fc9a09e114e.jpg");
        pinTitles.add("Интересная погода!");

        pinImagesUrls.add("https://solarsunrise.ru/static/pin/d7/d76dd9d60ca86d2781308fc9a09e114e.jpg");
        pinTitles.add("");

        pinImagesUrls.add("https://i.pinimg.com/736x/3a/84/ad/3a84ad5639df6dea8ab49fe299a3926a.jpg");
        pinTitles.add("Вау!");
    }

    private void createRecyclerView(View view, Context context) {
        RecyclerView recyclerView = view.findViewById(R.id.index_page_recycler_view);
        StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter(context, pinTitles, pinImagesUrls);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }
}
