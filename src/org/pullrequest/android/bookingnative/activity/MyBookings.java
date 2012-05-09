package org.pullrequest.android.bookingnative.activity;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.PreferencesManager;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.pullrequest.android.bookingnative.domain.model.User;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.table.TableUtils;

public class MyBookings extends SearchableActivity implements OnClickListener {

	private PreferencesManager preferencesManager = PreferencesManager.getInstance();

	public static final int LOGIN_ACTIVITY_CODE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_bookings);
		setTitle("My bookings");

		Button hotelsButton = (Button) findViewById(R.id.buttonHotels);
		Drawable newContentImg = getResources().getDrawable(R.drawable.ic_book_hotel);
		newContentImg.setBounds(0, 0, newContentImg.getIntrinsicWidth(), newContentImg.getIntrinsicHeight());
		hotelsButton.setCompoundDrawables(newContentImg, null, null, null);
		hotelsButton.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		if (preferencesManager.getLongPref(this, PreferencesManager.PREF_LOGGED) == -1L) {
			// show login activity
			startActivityForResult(new Intent(this, Login.class), LOGIN_ACTIVITY_CODE);
		} else {
			displayBookings();
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.bookings, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			onSearchRequested();
			break;

		case R.id.menu_logout:
			PreferencesManager.getInstance().removePref(MyBookings.this, PreferencesManager.PREF_LOGGED);
			MyBookings.this.finish();
			MyBookings.this.startActivity(new Intent(MyBookings.this, MyBookings.class));
			return true;

		case R.id.menu_bench:
			new BenchmarkTask().execute("0");
			break;

		case R.id.menu_bench_2:
			new BenchmarkTask().execute("1");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonHotels) {
			startActivity(new Intent(this, HotelsList.class));
		}
	}

	private void displayBookings() {
		ListView bookingList = (ListView) findViewById(R.id.bookingList);
		try {
			long userId = preferencesManager.getLongPref(this, PreferencesManager.PREF_LOGGED);
			User user = getHelper().getUserDao().queryForId((int) userId);
			if (user != null) {
				ForeignCollection<Booking> bookings = user.getBookings();
				if (bookings != null) {
					bookingList.setAdapter(new BookingListAdapter(this, R.layout.booking_item, R.id.hotelName, bookings.toArray(new Booking[] {})));
				}
			} else {
				// show login activity
				startActivityForResult(new Intent(this, Login.class), LOGIN_ACTIVITY_CODE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private class BenchmarkTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			final int nbHotels = 1000;

			if (params[0].equals("0")) {
				long start = System.currentTimeMillis();
				final HotelDao hotelDao = getHelper().getHotelDao();
				Log.d(C.BENCH_TAG, "get dao : " + (System.currentTimeMillis() - start));
				try {
					TableUtils.clearTable(getHelper().getConnectionSource(), Hotel.class);
					start = System.currentTimeMillis();
					for (int i = 0; i < nbHotels; i++) {
						Hotel hotel = new Hotel(i);
						hotel.setName("hotel " + i);
						hotelDao.create(hotel);
					}
					Log.d(C.BENCH_TAG, nbHotels + " hotels insertion : " + (System.currentTimeMillis() - start));
					Log.d(C.BENCH_TAG, "hotel insertion moy : " + ((System.currentTimeMillis() - start) / nbHotels));
				} catch (SQLException e) {
					Log.d(C.BENCH_TAG, "", e);
				}
			} else if (params[0].equals("1")) {
				long start = System.currentTimeMillis();
				final HotelDao hotelDao = getHelper().getHotelDao();
				Log.d(C.BENCH_TAG, "get dao : " + (System.currentTimeMillis() - start));
				try {
					TableUtils.clearTable(getHelper().getConnectionSource(), Hotel.class);
					start = System.currentTimeMillis();
					TransactionManager.callInTransaction(getHelper().getConnectionSource(), new Callable<Void>() {
						public Void call() throws Exception {
							for (int i = 0; i < nbHotels; i++) {
								Hotel hotel = new Hotel(i);
								hotel.setName("hotel " + i);
								hotelDao.create(hotel);
							}
							return null;
						}

					});
					Log.d(C.BENCH_TAG, nbHotels + " hotels insertion : " + (System.currentTimeMillis() - start));
					Log.d(C.BENCH_TAG, "hotel insertion moy : " + ((System.currentTimeMillis() - start) / nbHotels));
				} catch (SQLException e) {
					Log.d(C.BENCH_TAG, "", e);
				}
			}
			return true;

		}

		@Override
		protected void onPostExecute(Boolean status) {

		}
	}

}