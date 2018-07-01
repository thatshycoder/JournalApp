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

/**
 * Helper glass for getting and setting diary contents
 */
public class DiaryEntry {
    private String mDate;
    private String mEntryContent;
    private String mEntryId;

    public DiaryEntry() {

    }

    public DiaryEntry(String entry, String date, String entryId) {
        mEntryContent = entry;
        mDate = date;
        mEntryId = entryId;

    }

    /**
     * Gets entry content
     */
    public String getEntryContent() {
        return mEntryContent;
    }

    /**
     * Gets entry date
     */
    public String getEntryDate() {
        return mDate;
    }

    /**
     * Gets entry entry id
     */
    public String getEntryId() {
        return mEntryId;
    }

    /**
     * Sety entry content
     */
    public void setEntryContent(String mEntryContent) {
        this.mEntryContent = mEntryContent;
    }

    /**
     * Set entry date
     */
    public void setEntryDate(String mDate) {
        this.mDate = mDate;
    }

    /**
     * Set entry id
     */
    public void setEntryId(String mEntryId) {
        this.mEntryId = mEntryId;
    }
}
