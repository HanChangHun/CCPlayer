package com.ccplayer.community;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if user has logged in already
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            // Change activity
            finish();
            startActivity(new Intent(this, BoardsActivity.class));
        }

        // Listener
        // Sign up
        ((Button) findViewById(R.id.btn_signup)).setOnClickListener(v -> {
            // Change activity
            startActivity(new Intent(this, SignupActivity.class));
        });

        // Login
        ((Button) findViewById(R.id.btn_login)).setOnClickListener(v -> {
            // Change activity
            startActivity(new Intent(this, LoginActivity.class));
        });

        // No login mode
        ((Button) findViewById(R.id.btn_nologin)).setOnClickListener(v -> {
            // Change activity
            startActivity(new Intent(this, BoardsActivity.class));
        });

        // Set actionbar title
        ActionBar bar = getSupportActionBar();
        bar.setTitle("어서오세요!");
    }
}