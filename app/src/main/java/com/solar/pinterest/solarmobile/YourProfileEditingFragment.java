package com.solar.pinterest.solarmobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class YourProfileEditingFragment extends Fragment {

    Button closeSettingsButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.your_profile_editing, container, false);
        getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.GONE);

        closeSettingsButton = view.findViewById(R.id.your_profile_editing_close_button);
        closeSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(YourProfileEditingFragment.this).commit();
            }
        });
        return view;
    }

}
