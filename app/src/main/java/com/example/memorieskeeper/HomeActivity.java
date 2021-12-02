package com.example.memorieskeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.memorieskeeper.services.CustomGoogleAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btnLogout).setOnClickListener(view -> {
            CustomGoogleAuth.getGoogleSignInClient(this).signOut().addOnCompleteListener(result -> {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    }
}