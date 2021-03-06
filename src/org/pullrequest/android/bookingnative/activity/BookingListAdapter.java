package org.pullrequest.android.bookingnative.activity;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.DatabaseHelper;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Hotel;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class BookingListAdapter extends ArrayAdapter<Booking> implements OnClickListener {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private Context context;
	private Booking[] bookings;

	private DatabaseHelper databaseHelper = null;

	public BookingListAdapter(Context context, int resource, int textViewResourceId, Booking[] bookings) {
		super(context, resource, textViewResourceId, bookings);
		this.context = context;
		this.bookings = bookings;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		v.setOnClickListener(this);

		Booking booking = bookings[position];
		v.setTag(booking);

		// Hotel name
		OpenHelperManager.getHelper(this.context, DatabaseHelper.class);
		try {
			Hotel hotel = booking.getHotel();
			getHelper().getHotelDao().refresh(hotel);
			TextView hotelName = (TextView) v.findViewById(R.id.hotelName);
			hotelName.setText(hotel.getName());
		} catch (SQLException e) {
			Log.w(C.LOG_TAG, "Problem during hotel name querying", e);
		}

		// Dates
		TextView dates = (TextView) v.findViewById(R.id.checkin);
		dates.setText(dateFormat.format(booking.getCheckinDate()) + " to " + dateFormat.format(booking.getCheckoutDate()));
		return v;
	}

	@Override
	public void onClick(View v) {
		// Intent viewBookingIntent = new Intent(context, ViewHotel.class);
		// viewBookingIntent.putExtra(C.EXTRA_HOTEL_KEY, (Booking) v.getTag());
		// context.startActivity(viewBookingIntent);
	}

	private DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this.context, DatabaseHelper.class);
		}
		return databaseHelper;
	}
}