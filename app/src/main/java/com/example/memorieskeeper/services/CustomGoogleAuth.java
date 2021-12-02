package com.example.memorieskeeper.services;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class CustomGoogleAuth {
    private static GoogleSignInClient mGoogleSignInClient;

    private static GoogleSignInClient createClient(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id)) // TODO: figure out why this can't be referenced
                .requestIdToken("320345093698-cek38qs1d9t13ajfofm8ha8vc1ejdakq.apps.googleusercontent.com")
                .requestEmail()
                .build();

        return GoogleSignIn.getClient(activity, gso);
    }

    public static GoogleSignInClient getGoogleSignInClient(Activity activity) {
        if (mGoogleSignInClient == null) {
            mGoogleSignInClient = createClient(activity);
        }
        return mGoogleSignInClient;
    }
}
