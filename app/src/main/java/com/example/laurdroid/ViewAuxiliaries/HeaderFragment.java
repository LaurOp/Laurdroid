package com.example.laurdroid.ViewAuxiliaries;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.laurdroid.Activities.MainActivity;
import com.example.laurdroid.R;
import com.example.laurdroid.services.Session;

public class HeaderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        ImageButton backButton = view.findViewById(R.id.backHeaderButton);
        ImageButton signOutButton = view.findViewById(R.id.signOutHeaderButton);

        backButton.setOnClickListener(v -> getActivity().onBackPressed());

        signOutButton.setOnClickListener(v -> {
            Session.getInstance(getContext()).logoutUser();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            Toast.makeText(getActivity(), "Signed out", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}