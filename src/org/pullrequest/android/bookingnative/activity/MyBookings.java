package org.pullrequest.android.bookingnative.activity;

import java.sql.SQLException;

import org.pullrequest.android.bookingnative.BookingPrefs_;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.UserDao;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.User;

import roboguice.util.Ln;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.DrawableRes;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.j256.ormlite.dao.ForeignCollection;

@EActivity(R.layout.my_bookings)
@OptionsMenu(R.menu.bookings)
public class MyBookings extends SearchableActivity {

	@Pref
	BookingPrefs_ prefs;

	@Inject
	private UserDao userDao;
	
	@ViewById(R.id.buttonHotels)
	Button hotelsButton;
	
	@DrawableRes(R.drawable.ic_book_hotel)
	Drawable newContentImg;
	
	public static final int LOGIN_ACTIVITY_CODE = 1;

	@AfterViews
	public void init() {
		newContentImg.setBounds(0, 0, newContentImg.getIntrinsicWidth(), newContentImg.getIntrinsicHeight());
		hotelsButton.setCompoundDrawables(newContentImg, null, null, null);
	}

	@Override
	protected void onResume() {
		if (prefs.loggedUserId().get() == -1L) {
			// show login activity
			startActivityForResult(new Intent(this, Login_.class), LOGIN_ACTIVITY_CODE);
		} else {
			displayBookings();
		}
		super.onResume();
	}

	@OptionsItem(R.id.menu_search)
	public void searchOption() {
		onSearchRequested();
	}

	@OptionsItem(R.id.menu_bench)
	public void benchmarkOption() {
		benchmark();
	}

	@OptionsItem(R.id.menu_logout)
	public void logout() {
		prefs.clear();
		MyBookings.this.finish();
		MyBookings.this.startActivity(new Intent(MyBookings.this, MyBookings_.class));
	}

	@Click(R.id.buttonHotels)
	public void buttonHotelsClick() {
		startActivity(new Intent(this, HotelsList_.class));
	}
	
	private void displayBookings() {
		ListView bookingList = (ListView) findViewById(R.id.bookingList);
		try {
			long userId = prefs.loggedUserId().get();
			User user = userDao.queryForId((int) userId);
			if (user != null) {
				ForeignCollection<Booking> bookings = user.getBookings();
				if (bookings != null) {
					bookingList.setAdapter(new BookingListAdapter(this, R.layout.booking_item, R.id.hotelName, bookings.toArray(new Booking[] {})));
				}
			} else {
				// show login activity
				startActivityForResult(new Intent(this, Login_.class), LOGIN_ACTIVITY_CODE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Background
	public void benchmark() {
		int nb = 100;
		long time = 0L;
		long start = System.currentTimeMillis();
		try {
			for(int i=0; i<nb; i++) {
				User user = new User(100 + i);
				user.setLogin("user" + i);
				userDao.create(user);
			}
			time = System.currentTimeMillis() - start;
			Ln.i("Time for " + nb + " insertions : " + time);
			displayToast("Time for " + nb + " insertions : " + time);
		} catch (SQLException e) {
			Ln.w(e);
		}
	}
	
	@UiThread
	public void displayToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
}