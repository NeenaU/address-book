package com.example.addressbook;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private OnItemClickListener mListener;

    public ContactAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView addressText;
        private TextView numberText;
        private TextView emailText;

        public ContactViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.textview_name);
            addressText = itemView.findViewById(R.id.textview_address);
            numberText = itemView.findViewById(R.id.textview_phone_number);
            emailText = itemView.findViewById(R.id.textview_email);
        }

        itemView.setOnClickListener(new View.OnClickListener()){

        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
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
