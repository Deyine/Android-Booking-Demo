package org.pullrequest.android.bookingnative.activity;

import java.net.URI;

import org.json.JSONArray;
import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.PreferencesManager;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;
import org.pullrequest.android.bookingnative.network.RequestService;
import org.pullrequest.android.bookingnative.network.Response;
import org.pullrequest.android.bookingnative.provider.DatabaseHelper;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class HotelsList extends SearchableActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotels);

		ListView hotelList = (ListView) findViewById(R.id.hotelList);
		Cursor hotelCursor = HotelDao.getInstance(this).getAll();
		startManagingCursor(hotelCursor);
		hotelList.setAdapter(new HotelListAdapter(this, R.layout.hotel_item, hotelCursor, new String[] { Hotels.NAME, Hotels.ADDRESS, Hotels.CITY, Hotels.ZIP }, new int[] { R.id.name, R.id.address, R.id.city, R.id.zip }));

		// get hotels list
		new GetHotelsTask().execute();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.hotels, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;

		case R.id.menu_refresh:
			new GetHotelsTask().execute();
			break;

		case R.id.menu_search:
			onSearchRequested();
			break;

		case R.id.menu_logout:
			PreferencesManager.getInstance().removePref(HotelsList.this, PreferencesManager.PREF_LOGGED);
			HotelsList.this.finish();
			HotelsList.this.startActivity(new Intent(HotelsList.this, HotelsList.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		new DatabaseHelper(this).close();
		super.onStop();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	/**
	 * 
	 * @param intent
	 */
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Toast.makeText(this, query, Toast.LENGTH_LONG).show();
		}
	}

	private class GetHotelsTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			Response hotelsResponse = null;
			try {
				hotelsResponse = RequestService.getInstance().get(new URI(C.SERVER_URL + "/api/hotels"));
				if (hotelsResponse.getCode() == 200) {
					JSONArray jsonHotels = new JSONArray(hotelsResponse.getJsonData());
					if (!HotelDao.getInstance(HotelsList.this).updateList(jsonHotels)) {
						return "ko";
					}
				}
			} catch (Exception e) {
				Log.e(C.LOG_TAG, "Problem during hotels update", e);
			}
			return ((hotelsResponse == null) || (hotelsResponse.getCode() != 200)) ? "ko" : "ok";
		}

		@Override
		protected void onPreExecute() {
			getActionBarHelper().setRefreshActionItemState(true);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String status) {
			getActionBarHelper().setRefreshActionItemState(false);
			if (status.equalsIgnoreCase("ko")) {
				Toast.makeText(HotelsList.this, "get hotels failed", Toast.LENGTH_LONG).show();
			}
		}
	}
}