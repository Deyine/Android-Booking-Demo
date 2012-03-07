package org.pullrequest.android.bookingnative.activity;

import java.util.Calendar;

import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.actionbar.ActionBarActivity;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.model.Hotel;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class BookHotel extends ActionBarActivity implements OnClickListener {

	protected Hotel hotel;

	private static final int CHECK_IN_DATE_DIALOG_ID = 0;
	private static final int CHECK_OUT_DATE_DIALOG_ID = 1;

	private int year;
	private int month;
	private int day;
	private EditText checkin;
	private EditText checkout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		long hotelId = getIntent().getLongExtra("hotelId", -1);

		if (hotelId < 0) {
			this.finish();
		} else {
			hotel = HotelDao.getInstance(this).getById(hotelId);
		}

		setContentView(R.layout.book_hotel);
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

		checkin = (EditText) findViewById(R.id.checkin);
		checkin.setInputType(InputType.TYPE_NULL);
		checkin.setOnClickListener(this);
		checkout = (EditText) findViewById(R.id.checkout);
		checkout.setInputType(InputType.TYPE_NULL);
		checkout.setOnClickListener(this);

		Spinner roomPref = (Spinner) findViewById(R.id.roomPref);
		ArrayAdapter<CharSequence> roomPrefAdapter = ArrayAdapter.createFromResource(this, R.array.room_preferences, android.R.layout.simple_spinner_item);
		roomPrefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		roomPref.setAdapter(roomPrefAdapter);

		Spinner smokingPref = (Spinner) findViewById(R.id.smokingPref);
		ArrayAdapter<CharSequence> smokingPrefAdapter = ArrayAdapter.createFromResource(this, R.array.smoking_preferences, android.R.layout.simple_spinner_item);
		smokingPrefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		smokingPref.setAdapter(smokingPrefAdapter);

		Spinner creditCardTypePref = (Spinner) findViewById(R.id.creditCardType);
		ArrayAdapter<CharSequence> creditCardTypeAdapter = ArrayAdapter.createFromResource(this, R.array.credit_card_preferences, android.R.layout.simple_spinner_item);
		creditCardTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		creditCardTypePref.setAdapter(creditCardTypeAdapter);

		// get the current date
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.checkin) {
			showDialog(CHECK_IN_DATE_DIALOG_ID);
		} else if (v.getId() == R.id.checkout) {
			showDialog(CHECK_OUT_DATE_DIALOG_ID);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CHECK_IN_DATE_DIALOG_ID:
			return new DatePickerDialog(this, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					checkin.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
				}
			}, year, month, day);
		case CHECK_OUT_DATE_DIALOG_ID:
			return new DatePickerDialog(this, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					checkout.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
				}
			}, year, month, day);
		}
		return null;
	}
}
