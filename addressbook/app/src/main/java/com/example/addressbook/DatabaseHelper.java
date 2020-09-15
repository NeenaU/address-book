package com.example.addressbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.addressbook.Database.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    /*
    DatabaseHelper constructor
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    Method to create the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE " +
                ContactsEntry.TABLE_NAME + " (" +
                ContactsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContactsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ContactsEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                ContactsEntry.COLUMN_PHONE_NUMBER + " TEXT NOT NULL," +
                ContactsEntry.COLUMN_EMAIL + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ContactsEntry.TABLE_NAME);
        onCreate(db);
    }
}
