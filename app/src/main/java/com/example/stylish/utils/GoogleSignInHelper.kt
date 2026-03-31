package com.example.stylish.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object GoogleSignInHelper {

    // This is your Web Client ID (OAuth 2.0 client ID) from Firebase Console
    private const val WEB_CLIENT_ID = "280666599112-o1jprl0nqbpdgd5ur87t4muketqa2l6k.apps.googleusercontent.com"
    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        Log.d("GoogleSignInHelper", "Creating GoogleSignInClient with web client ID: $WEB_CLIENT_ID")

        @Suppress("DEPRECATION") // You’re seeing GoogleSignInOptions is deprecated because Google is slowly moving to the new Google Identity Services / Credential Manager API.

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)  // Request ID token for Firebase Auth
            .requestEmail()                  // Request email address
            .requestProfile()                // Request profile information
            .build()

        Log.d("GoogleSignInHelper", "GoogleSignInOptions configured successfully")
        return GoogleSignIn.getClient(context, gso)
    }
}

