package com.example.memorieskeeper.services;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * A component responsible for authenticating with Google
 */
public class CustomGoogleAuth {
    private static GoogleSignInClient mGoogleSignInClient;

    /**
     * Create a Google sign in client
     * @param activity current activity
     * @return the sign in client object
     */
    private static GoogleSignInClient createClient(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("320345093698-cek38qs1d9t13ajfofm8ha8vc1ejdakq.apps.googleusercontent.com")
                .requestEmail()
                .build();

        return GoogleSignIn.getClient(activity, gso);
    }

    /**
     * Retrieve the Google sign in client. Use this method to get information about the current user.
     * @param activity
     * @returnthe sign in client object
     */
    public static GoogleSignInClient getGoogleSignInClient(Activity activity) {
        if (mGoogleSignInClient == null) {
            mGoogleSignInClient = createClient(activity);
        }
        return mGoogleSignInClient;
    }
}
