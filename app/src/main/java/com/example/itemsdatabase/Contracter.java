package com.example.itemsdatabase;

import android.provider.BaseColumns;

public class Contracter {
    private Contracter() {
    }
    public static final class RecordsEntry implements BaseColumns {
        public static final String TABLE_NAME = "recordList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
