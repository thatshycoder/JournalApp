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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Communicates with Database to register users
 */
public class RegisterActivityModel {

    private Boolean mRegisterStatus = false;
    private Context mContext;
    private String mErrorMessage;
    private FirebaseAuth mFirebaseAuth;
    public GoogleSignInClient signInClient;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public RegisterActivityModel(Context context) {
        mContext = context;
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("582316455885-grvodl3e3oten7npputtc44fad9l8ta4.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(context, gso);
    }

    /**
     * Initiates manual registration
     * @param email
     * @param password
     * @return
     */
    public Boolean initiateManualRegister(String email, String password) {

        // sign user out user if they are currently logged in and they visit the registration page
        if (mFirebaseAuth.getCurrentUser() != null) {
            mFirebaseAuth.signOut();
        }

        email = email.trim();
        password = password.trim();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            if (matcher.find()) {

                // checks if password is not less than 6 characters
                if (!(password.length() < 6)) {
                    // if email is valid register
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent intent = new Intent(mContext, HomeActivity.class);
                            mContext.startActivity(intent);

                            ((Activity) mContext).finish();

                            mRegisterStatus = true;
                        }
                    });
                } else {
                    mErrorMessage = "Please input atleasr 6 characters password";
                }
            } else {
                mErrorMessage = "Please input a valid email";
            }

        } else {
            mErrorMessage = "Email/Password cannot be empty";
        }

        return mRegisterStatus;
    }

    /**
     * Initiates google registration
     * @param acct
     */
    public void initiateGoogleRegister(GoogleSignInAccount acct) {

        // sign user out user if they are currently logged in and they visit the registration page
        if (mFirebaseAuth.getCurrentUser() != null) {
            mFirebaseAuth.signOut();
        }

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
     * Gets error message for the current module
     * @return String.
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }
}
