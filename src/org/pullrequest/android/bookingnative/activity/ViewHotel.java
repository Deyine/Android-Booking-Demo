package org.pullrequest.android.bookingnative.activity;

import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.actionbar.ActionBarActivity;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.model.Hotel;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewHotel extends ActionBarActivity {

	protected Hotel hotel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		long hotelId = getIntent().getLongExtra("hotelId", -1);

		if (hotelId < 0) {
			this.finish();
		} else {
			hotel = HotelDao.getInstance(this).getById(hotelId);
		}

		setContentView(R.layout.view_hotel);
		TextView name = (TextView) findViewById(R.id.name);
		name.setText(hotel.getName());
		TextView address = (TextView) findViewById(R.id.address);
		address.setText(hotel.getAddress());
		TextView city = (TextView) findViewById(R.id.city);
		city.setText(hotel.getCity());
		TextView state = (TextView) findViewById(R.id.state);
		state.setText(hotel.getState());
		TextView zip = (TextView) findViewById(R.id.zip);
		zip.setText(hotel.getZip());
		TextView country = (TextView) findViewById(R.id.country);
		country.setText(hotel.getCountry());
		TextView price = (TextView) findViewById(R.id.price);
		price.setText(String.valueOf(hotel.getPrice()));
	
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
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
			bookIntent.putExtra("hotelId", hotel.getId());
			startActivity(bookIntent);
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
