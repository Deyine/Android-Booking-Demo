package org.pullrequest.android.bookingnative.activity;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.PreferencesManager;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.actionbar.ActionBarActivity;
import org.pullrequest.android.bookingnative.domain.dao.BookingDao;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.pullrequest.android.bookingnative.domain.model.User;

import android.app.ActionBar;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.inject.Inject;
import com.googlecode.android.widgets.DateSlider.DateSlider;
import com.googlecode.android.widgets.DateSlider.DateSlider.OnDateSetListener;
import com.googlecode.android.widgets.DateSlider.DefaultDateSlider;
import com.googlecode.android.widgets.DateSlider.MonthYearDateSlider;

public class BookHotel extends ActionBarActivity implements OnClickListener {

	private PreferencesManager preferencesManager = PreferencesManager.getInstance();

	@Inject
	private BookingDao bookingDao;
	
	protected Hotel hotel;

	private static final int CHECK_IN_DATE_DIALOG_ID = 0;
	private static final int CHECK_OUT_DATE_DIALOG_ID = 1;
	private static final int EXPIRY_DATE_DIALOG_ID = 2;

	private Calendar today;
	private EditText checkin;
	private EditText checkout;
	private EditText creditCardNumber;
	private EditText creditCardName;
	private EditText expiryDate;

	private Booking currentBooking = new Booking();

	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private SimpleDateFormat monthYearFormat = new SimpleDateFormat("MM/yyyy");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		hotel = (Hotel) getIntent().getExtras().get(C.EXTRA_HOTEL_KEY);
		currentBooking.setHotel(hotel);
		long userId = preferencesManager.getLongPref(this, PreferencesManager.PREF_LOGGED);
		currentBooking.setUser(new User(userId));

		// get the current date
		today = Calendar.getInstance();

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
		checkin.setText(dateFormat.format(today.getTime()));
		checkout = (EditText) findViewById(R.id.checkout);
		checkout.setInputType(InputType.TYPE_NULL);
		checkout.setOnClickListener(this);
		checkout.setText(dateFormat.format(today.getTime()));

		creditCardNumber = (EditText) findViewById(R.id.creditCard);
		creditCardName = (EditText) findViewById(R.id.creditCardName);

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
		expiryDate.setText(monthYearFormat.format(today.getTime()));

		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(this);
		Button proceedButton = (Button) findViewById(R.id.proceedButton);
		proceedButton.setOnClickListener(this);

		// set action bar navigation on
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.checkin) {
			checkin.setError(null);
			showDialog(CHECK_IN_DATE_DIALOG_ID);
		} else if (v.getId() == R.id.checkout) {
			checkout.setError(null);
			showDialog(CHECK_OUT_DATE_DIALOG_ID);
		} else if (v.getId() == R.id.creditCardExpiryDate) {
			expiryDate.setError(null);
			showDialog(EXPIRY_DATE_DIALOG_ID);
		} else if (v.getId() == R.id.cancelButton) {
			this.finish();
		} else if (v.getId() == R.id.proceedButton) {
			if (validateBooking()) {
				try {
					bookingDao.create(currentBooking);
				} catch (SQLException e) {
					Log.e(C.LOG_TAG, "Problem during hotel booking", e);
				}
				this.finish();
			}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
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

	/**
	 * 
	 * @return
	 */
	private boolean validateBooking() {
		boolean valid = true;
		clearErrors();

		// checkin date
		Date checkinDate = null;
		try {
			checkinDate = dateFormat.parse(checkin.getText().toString());
			if (!checkinDate.after(new Date())) {
				displayTextError(checkin, "Please enter a date in the future");
				valid = false;
			} else {
				currentBooking.setCheckinDate(checkinDate);
			}
		} catch (ParseException e) {
			Log.e(C.LOG_TAG, "", e);
			displayTextError(checkin, "Please enter a correct date");
			valid = false;
		}

		// checkout date
		try {
			Date checkoutDate = dateFormat.parse(checkout.getText().toString());
			if (!checkoutDate.after(new Date())) {
				displayTextError(checkout, "Please enter a date in the future");
				valid = false;
			} else if (!checkoutDate.after(checkinDate)) {
				displayTextError(checkout, "Please enter a check out date after the checkin date");
				valid = false;
			} else {
				currentBooking.setCheckoutDate(checkoutDate);
			}
		} catch (ParseException e) {
			Log.e(C.LOG_TAG, "", e);
			displayTextError(checkout, "Please enter a correct date");
			valid = false;
		}

		// credit card number
		if (creditCardNumber.getText().toString().length() < 12) {
			displayTextError(creditCardNumber, "Please enter a valid credit card number");
			valid = false;
		} else {
			currentBooking.setCreditCardNumber(creditCardNumber.getText().toString());
		}

		// credit card name
		if (creditCardName.getText().toString().length() == 0) {
			displayTextError(creditCardName, "Please enter a credit card name");
			valid = false;
		} else {
			currentBooking.setCreditCardName(creditCardName.getText().toString());
		}

		// credit card expiry
		try {
			Date creditCardExpiryDate = monthYearFormat.parse(expiryDate.getText().toString());
			if (!creditCardExpiryDate.after(new Date())) {
				displayTextError(expiryDate, "Please enter a valid expiry date");
				valid = false;
			} else {
				Calendar expiryCal = Calendar.getInstance();
				expiryCal.setTime(creditCardExpiryDate);
				currentBooking.setCreditCardExpiryMonth(expiryCal.get(Calendar.MONTH));
				currentBooking.setCreditCardExpiryYear(expiryCal.get(Calendar.YEAR));
			}
		} catch (ParseException e) {
			Log.e(C.LOG_TAG, "", e);
			displayTextError(expiryDate, "Please enter a valid expiry date");
			valid = false;
		}

		return valid;
	}

	private void clearErrors() {
		checkin.setError(null);
		checkout.setError(null);
		creditCardNumber.setError(null);
		creditCardName.setError(null);
	}

	private void displayTextError(EditText edit, String message) {
		edit.setError(message);
	}
}
