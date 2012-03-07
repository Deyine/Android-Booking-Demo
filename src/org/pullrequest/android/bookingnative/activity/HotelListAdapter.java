package org.pullrequest.android.bookingnative.activity;

import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class HotelListAdapter extends SimpleCursorAdapter implements OnClickListener {

	private Context context;
	private Cursor cursor;
	
	public HotelListAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to) {
		super(context, layout, cursor, from, to);
		this.context = context;
		this.cursor = cursor;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		v.setOnClickListener(this);
		v.setTag(cursor.getLong(cursor.getColumnIndex(Hotels.ID)));
		return v;
	}

	@Override
	public void onClick(View v) {
		Intent viewHotelIntent = new Intent(context, ViewHotel.class);
		viewHotelIntent.putExtra("hotelId", (Long) v.getTag());
		context.startActivity(viewHotelIntent);
	}
}