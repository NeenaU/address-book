package com.example.addressbook;

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
    private ContactAdapter adapter = MainActivity.adapter;

    private EditText nameEntry;
    private EditText addressEntry;
    private EditText phoneEntry;
    private EditText emailEntry;
    private long id;

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

        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);
        getContactInfo(id);

        //Display button to edit a contact
        Button editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContact(id);
            }
        });

        //Display button to delete a contact
        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact(id);
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

        nameEntry.setText(name);
        addressEntry.setText(address);
        phoneEntry.setText(number);
        emailEntry.setText(email);
    }

    private void editContact(long id) {

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Changes updated", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //Method to delete a contact from the database
    private void deleteContact(long id) {
        database.delete(Database.ContactsEntry.TABLE_NAME,
                Database.ContactsEntry._ID + "=" + id, null);

        //Update the RecyclerView
        adapter.swapCursor(getAllItems());

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        //Show snackbar indicating the contact has been deleted
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Contact deleted", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //Method to get contacts from the database for the RecyclerView
    private Cursor getAllItems() {
        return database.query(
                Database.ContactsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Database.ContactsEntry.COLUMN_NAME
        );
    }
}
