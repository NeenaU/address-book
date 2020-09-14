package com.example.addressbook;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class AddNewContact extends AppCompatActivity {

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_add_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        Button addButton = findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });
    }

    private void addContact() {
        EditText nameEntry = findViewById(R.id.name_entry);
        EditText addressEntry = findViewById(R.id.address_entry);
        EditText phoneEntry = findViewById(R.id.phone_number_entry);
        EditText emailEntry = findViewById(R.id.email_entry);

        String name = nameEntry.getText().toString();
        String address = addressEntry.getText().toString();
        String phone = phoneEntry.getText().toString();
        String email = emailEntry.getText().toString();

        //Some information is missing - display an error message
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Enter in all the information", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        //Put contact information into a content values and add to the database
        else {
            ContentValues cv = new ContentValues();
            cv.put(Database.ContactsEntry.COLUMN_NAME, name);
            cv.put(Database.ContactsEntry.COLUMN_ADDRESS, address);
            cv.put(Database.ContactsEntry.COLUMN_PHONE_NUMBER, phone);
            cv.put(Database.ContactsEntry.COLUMN_EMAIL, email);

            database.insert(Database.ContactsEntry.TABLE_NAME, null, cv);

            nameEntry.getText().clear();
            addressEntry.getText().clear();
            phoneEntry.getText().clear();
            emailEntry.getText().clear();
        }
    }
}
