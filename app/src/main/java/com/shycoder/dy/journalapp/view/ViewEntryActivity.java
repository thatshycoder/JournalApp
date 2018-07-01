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

import com.google.firebase.auth.FirebaseAuth;
import com.shycoder.dy.journalapp.R;
import com.shycoder.dy.journalapp.model.DiaryViewEntryModel;

/**
 * Shows an individual entry full content
 */
public class ViewEntryActivity extends AppCompatActivity {
    private DiaryViewEntryModel mModel;
    private String mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String mEntryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        mModel = new DiaryViewEntryModel(mUserId);
        String entryIdKey = "entryIdKey";

        // gets the details of the entry ro show
        Bundle extras = getIntent().getExtras();
        mEntryId = extras.getString(entryIdKey);

        mModel.setViewContent(mEntryId, this);

        // set up floating action bar
        FloatingActionButton fab = findViewById(R.id.fab_add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddEntryActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.edit_entry:
                mModel.getEntryContent(mEntryId, this);
                return true;

            case R.id.delete_entry:
                mModel.deleteEntry(mEntryId);

                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
