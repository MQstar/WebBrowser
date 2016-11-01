/*
 * Copyright 2016, The Android Open Source Project
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

package com.demo.qx.webbrowser.data.source.Local;

import android.provider.BaseColumns;

public final class PersistenceContract {

    private PersistenceContract() {
    }

    public static abstract class Bookmarks implements BaseColumns {
        public static final String TABLE_NAME = "bookmarks";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ADDRESS = "address";
    }
    public static abstract class History implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_DATE = "date";
    }
    public static abstract class Download implements BaseColumns {
        public static final String TABLE_NAME = "download";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_SIZE = "size";
        public static final String COLUMN_NAME_CURRENT = "current";

    }
}
