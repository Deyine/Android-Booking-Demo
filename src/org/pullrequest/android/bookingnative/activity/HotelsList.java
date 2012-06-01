package org.pullrequest.android.bookingnative.activity;

import java.sql.SQLException;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;
import org.pullrequest.android.bookingnative.domain.service.HotelService;
import org.pullrequest.android.bookingnative.network.BookingRestApi;
import org.pullrequest.android.bookingnative.network.HotelList;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.rest.RestService;

@EActivity(R.layout.hotels)
@OptionsMenu(R.menu.hotels)
public class HotelsList extends SearchableActivity {

	@Bean
	HotelService hotelService;

	private Cursor hotelCursor;

	@ViewById
	ListView hotelList;

	@RestService
	BookingRestApi bookingRestApi;
	
	@AfterViews
	@SuppressLint("NewApi")
	public void init() {
		// add GsonMessageConverter to resttemplate
		RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
		bookingRestApi.setRestTemplate(restTemplate);

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
		try {
			HotelList hotels = bookingRestApi.getHotels();
			hotelService.getDao().updateList(hotels);
			status = "ok";
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
			hotelCursor = hotelService.getDao().findAll();
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