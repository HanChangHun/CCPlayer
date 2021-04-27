package com.example.layout_test.ui.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;
import com.example.layout_test.ui.login.CommunitySignupFragment;

public class CommunityFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.screen_community, container, false);

        // Set listeners
        root.findViewById(R.id.btn_login).setOnClickListener(v -> {
            // Move fragment to login page
            FragmentManager manager = ((MainActivity) getActivity()).getSupportFragmentManager();

            while (manager.getBackStackEntryCount() > 0)
            {
                manager.popBackStackImmediate();
            }
            manager.beginTransaction().replace(R.id.nav_host_fragment, new CommunitySignupFragment()).addToBackStack(null).commit();
        });
        root.findViewById(R.id.btn_signup).setOnClickListener(v -> {
            // Move fragment to signup page
            FragmentManager manager = ((MainActivity) getActivity()).getSupportFragmentManager();

            while (manager.getBackStackEntryCount() > 0)
            {
                manager.popBackStackImmediate();
            }
            manager.beginTransaction().replace(R.id.nav_host_fragment, new CommunitySignupFragment()).addToBackStack(null).commit();
        });

        return root;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // R.menu.top_menu_calendar로 메뉴 만들기
        inflater.inflate(R.menu.top_menu_calendar, menu);
    }
}
