package org.pullrequest.android.bookingnative.activity;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.actionbar.ActionBarActivity;
import org.pullrequest.android.bookingnative.domain.model.Hotel;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.view_hotel)
@OptionsMenu(R.menu.hotel)
public class ViewHotel extends ActionBarActivity {

	@ViewById(R.id.name)
	TextView name;
	
	@ViewById(R.id.address)
	TextView address;
	
	@ViewById(R.id.city)
	TextView city;
	
	@ViewById(R.id.state)
	TextView state;
	
	@ViewById(R.id.zip)
	TextView zip;
	
	@ViewById(R.id.country)
	TextView country;
	
	@ViewById(R.id.price)
	TextView price;
	
	@NonConfigurationInstance
	@Extra(C.EXTRA_HOTEL_KEY)
	Hotel hotel;

	@AfterViews
	@SuppressLint("NewApi")
	protected void init() {
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

	@OptionsItem(android.R.id.home)
	public void home() {
		this.finish();
	}
	
	@OptionsItem(R.id.menu_book)
	public void bookHotel() {
		Intent bookIntent = new Intent(this, BookHotel_.class);
		bookIntent.putExtra(C.EXTRA_HOTEL_KEY, hotel);
		startActivity(bookIntent);
		//this.finish();
	}
}
