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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shycoder.dy.journalapp.R;
import com.shycoder.dy.journalapp.model.DiaryEntryModel;

/**
 * Displays all diary entries
 */
public class HomeActivity extends AppCompatActivity {
    private String mUserId;
    private DiaryEntryModel diaryEntryModel;
    private FirebaseAuth mFirebaseAuth;
    private static boolean mPersistenceEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // enable offline persistence if it was not previously enabled
        if (!mPersistenceEnable) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            mPersistenceEnable = true;
        }

        mFirebaseAuth = FirebaseAuth.getInstance();

        // checks if user is logged in
        if (mFirebaseAuth.getCurrentUser() == null) {

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else {

            mUserId = mFirebaseAuth.getCurrentUser().getUid();

            TextView email = findViewById(R.id.text_email);
            email.setText(mFirebaseAuth.getCurrentUser().getEmail());

            diaryEntryModel = new DiaryEntryModel(this, mUserId);

            ListView listView = findViewById(R.id.listView);
            listView.setAdapter(diaryEntryModel.getDiaryEntries(this));

            // set up floating action bar
            FloatingActionButton fab = findViewById(R.id.fab_add);

            fab.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AddEntryActivity.class);
                    startActivity(intent);

                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.home_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.signout:

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);

                startActivity(intent);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
