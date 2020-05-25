package com.solar.pinterest.solarmobile.profileFragments;

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

import com.solar.pinterest.solarmobile.BoardViewFragment;
import com.solar.pinterest.solarmobile.PinViewFragment;
import com.solar.pinterest.solarmobile.R;
import com.solar.pinterest.solarmobile.adapter.DataSourse;
import com.solar.pinterest.solarmobile.adapter.StaggeredRecyclerViewAdapter;

public class ProfileBoardsListFragment extends Fragment {

    private DataSourse dataForBoards = new DataSourse();

    String URL_KEY = "url";
    String TITLE_KEY = "title";
    String ID_KEY = "id";

    int NUM_COLUMNS = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataForBoards.createDataList(getArguments().getStringArrayList(URL_KEY), getArguments().getStringArrayList(TITLE_KEY), getArguments().getIntegerArrayList(ID_KEY));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.your_profile_pins_boards_list_view_fragment, container, false);
        createRecyclerView(view, container.getContext());
        return view;
    }

    private void createRecyclerView(View view, Context context) {
        RecyclerView recyclerView = view.findViewById(R.id.your_profile_pins_list_place_view);
        StaggeredRecyclerViewAdapter staggeredRecyclerViewAdapter = new StaggeredRecyclerViewAdapter(
                context,
                dataForBoards.getDataSource(),
                new BoardViewFragment());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }
}
