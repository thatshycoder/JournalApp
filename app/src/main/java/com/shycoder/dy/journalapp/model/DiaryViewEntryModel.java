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
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shycoder.dy.journalapp.Helper.DateConverter;
import com.shycoder.dy.journalapp.Helper.DiaryEntry;
import com.shycoder.dy.journalapp.R;
import com.shycoder.dy.journalapp.view.AddEntryActivity;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Performs ViewEntryActivity actions like showing the full entry content or deleting entry
 */
public class DiaryViewEntryModel {
    private DatabaseReference mDatabaseReference;

    public DiaryViewEntryModel(String userId) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference("users").child(userId);

    }

    /**
     * Sets the full entry content
     * @param entryId
     * @param activity
     */
    public void setViewContent(String entryId, final Activity activity) {

        mDatabaseReference.child(entryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TextView entryContent = activity.findViewById(R.id.text_entry_content);
                TextView dateView = activity.findViewById(R.id.text_date);

                DiaryEntry entry = dataSnapshot.getValue(DiaryEntry.class);

                Format formatter = new SimpleDateFormat("dd MMM yyyy.", Locale.getDefault());

                if (entry != null) {

                    try {
                        Date date = new DateConverter().convertToDate(entry.getEntryDate());
                        dateView.setText(formatter.format(date));

                    } catch (Exception e) {
                        Log.e("DiaryViewEntryModel", "Unable to parse date");
                    }

                    entryContent.setText(entry.getEntryContent());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ViewEntryModel", "Failed to read value.", error.toException());
            }
        });
    }

    /**
     * Deletes entry
     * @param entryId
     */
    public void deleteEntry(String entryId) {
        mDatabaseReference.child(entryId).setValue(null);
    }

    /**
     * Populates AddEntryActivity
     * @param entryId
     * @param activity
     */
    public void getEntryContent(String entryId, final Activity activity) {
        mDatabaseReference.child(entryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DiaryEntry entry = dataSnapshot.getValue(DiaryEntry.class);

                if (entry != null) {

                    Intent intent = new Intent(activity, AddEntryActivity.class);
                    intent.putExtra("entryContentKey", entry.getEntryContent());
                    intent.putExtra("entryDate", entry.getEntryDate());
                    intent.putExtra("entryId", entry.getEntryId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ViewEntryModel", "Failed to read value.", error.toException());
            }
        });
    }

}
