package com.example.laurdroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.laurdroid.Models.User;
import com.example.laurdroid.R;
import com.example.laurdroid.Repos.UserRepository;
import com.example.laurdroid.services.Session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {
    private UserRepository userRepository;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRepository = new UserRepository(getApplication());

        EditText nameEditText = findViewById(R.id.edit_text_name);
        EditText emailEditText = findViewById(R.id.edit_text_email);
        EditText passwordEditText = findViewById(R.id.edit_text_password);
        Button registerButton = findViewById(R.id.button_register);

        registerButton.setOnClickListener(v -> {
            String name =nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if(isValidNameEmailAndPassword(name, email, password)) {
                Session.getInstance(getApplicationContext()).createLoginSession(email, password);
                try {
                    register(name, email, password);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    public void onLoginButtonClick(View view) {

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            progressBar.setVisibility(View.GONE);
        }, 200);
    }


    public boolean isValidNameEmailAndPassword(String name, String email, String password) {
        if(TextUtils.isEmpty(name)){
            Toast.makeText(getApplicationContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 8) {
            Toast.makeText(getApplicationContext(), "Please enter a valid password (at least 8 characters)", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void register(String name, String email, String password) throws NoSuchAlgorithmException {
        User user = new User(name, email, hashThisPass(password));

        userRepository.insert(user);

        Toast.makeText(getApplicationContext(), "Successful register!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public static String hashThisPass(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();

    }
}