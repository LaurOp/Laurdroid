package com.example.laurdroid.ViewAux;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.laurdroid.DashboardActivity;
import com.example.laurdroid.MainActivity;
import com.example.laurdroid.R;
import com.example.laurdroid.services.Session;

public class HeaderFragment extends Fragment {


    private ImageButton backButton;
    private ImageButton signOutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        // Initialize the back arrow and sign out button
        backButton = view.findViewById(R.id.backHeaderButton);
        signOutButton = view.findViewById(R.id.signOutHeaderButton);

        // Set onClickListeners for the buttons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back arrow click
                getActivity().onBackPressed();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getInstance(getContext()).logoutUser();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "Signed out", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}