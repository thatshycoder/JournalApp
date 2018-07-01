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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shycoder.dy.journalapp.Helper.DateConverter;
import com.shycoder.dy.journalapp.Helper.DiaryEntry;
import com.shycoder.dy.journalapp.Helper.ReverseFirebaseListAdapter;
import com.shycoder.dy.journalapp.R;
import com.shycoder.dy.journalapp.view.HomeActivity;
import com.shycoder.dy.journalapp.view.ViewEntryActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Communicates with database to perform CRUD operations
 */
public class DiaryEntryModel {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private String mUserId;
    private Boolean mReturnValue = false;
    private Activity mActivity;
    private String mErrorMessage;

    public DiaryEntryModel(Activity activity, String userId) {

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference("users");

        mUserId = userId;
        mActivity = activity;
    }

    /**
     * Adds diary entry to database
     * @param entryContent
     * @param date
     * @param entryId
     * @param oldDate
     * @return boolean
     */
    public Boolean addEntry(String entryContent, String date, String entryId, String oldDate) {

        // Generate a diaryEntryId
        String diaryEntryId = mDatabaseReference.push().getKey();

        // check if user is editing a previously created diary entry
        if (entryId != null) {
            diaryEntryId = entryId;

            if (oldDate != null) {
                date = oldDate;
            }

        }

        // Create DiaryEntry object with either contents to be edited or created.
        DiaryEntry diaryEntry = new DiaryEntry(entryContent, date, diaryEntryId);

        // push to diaryEntry node using the diaryEntryId
        mDatabaseReference.child(mUserId).child(diaryEntryId).setValue(diaryEntry, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                // if operation was successful, go back to home activity
                if (databaseError == null) {

                    mReturnValue = true;

                    Intent intent = new Intent(mActivity, HomeActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();

                } else {
                    mErrorMessage = "Something went wrong when trying to add your entry";
                }
            }
        });

        return mReturnValue;
    }

    /**
     * Gets all diary entries for home activity
     * @param activity
     * @return FirebaseListAdapter
     */
    public ReverseFirebaseListAdapter<DiaryEntry> getDiaryEntries(final Activity activity) {

        DatabaseReference entryReference = mDatabase.getReference("users/" + mUserId);

        // use firebase ui to populate listview
        ReverseFirebaseListAdapter<DiaryEntry> adapter = new ReverseFirebaseListAdapter<DiaryEntry>(activity, DiaryEntry.class, R.layout.item_entry_view_layout, entryReference) {

            @Override
            protected void populateView(View view, final DiaryEntry myObj, final int position) {

                String date = myObj.getEntryDate();
                DateConverter dateConverter = new DateConverter();

                // two simpledateformatter for formatter day and month
                SimpleDateFormat dateDayFormatter = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat dateMonthFormatter = new SimpleDateFormat("MMM", Locale.getDefault());

                try {
                    Date entryDate = dateConverter.convertToDate(date);

                    String entryDateDay = dateDayFormatter.format(entryDate);
                    String entryDateMonth = dateMonthFormatter.format(entryDate);

                    ((TextView) view.findViewById(R.id.text_date_day)).setText(entryDateDay);
                    ((TextView) view.findViewById(R.id.text_date_month)).setText(entryDateMonth);

                } catch (Exception e) {
                    Log.e("DiaryEntryModel","Couldn't parse date object");
                }

                ((TextView) view.findViewById(R.id.text_entry)).setText(myObj.getEntryContent());

                // listen to when an item from listview gets clicked for viewing and send intent to viewentryactivity
                view.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, ViewEntryActivity.class);
                        intent.putExtra("entryIdKey", myObj.getEntryId());
                        activity.startActivity(intent);
                    }
                });
            }
        };

        return adapter;
    }

    /**
     * Gets the module error messages
     * @return String
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }
}
