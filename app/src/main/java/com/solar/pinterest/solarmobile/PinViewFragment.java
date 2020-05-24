package com.solar.pinterest.solarmobile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PinViewFragment extends Fragment {
    String KEY_ID = "id";
    int pinId;

    Button сlosePinBtn;
    Button savePinBtn;

    ImageView pinImage;
    TextView pinTitle;
    TextView pinDescription;
    TextView pinAuthor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pinId = getArguments().getInt(KEY_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pin_view_fragment, container, false);
        getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.GONE);

        Log.d(KEY_ID, "" + pinId);

        сlosePinBtn = view.findViewById(R.id.pin_view_fragment_close_button);
        сlosePinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment();
            }
        });

        savePinBtn = view.findViewById(R.id.pin_view_fragment_save_button);
        pinImage = view.findViewById(R.id.pin_view_fragment_image_pin);
        pinTitle = view.findViewById(R.id.pin_view_fragment_title_pin);
        pinDescription = view.findViewById(R.id.pin_view_fragment_description_pin);
        pinAuthor = view.findViewById(R.id.pin_view_fragment_author_nickname);

        return view;
    }

    public void replaceFragment() {
        Fragment fragment = new YourProfileFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.your_profile_view_relativeLayout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
