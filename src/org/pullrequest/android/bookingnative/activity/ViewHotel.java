package org.pullrequest.android.bookingnative.activity;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.actionbar.ActionBarActivity;
import org.pullrequest.android.bookingnative.domain.model.Hotel;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

@ContentView(R.layout.view_hotel)
public class ViewHotel extends ActionBarActivity {

	protected Hotel hotel;

	@InjectView(R.id.name)
	private TextView name;
	
	@InjectView(R.id.address)
	private TextView address;
	
	@InjectView(R.id.city)
	private TextView city;
	
	@InjectView(R.id.state)
	private TextView state;
	
	@InjectView(R.id.zip)
	private TextView zip;
	
	@InjectView(R.id.country)
	private TextView country;
	
	@InjectView(R.id.price)
	private TextView price;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hotel = (Hotel) getIntent().getExtras().get(C.EXTRA_HOTEL_KEY);

		name.setText(hotel.getName());
		address.setText(hotel.getAddress());
		city.setText(hotel.getCity());
		state.setText(hotel.getState());
		zip.setText(hotel.getZip());
		country.setText(hotel.getCountry());
		price.setText(String.valueOf(hotel.getPrice()));

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.hotel, menu);
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		case R.id.menu_book:
			Intent bookIntent = new Intent(this, BookHotel.class);
			bookIntent.putExtra(C.EXTRA_HOTEL_KEY, hotel);
			startActivity(bookIntent);
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
