package com.example.addressbook;

import android.provider.BaseColumns;

public class Database {

    //Empty constructor
    private Database() {
    }

    /* Inner class defining the table contents */
    public static class ContactsEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_PHONE_NUMBER = "phonenumber";
        public static final String COLUMN_EMAIL = "email";
    }
}
