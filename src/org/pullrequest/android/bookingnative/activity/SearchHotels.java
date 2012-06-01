package org.pullrequest.android.bookingnative.activity;

import java.sql.SQLException;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.actionbar.ActionBarActivity;
import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;
import org.pullrequest.android.bookingnative.domain.service.HotelService;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;

@EActivity(R.layout.hotels)
public class SearchHotels extends ActionBarActivity {

	@Bean
	HotelService hotelService;

	@AfterViews
	@SuppressLint("NewApi")
	public void init() {
		handleIntent(getIntent());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	@OptionsItem(android.R.id.home)
	public void home() {
		this.finish();
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			ListView hotelList = (ListView) findViewById(R.id.hotelList);
			try {
				Cursor hotelCursor = hotelService.getDao().search(query);
				startManagingCursor(hotelCursor);
				hotelList.setAdapter(new HotelListAdapter(this, R.layout.hotel_item, hotelCursor, new String[] { Hotels.NAME, Hotels.ADDRESS, Hotels.CITY, Hotels.ZIP }, new int[] { R.id.name, R.id.address, R.id.city, R.id.zip }));
			} catch (SQLException e) {
				Log.e(C.LOG_TAG, "Error during search for hotel " + query, e);
				Toast.makeText(this, "Search failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
}