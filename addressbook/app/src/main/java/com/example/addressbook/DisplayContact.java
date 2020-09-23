package com.example.addressbook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class DisplayContact extends AppCompatActivity {

    private SQLiteDatabase database;

    private EditText nameEntry;
    private EditText addressEntry;
    private EditText phoneEntry;
    private EditText emailEntry;

    private Button deleteButton;
    private Button editButton;
    private Button cancelButton;
    private Button confirmButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_display);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        nameEntry = findViewById(R.id.name_entry);
        addressEntry = findViewById(R.id.address_entry);
        phoneEntry = findViewById(R.id.phone_number_entry);
        emailEntry = findViewById(R.id.email_entry);

        nameEntry.setEnabled(false);
        addressEntry.setEnabled(false);
        phoneEntry.setEnabled(false);
        emailEntry.setEnabled(false);

        //Get id from main activity, find contact info and display
        Intent intent = getIntent();
        final long id = intent.getLongExtra("id", 0);
        getContactInfo(id);

        //Display button to edit a contact
        editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContact();
            }
        });

        //Display button to delete a contact
        deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact(id);
            }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setVisibility(View.GONE);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEdit();
            }
        });

        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setVisibility(View.GONE);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact(id);
            }
        });

        //Go back to the main screen when the back button is pressed
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    //Method that gets the information for a specific contact from the database using the contact's id
    private void getContactInfo(long id){
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.ContactsEntry.TABLE_NAME + " " +
                "WHERE " + Database.ContactsEntry._ID + "=" + id, null);
        cursor.moveToFirst();

        String name = cursor.getString(cursor.getColumnIndex(Database.ContactsEntry.COLUMN_NAME));
        String address = cursor.getString(cursor.getColumnIndex(Database.ContactsEntry.COLUMN_ADDRESS));
        String number = cursor.getString(cursor.getColumnIndex(Database.ContactsEntry.COLUMN_PHONE_NUMBER));
        String email = cursor.getString(cursor.getColumnIndex(Database.ContactsEntry.COLUMN_EMAIL));
        cursor.close();

        nameEntry.setText(name);
        addressEntry.setText(address);
        phoneEntry.setText(number);
        emailEntry.setText(email);
    }

    private void editContact() {

        //Allow the user to change contact info
        nameEntry.setEnabled(true);
        addressEntry.setEnabled(true);
        phoneEntry.setEnabled(true);
        emailEntry.setEnabled(true);

        //Hide delete button and show confirm and cancel button
        deleteButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.VISIBLE);
        confirmButton.setVisibility(View.VISIBLE);
    }

    private void cancelEdit() {

        //Prevent the user from changing contact info
        nameEntry.setEnabled(false);
        addressEntry.setEnabled(false);
        phoneEntry.setEnabled(false);
        emailEntry.setEnabled(false);

        //Hide confirm and cancel button and show delete button
        deleteButton.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.GONE);
        confirmButton.setVisibility(View.GONE);
    }

    private void updateContact(long id) {

        String name = nameEntry.getText().toString();
        String address = addressEntry.getText().toString();
        String phone = phoneEntry.getText().toString();
        String email = emailEntry.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(Database.ContactsEntry.COLUMN_NAME, name);
        cv.put(Database.ContactsEntry.COLUMN_ADDRESS, address);
        cv.put(Database.ContactsEntry.COLUMN_PHONE_NUMBER, phone);
        cv.put(Database.ContactsEntry.COLUMN_EMAIL, email);

        database.update(Database.ContactsEntry.TABLE_NAME, cv, "_id="+id, null);
        cancelEdit();

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Changes updated", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //Method to delete a contact from the database
    private void deleteContact(long id) {
        database.delete(Database.ContactsEntry.TABLE_NAME,
                Database.ContactsEntry._ID + "=" + id, null);

        //Go back to main activity
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","delete"); //indicate that a contact has just been deleted
        setResult(MainActivity.RESULT_OK,returnIntent);       //so the relevant snackbar can be shown in main activity
        finish();
    }
}
