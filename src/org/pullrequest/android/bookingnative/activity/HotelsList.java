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

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.hotels)
@OptionsMenu(R.menu.hotels)
public class HotelsList extends SearchableActivity {

	@Inject
	private HotelDao hotelDao;

	private Cursor hotelCursor;

	@ViewById
	ListView hotelList;

	@AfterViews
	@SuppressLint("NewApi")
	public void init() {
		// get hotels list
		hotelList.setEmptyView(findViewById(R.id.hotels_empty));
		hotelList.setAdapter(null);
		getHotels();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getRemoteHotels();
		return super.onCreateOptionsMenu(menu);
	}

	@OptionsItem(android.R.id.home)
	public void home() {
		this.finish();
	}
	
	@OptionsItem(R.id.menu_search)
	public void searchOption() {
		onSearchRequested();
	}

	@OptionsItem(R.id.menu_refresh)
	public void refresh() {
		getRemoteHotels();
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

	@Background
	public void getRemoteHotels() {
		setRefreshItemState(true);
		String status = "ko";
		Response hotelsResponse = null;
		try {
			hotelsResponse = RequestService.getInstance().get(new URI(C.SERVER_URL + "/api/hotels"));
			if ((hotelsResponse != null) && (hotelsResponse.getCode() == 200)) {
				JSONArray jsonHotels = new JSONArray(hotelsResponse.getJsonData());
				if (!hotelDao.updateList(jsonHotels)) {
					status = "ko";
				} else {
					status = "ok";
				}
			}
		} catch (Exception e) {
			Log.e(C.LOG_TAG, "Problem during hotels update", e);
		}
		getRemoteHotelsResponse(status);
	}

	@UiThread
	public void getRemoteHotelsResponse(String status) {
		setRefreshItemState(false);
		if (status.equalsIgnoreCase("ko")) {
			Toast.makeText(HotelsList.this, "get hotels failed", Toast.LENGTH_LONG).show();
		} else {
			hotelCursor.requery();
		}
	}

	@UiThread
	public void setRefreshItemState(boolean state) {
		getActionBarHelper().setRefreshActionItemState(state);
	}

	@Background
	public void getHotels() {
		try {
			hotelCursor = hotelDao.findAll();
			startManagingCursor(hotelCursor);
		} catch (SQLException e) {
			Log.e(C.LOG_TAG, "Problem during hotel list retrieval", e);
		}
		refreshHotelsList();
	}

	@UiThread
	protected void refreshHotelsList() {
		hotelList.setAdapter(new HotelListAdapter(HotelsList.this, R.layout.hotel_item, hotelCursor, new String[] { Hotels.NAME, Hotels.ADDRESS, Hotels.CITY, Hotels.ZIP }, new int[] { R.id.name, R.id.address, R.id.city, R.id.zip }));
	}
}