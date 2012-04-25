package org.pullrequest.android.bookingnative.activity;

import java.sql.SQLException;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.actionbar.ActionBarActivity;
import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class SearchHotels extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotels);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			
	        ListView hotelList = (ListView) findViewById(R.id.hotelList);
	        try {
				Cursor hotelCursor = getHelper().getHotelDao().search(query);
				startManagingCursor(hotelCursor);
				hotelList.setAdapter(new HotelListAdapter(this, R.layout.hotel_item, hotelCursor, new String[] { Hotels.NAME, Hotels.ADDRESS, Hotels.CITY, Hotels.ZIP }, new int[] { R.id.name, R.id.address, R.id.city, R.id.zip }));
			} catch (SQLException e) {
				Log.e(C.LOG_TAG, "Error during search for hotel " + query, e);
				Toast.makeText(this, "Search failed", Toast.LENGTH_SHORT);
			}
		}
	}
}