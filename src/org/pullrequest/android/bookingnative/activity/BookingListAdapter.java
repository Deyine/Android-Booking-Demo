package org.pullrequest.android.bookingnative.activity;

import java.text.SimpleDateFormat;

import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.BookingDao;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Hotel;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class BookingListAdapter extends SimpleCursorAdapter implements OnClickListener {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private Context context;
	private Cursor cursor;
	
	public BookingListAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to) {
		super(context, layout, cursor, from, to);
		this.context = context;
		this.cursor = cursor;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		v.setOnClickListener(this);
		
		Booking booking = BookingDao.getFromCursor(cursor);
		
		// Hotel name
		Hotel hotel = HotelDao.getInstance(context).getById(booking.getHotelId());
		v.setTag(booking);
		TextView hotelName = (TextView) v.findViewById(R.id.hotelName);
		hotelName.setText(hotel.getName());
		
		// Dates
		TextView dates = (TextView) v.findViewById(R.id.checkin);
		dates.setText(dateFormat.format(booking.getCheckinDate()) + " to " + dateFormat.format(booking.getCheckoutDate()));
		return v;
	}

	@Override
	public void onClick(View v) {
		//Intent viewBookingIntent = new Intent(context, ViewHotel.class);
		//viewBookingIntent.putExtra(C.EXTRA_HOTEL_KEY, (Booking) v.getTag());
		//context.startActivity(viewBookingIntent);
	}
}