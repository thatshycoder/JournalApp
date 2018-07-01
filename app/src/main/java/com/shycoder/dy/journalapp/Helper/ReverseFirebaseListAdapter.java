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

package com.shycoder.dy.journalapp.Helper;

import android.app.Activity;
import android.view.View;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Overrides FirebaseListAdapter to show entry contents in descending order
 * @param <DiaryEntry>
 */
public class ReverseFirebaseListAdapter<DiaryEntry> extends FirebaseListAdapter<DiaryEntry> {

    public ReverseFirebaseListAdapter(Activity activity, Class<DiaryEntry> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    public ReverseFirebaseListAdapter(Activity activity, Class<DiaryEntry> modelClass, int modelLayout, DatabaseReference ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(View v, DiaryEntry model, int position) {

    }

    @Override
    public DiaryEntry getItem(int position) {
        return super.getItem(getCount() - (position + 1));
    }
}
