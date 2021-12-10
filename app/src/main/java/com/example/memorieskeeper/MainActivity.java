package com.example.memorieskeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memorieskeeper.services.CustomGoogleAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

/*
    This activity is the first one to be opened by  the app.
    It displays the login screen.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";

    ActivityResultLauncher<Intent> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                    }
                });

        setContentView(R.layout.activity_main);

        findViewById(R.id.btnSignIn).setOnClickListener(view -> signIn());
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            forwardToHome();
        }
    }

    /**
     * Sign in with Google
     */
    private void signIn() {
        Intent signInIntent = CustomGoogleAuth.getGoogleSignInClient(this).getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    /**
     * Handle the Google sign in form. Start Firebase auth upon success
     * @param completedTask the result returned from the Google sign in form
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                firebaseAuthWithGoogle(account.getIdToken());
            }
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    /**
     * Add the Google user to Firebase
     * @param idToken the Google auth sign in token
     */
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        forwardToHome();
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    /*
        Opens up HomeActivity
     */
    private void forwardToHome() {
        startActivity(new Intent(this, HomeActivity.class));
    }
}