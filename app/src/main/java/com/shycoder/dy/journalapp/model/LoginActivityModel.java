/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shycoder.dy.journalapp.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shycoder.dy.journalapp.view.HomeActivity;

/**
 * Communicates with database to perform login actions
 */
public class LoginActivityModel {

    private String mLoginStatus;
    private Context mContext;
    private String mErrorMessage;
    private FirebaseAuth mFirebaseAuth;
    public GoogleSignInClient signInClient;

    public LoginActivityModel(Context context) {

        mFirebaseAuth = FirebaseAuth.getInstance();
        mContext = context;

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("582316455885-grvodl3e3oten7npputtc44fad9l8ta4.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(context, gso);

    }

    /**
     * Logs user in with email and password
     * @param email
     * @param password
     * @return boolean
     */
    public Boolean initiateManualLogin(String email, String password) {
        email = email.trim();
        password = password.trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    mLoginStatus = mLoginStatus.concat("true");
                }
            });

            if(mLoginStatus == null) {
               mErrorMessage = "Invalid username or password";
               return false;
            } else {
                return true;
            }

        } else {
            mErrorMessage = "Email/Password cannot be empty";
        }

        return false;
    }

    /**
     * Logs user in with google
     * @param acct
     */
    public void initiateGoogleLogin(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();

            }
        });
    }

    /**
     * Gets error messages for the module
     * @return String
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }
}
