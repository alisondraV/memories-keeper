package com.example.memorieskeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btnLogout).setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            LoginActivity.signOutLauncher.launch(new Intent(this, LoginActivity.class));
        });
    }
}