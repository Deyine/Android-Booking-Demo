package org.pullrequest.android.bookingnative.activity;

import java.net.URI;
import java.sql.SQLException;

import org.json.JSONArray;
import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;
import org.pullrequest.android.bookingnative.network.RequestService;
import org.pullrequest.android.bookingnative.network.Response;

import roboguice.inject.ContentView;
import roboguice.util.RoboAsyncTask;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.inject.Inject;

@ContentView(R.layout.hotels)
public class HotelsList extends SearchableActivity {

	@Inject
	private HotelDao hotelDao;
	
	private Cursor hotelCursor;
	private ListView hotelList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get hotels list
		hotelList = (ListView) findViewById(R.id.hotelList);
		hotelList.setEmptyView(findViewById(R.id.hotels_empty));
		hotelList.setAdapter(null);

		new GetHotelsTask(this).execute();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.hotels, menu);
		boolean result = super.onCreateOptionsMenu(menu);

		// update hotels list
		new GetRemoteHotelsTask(this).execute();
		
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;

		case R.id.menu_refresh:
			new GetRemoteHotelsTask(this).execute();
			break;

		case R.id.menu_search:
			onSearchRequested();
			break;
		}
		return super.onOptionsItemSelected(item);
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

	private class GetRemoteHotelsTask extends RoboAsyncTask<String> {

		protected GetRemoteHotelsTask(Context context) {
			super(context);
		}

		@Override
		public String call() throws Exception {
			Response hotelsResponse = null;
			try {
				hotelsResponse = RequestService.getInstance().get(new URI(C.SERVER_URL + "/api/hotels"));
				if (hotelsResponse.getCode() == 200) {
					JSONArray jsonHotels = new JSONArray(hotelsResponse.getJsonData());
					if (!hotelDao.updateList(jsonHotels)) {
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
		}

		@Override
		protected void onSuccess(String status) {
			getActionBarHelper().setRefreshActionItemState(false);
			if (status.equalsIgnoreCase("ko")) {
				Toast.makeText(HotelsList.this, "get hotels failed", Toast.LENGTH_LONG).show();
			}
			else {
				hotelCursor.requery();
			}
		}
	}

	private class GetHotelsTask extends RoboAsyncTask<Boolean> {

		protected GetHotelsTask(Context context) {
			super(context);
		}

		@Override
		public Boolean call() throws Exception {
			try {
				hotelCursor = hotelDao.findAll();
				startManagingCursor(hotelCursor);
			} catch (SQLException e) {
				Log.e(C.LOG_TAG, "Problem during hotel list retrieval", e);
				return false;
			}
			return true;
		}
		
		@Override
		protected void onSuccess(Boolean status) {
			hotelList.setAdapter(new HotelListAdapter(HotelsList.this, R.layout.hotel_item, hotelCursor, new String[] { Hotels.NAME, Hotels.ADDRESS, Hotels.CITY, Hotels.ZIP }, new int[] { R.id.name, R.id.address, R.id.city, R.id.zip }));
		}
	}
}