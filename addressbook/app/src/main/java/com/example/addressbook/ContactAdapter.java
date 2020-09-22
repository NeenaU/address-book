package com.example.addressbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context mContext;
    protected Cursor mCursor;
    private OnItemClickListener mListener;

    public ContactAdapter(Context context, Cursor cursor, OnItemClickListener listener) {
        mContext = context;
        mCursor = cursor;
        mListener = listener;
    }

    public interface OnItemClickListener{
        void onClick(View v, int position);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView nameText;
        private TextView addressText;
        private TextView numberText;
        private TextView emailText;

        public ContactViewHolder(View itemView){
            super(itemView);

            nameText = itemView.findViewById(R.id.textview_name);
            addressText = itemView.findViewById(R.id.textview_address);
            numberText = itemView.findViewById(R.id.textview_phone_number);
            emailText = itemView.findViewById(R.id.textview_email);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(itemView, getAdapterPosition());
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(Database.ContactsEntry.COLUMN_NAME));
        String address = mCursor.getString(mCursor.getColumnIndex(Database.ContactsEntry.COLUMN_ADDRESS));
        String number = mCursor.getString(mCursor.getColumnIndex(Database.ContactsEntry.COLUMN_PHONE_NUMBER));
        String email = mCursor.getString(mCursor.getColumnIndex(Database.ContactsEntry.COLUMN_EMAIL));
        long id = mCursor.getLong(mCursor.getColumnIndex(Database.ContactsEntry._ID));

        holder.nameText.setText(name);
        holder.addressText.setText(address);
        holder.numberText.setText(number);
        holder.emailText.setText(email);
        holder.itemView.setTag(id);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity origin = (Activity) mContext;
                Intent intent = new Intent(origin, DisplayContact.class);

                long id = (long) holder.itemView.getTag();
                intent.putExtra("id", id);

                origin.startActivityForResult(intent, 2);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ContactAdapter", "onActivityResult");
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
