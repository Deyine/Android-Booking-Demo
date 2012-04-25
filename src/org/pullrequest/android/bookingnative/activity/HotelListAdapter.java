package org.pullrequest.android.bookingnative.activity;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.model.Hotel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RatingBar;
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
		Hotel hotel = HotelDao.getFromCursor(cursor);
		v.setTag(hotel);
		RatingBar stars = (RatingBar) v.findViewById(R.id.stars);
		stars.setNumStars(hotel.getStars());
		return v;
	}

	@Override
	public void onClick(View v) {
		Intent viewHotelIntent = new Intent(context, ViewHotel.class);
		viewHotelIntent.putExtra(C.EXTRA_HOTEL_KEY, (Hotel) v.getTag());
		context.startActivity(viewHotelIntent);
	}
}