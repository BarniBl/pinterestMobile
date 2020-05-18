package com.solar.pinterest.solarmobile;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.solar.pinterest.solarmobile.adapter.DataSourse;
import com.solar.pinterest.solarmobile.profileFragments.ProfileBoardsListFragment;
import com.solar.pinterest.solarmobile.profileFragments.ProfilePinsListFragment;

public class YourProfileFragment extends Fragment {

    Button addPinsBoardsButton;
    Button settingsButton;
    TextView errorTextYourProfile;

    ImageView yourProfileAvatarImage;
    TextView yourProfileNickname;
    TextView yourProfileStatus;

    Fragment selectedFragment;

    private DataSourse dataForPins = new DataSourse();
    private DataSourse dataForBoards = new DataSourse();

    Button openPinsBtn;
    Button openBoardsBtn;

    private int forPinsBoardsPlace = R.id.your_profile_doard_pin_f_place;
    private int placeForChangeFragment = R.id.your_profile_view_relativeLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.your_profile_fragment, container, false);
        getActivity().findViewById(R.id.your_profile_bottom_navigation).setVisibility(View.VISIBLE);

        showBoards();

        BottomNavigationView bottomNavBar = getActivity().findViewById(R.id.your_profile_bottom_navigation);
        bottomNavBar.getMenu().getItem(2).setChecked(true);

        errorTextYourProfile = view.findViewById(R.id.your_profile_view_error_field);

        yourProfileAvatarImage = view.findViewById(R.id.your_profile_view_image_field);
        yourProfileNickname = view.findViewById(R.id.your_profile_view_nickname_field);
        yourProfileStatus = view.findViewById(R.id.your_profile_view_status_field);

        addPinsBoardsButton = view.findViewById(R.id.your_profile_buttons_plus_button);
        addPinsBoardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionBox();
            }
        });

        settingsButton = view.findViewById(R.id.your_profile_buttons_edit_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFragment = new YourProfileEditingFragment();
                replaceFragment(selectedFragment, placeForChangeFragment);
            }
        });

        openBoardsBtn = view.findViewById(R.id.your_profile_buttons_board_button);
        openBoardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBoards();
            }
        });

        openPinsBtn = view.findViewById(R.id.your_profile_buttons_pin_button);
        openPinsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPins();
            }
        });

        return view;
    }

    private void showSelectionBox() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.create_pin_board_choose_card);

        Button dialogCloseButton = (Button)dialog.findViewById(R.id.create_pin_board_choose_card_close);
        Button dialogChooseBoard = (Button)dialog.findViewById(R.id.create_pin_board_choose_card_add_board);
        Button dialogChoosePin = (Button)dialog.findViewById(R.id.create_pin_board_choose_card_add_pin);

        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogChooseBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                selectedFragment = new CreateBoardFragment();
                replaceFragment(selectedFragment, placeForChangeFragment);
            }
        });

        dialogChoosePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                selectedFragment = new CreatePinFragment();
                replaceFragment(selectedFragment, placeForChangeFragment);
            }
        });

        dialog.show();
    }

    private void replaceFragment(Fragment fragment, int placeId) {
        assert getFragmentManager() != null;
        getFragmentManager()
                .beginTransaction()
                .replace(placeId, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showBoards() {
        selectedFragment = new ProfileBoardsListFragment();
        replaceFragment(selectedFragment, forPinsBoardsPlace);
    }

    private void showPins() {
        selectedFragment = new ProfilePinsListFragment();
        replaceFragment(selectedFragment, forPinsBoardsPlace);
    }
//
//    public void getBoards() {
//
//    }
//
//    public void getPins() {
//
//    }
}
