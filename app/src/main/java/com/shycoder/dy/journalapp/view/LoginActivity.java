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

package com.shycoder.dy.journalapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.shycoder.dy.journalapp.R;
import com.shycoder.dy.journalapp.model.LoginActivityModel;

/**
 * Logs user in
 */
public class LoginActivity extends AppCompatActivity {

    private TextView mErrorMessageView;
    public LoginActivityModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        mModel = new LoginActivityModel(this);

        TextView registerLink = findViewById(R.id.text_register_link);
        mErrorMessageView = findViewById(R.id.text_error_message);

        final EditText email = findViewById(R.id.edit_email);
        final EditText password = findViewById(R.id.edit_password);

        Button manualLoginButton = findViewById(R.id.button_login);
        Button googleLoginButton = findViewById(R.id.button_google_signin);

        // Listens to when manual login button is clicked
        manualLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // checks if login was successful, or get corresponding error message
                if (mModel.initiateManualLogin(email.getText().toString(), password.getText().toString())) {
                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    enableErrorMessageView();
                    mErrorMessageView.setText(mModel.getErrorMessage());
                }
            }
        });

        // Listens to when google login button is clicked
        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = mModel.signInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);

            }
        });

        // Redirects to registration screen
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                mModel.initiateGoogleLogin(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e);
                // ...
            }
        }
    }

    /*
     * Enables error message view
     */
    private void enableErrorMessageView() {
        mErrorMessageView.setVisibility(View.VISIBLE);
    }
}
