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

import com.solar.pinterest.solarmobile.adapter.DataSourse;
import com.solar.pinterest.solarmobile.adapter.StaggeredRecyclerViewAdapter;

public class IndexPageFragment extends Fragment {

    int NUM_COLUMNS = 2;

    private DataSourse dataForIndex = new DataSourse();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.index_page, container, false);
        addData();
        createRecyclerView(view, container.getContext());

        return view;
    }

    private void addData() {
        dataForIndex.addDataItem("https://i.redd.it/obx4zydshg601.jpg", "Привет!", 1);
        dataForIndex.addDataItem("https://i.redd.it/glin0nwndo501.jpg", "", 2);
        dataForIndex.addDataItem("https://i.redd.it/k98uzl68eh501.jpg", "Как у тебя дела?", 3);
        dataForIndex.addDataItem("https://i.redd.it/tpsnoz5bzo501.jpg", "Красивый пейзаж", 4);
        dataForIndex.addDataItem("https://solarsunrise.ru/static/pin/d7/d76dd9d60ca86d2781308fc9a09e114e.jpg", "", 5);
        dataForIndex.addDataItem("https://weneedfun.com/wp-content/uploads/2016/10/Vintage-Photography-Desktop-Wallpapers-17-1024x576.jpg", "Очень интересно", 6);
        dataForIndex.addDataItem("https://i.pinimg.com/736x/3a/84/ad/3a84ad5639df6dea8ab49fe299a3926a.jpg", "", 7);
        dataForIndex.addDataItem("https://i.redd.it/glin0nwndo501.jpg", "Невероятно", 8);
    }

    private void createRecyclerView(View view, Context context) {
        RecyclerView recyclerView = view.findViewById(R.id.index_page_recycler_view);
        StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter(context, dataForIndex.getDataSource());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }
}
