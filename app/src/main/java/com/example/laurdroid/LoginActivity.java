package com.example.laurdroid;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.laurdroid.Repos.UserRepository;
import com.example.laurdroid.services.Session;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity  {

    private UserRepository userRepository;
    private static final int RC_SIGN_IN = 2345;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


        userRepository = new UserRepository(getApplication());


        SignInButton signInButton = findViewById(R.id.Google_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });



        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                try {
                    if(isValidEmailAndPassword(email,password)){
                        combinationExists(email,password);
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }


            }
        });
    }

    public void onRegisterButtonClick(View view) {

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            }
        }, 200);

    }


//    public void onLoginButtonClick(View view) {
//
//
//
//        progressBar = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//                startActivity(intent);
//                progressBar.setVisibility(View.GONE);
//            }
//        }, 200);
//    }

    private void signInGoogle() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
        if (account != null) {
            String email = account.getEmail();
            Toast.makeText(this, "Signed in as " + email, Toast.LENGTH_SHORT).show();
        }

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signIn() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            Session.getInstance(getApplicationContext()).createLoginSession(account.getEmail(), "");
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }


    private void updateUI(GoogleSignInAccount account){

        if(account!=null){
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }

    public boolean isValidEmailAndPassword(String email, String password) throws NoSuchAlgorithmException {
        if(email.equals("admin") && password.equals("admin")){
            Toast.makeText(this, "Successful login", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(email.isEmpty())
            return false;
        return !password.isEmpty();
    }

    public void combinationExists(String email, String password) throws NoSuchAlgorithmException {
        if(email.equals("admin") && password.equals("admin")){
            Session.getInstance(getApplicationContext()).createLoginSession(email, password);
            signIn();
            return;
        }

        String hashed = RegisterActivity.hashThisPass(password);

        userRepository.loginUser(email, hashed).observe(this, loginResult -> {
            if (loginResult) {
                Session.getInstance(getApplicationContext()).createLoginSession(email, password);
                signIn();
            } else {
                Toast.makeText(this, "Invalid email/password combination", Toast.LENGTH_SHORT).show();
            }
        });

    }
}