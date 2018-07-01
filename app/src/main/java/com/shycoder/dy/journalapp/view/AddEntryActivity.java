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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.shycoder.dy.journalapp.R;
import com.shycoder.dy.journalapp.model.DiaryEntryModel;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * HomeActivity
 */
public class AddEntryActivity extends AppCompatActivity {
    private String mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DiaryEntryModel mModel;
    private EditText mEntryContent;
    private TextView mErrorMessage;
    private String mEntryDate;
    private String mEntryId;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_entry);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mModel = new DiaryEntryModel(this, mUserId);

        Format formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        final String date = formatter.format(new Date());

        mErrorMessage = findViewById(R.id.text_error_message);
        mEntryContent = findViewById(R.id.text_entry_content);
        Button doneButton = findViewById(R.id.button_done);

        final Bundle extras = getIntent().getExtras();

        // checks if a user is editing an entry
        if (extras != null) {
            String entryContent = extras.getString("entryContentKey");

            mEntryDate = extras.getString("entryDate");
            mEntryId = extras.getString("entryId");

            editEntry(entryContent);

        }

        // listens to when done button is clicked
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entry = mEntryContent.getText().toString();

                if (!TextUtils.isEmpty(entry)) {

                    if (!mModel.addEntry(entry, date, mEntryId, mEntryDate)) {

                        mErrorMessage.setVisibility(View.VISIBLE);
                        mErrorMessage.setText(mModel.getErrorMessage());
                    }

                } else {
                    mErrorMessage.setVisibility(View.VISIBLE);
                    mErrorMessage.setText("Entry content cannot be empty");
                }
            }
        });
    }

    /**
     * Sets the content to be edited
     * @param entryContent
     */
    private void editEntry(String entryContent) {
        mEntryContent.setText(entryContent, TextView.BufferType.EDITABLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
