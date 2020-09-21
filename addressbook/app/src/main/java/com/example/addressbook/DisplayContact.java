package com.example.addressbook;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class DisplayContact extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_display);

        //Display button to edit a contact
        Button editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContact();
            }
        });

        //Display button to delete a contact
        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });
    }

    private void editContact() {

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Changes updated", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void deleteContact() {

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Contact deleted", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
