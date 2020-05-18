package com.solar.pinterest.solarmobile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PinViewFragment extends Fragment {
    String KEY_ID = "id";
    int pinId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pinId = getArguments().getInt(KEY_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pin_view_fragment, container, false);
        getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.VISIBLE);

        Log.d(KEY_ID, "" + pinId);

        return view;
    }
}
