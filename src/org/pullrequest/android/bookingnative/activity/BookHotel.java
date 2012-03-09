package org.pullrequest.android.bookingnative.activity;

import java.util.Calendar;

import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.actionbar.ActionBarActivity;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.model.Hotel;

import android.app.ActionBar;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.googlecode.android.widgets.DateSlider.DateSlider;
import com.googlecode.android.widgets.DateSlider.DateSlider.OnDateSetListener;
import com.googlecode.android.widgets.DateSlider.DefaultDateSlider;
import com.googlecode.android.widgets.DateSlider.MonthYearDateSlider;

public class BookHotel extends ActionBarActivity implements OnClickListener {

	protected Hotel hotel;

	private static final int CHECK_IN_DATE_DIALOG_ID = 0;
	private static final int CHECK_OUT_DATE_DIALOG_ID = 1;
	private static final int EXPIRY_DATE_DIALOG_ID = 2;

	private Calendar today;
	private EditText checkin;
	private EditText checkout;
	private EditText expiryDate;

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

		expiryDate = (EditText) findViewById(R.id.creditCardExpiryDate);
		expiryDate.setInputType(InputType.TYPE_NULL);
		expiryDate.setOnClickListener(this);
		
		// get the current date
		today = Calendar.getInstance();

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.checkin) {
			showDialog(CHECK_IN_DATE_DIALOG_ID);
		} else if (v.getId() == R.id.checkout) {
			showDialog(CHECK_OUT_DATE_DIALOG_ID);
		} else if (v.getId() == R.id.creditCardExpiryDate) {
			showDialog(EXPIRY_DATE_DIALOG_ID);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CHECK_IN_DATE_DIALOG_ID:
			return new DefaultDateSlider(this, new OnDateSetListener() {
				@Override
				public void onDateSet(DateSlider view, Calendar selectedDate) {
					checkin.setText((selectedDate.get(Calendar.MONTH) + 1) + "/" + selectedDate.get(Calendar.DAY_OF_MONTH) + "/" + selectedDate.get(Calendar.YEAR));
				}
			}, today);
		case CHECK_OUT_DATE_DIALOG_ID:
			return new DefaultDateSlider(this, new OnDateSetListener() {
				@Override
				public void onDateSet(DateSlider view, Calendar selectedDate) {
					checkout.setText((selectedDate.get(Calendar.MONTH) + 1) + "/" + selectedDate.get(Calendar.DAY_OF_MONTH) + "/" + selectedDate.get(Calendar.YEAR));
				}
			}, today);
		case EXPIRY_DATE_DIALOG_ID:
			return new MonthYearDateSlider(this, new OnDateSetListener() {
				@Override
				public void onDateSet(DateSlider view, Calendar selectedDate) {
					expiryDate.setText((selectedDate.get(Calendar.MONTH) + 1) + "/" + selectedDate.get(Calendar.YEAR));
				}
			}, today);
		}
		return null;
	}
}
