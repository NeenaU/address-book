package com.example.addressbook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    protected static ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        //Display contacts in a RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(this, getAllItems());
        recyclerView.setAdapter(adapter);

        //Display floating button used to add a new contact
        FloatingActionButton fab = findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewContact.class);
                startActivity(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
    }

    protected void removeItem(long id) {
        database.delete(Database.ContactsEntry.TABLE_NAME,
                Database.ContactsEntry._ID + "=" + id, null);

        adapter.swapCursor(getAllItems());

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
