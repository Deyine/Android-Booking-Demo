package org.pullrequest.android.bookingnative.activity;

import java.sql.SQLException;

import org.pullrequest.android.bookingnative.PreferencesManager;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.UserDao;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.User;

import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.j256.ormlite.dao.ForeignCollection;

@ContentView(R.layout.my_bookings)
public class MyBookings extends SearchableActivity implements OnClickListener {

	private PreferencesManager preferencesManager = PreferencesManager.getInstance();

	@Inject
	private UserDao userDao;
	
	@InjectView(R.id.buttonHotels)
	private Button hotelsButton;
	
	@InjectResource(R.drawable.ic_book_hotel)
	private Drawable newContentImg;
	
	public static final int LOGIN_ACTIVITY_CODE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTitle("My bookings");
		super.onCreate(savedInstanceState);
		
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

		case R.id.menu_bench:
			new BenchmarkTask(this).execute();
			break;

		case R.id.menu_bench_2:
			break;

		case R.id.menu_logout:
			PreferencesManager.getInstance().removePref(MyBookings.this, PreferencesManager.PREF_LOGGED);
			MyBookings.this.finish();
			MyBookings.this.startActivity(new Intent(MyBookings.this, MyBookings.class));
			return true;
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
			User user = userDao.queryForId((int) userId);
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

	private class BenchmarkTask extends RoboAsyncTask<Long> {

		private int nb = 1000;
		
		protected BenchmarkTask(Context context) {
			super(context);
		}

		@Override
		public Long call() throws Exception {
			long time = 0L;
			long start = System.currentTimeMillis();
			try {
				for(int i=0; i<nb; i++) {
					User user = new User(100 + i);
					user.setLogin("user" + i);
					userDao.create(user);
				}
				time = System.currentTimeMillis() - start;
			} catch (SQLException e) {
				Ln.w(e);
			}
			return time;
		}

		@Override
		protected void onSuccess(Long time) {
			Toast.makeText(this.context, nb + " insert : " + time, Toast.LENGTH_LONG).show();
		}
	}
}