package com.example.addressbook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    @SuppressLint("StaticFieldLeak")
    protected static ContactAdapter adapter;
    private ContactAdapter.OnItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        //Display contacts in a RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ContactAdapter(this, getAllItems(), listener);
        recyclerView.setAdapter(adapter);
        adapter.swapCursor(getAllItems());

        //Display floating button used to add a new contact
        FloatingActionButton fab = findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewContact.class);
                startActivityForResult(intent, 1);
            }
        });

        //Detects when a contact has been swiped and calls a method to delete the contact
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
    }

    //Removes a contact from the database when swiped
    protected void removeItem(long id) {
        database.delete(Database.ContactsEntry.TABLE_NAME,
                Database.ContactsEntry._ID + "=" + id, null);

        adapter.swapCursor(getAllItems());

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Contact deleted", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");

                if (result != null) {
                    adapter.swapCursor(getAllItems()); //Update cursor to display new contact

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "New contact added", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }
        else if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");

                if (result != null) {
                    adapter.swapCursor(getAllItems()); //Update cursor to remove contact

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Contact deleted", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            if (resultCode == Activity.RESULT_CANCELED) {
                adapter.swapCursor(getAllItems());
            }
        }
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
