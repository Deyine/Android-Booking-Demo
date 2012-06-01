package org.pullrequest.android.bookingnative.activity;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.pullrequest.android.bookingnative.BookingPrefs_;
import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.actionbar.ActionBarActivity;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.pullrequest.android.bookingnative.domain.model.User;
import org.pullrequest.android.bookingnative.domain.service.BookingService;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.googlecode.android.widgets.DateSlider.DateSlider;
import com.googlecode.android.widgets.DateSlider.DateSlider.OnDateSetListener;
import com.googlecode.android.widgets.DateSlider.DefaultDateSlider;
import com.googlecode.android.widgets.DateSlider.MonthYearDateSlider;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.book_hotel)
public class BookHotel extends ActionBarActivity {

	@Pref
	BookingPrefs_ prefs;

	@Bean
	BookingService bookingService;

	@NonConfigurationInstance
	@Extra(C.EXTRA_HOTEL_KEY)
	Hotel hotel;

	private static final int CHECK_IN_DATE_DIALOG_ID = 0;
	private static final int CHECK_OUT_DATE_DIALOG_ID = 1;
	private static final int EXPIRY_DATE_DIALOG_ID = 2;

	private Calendar today;

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

	@ViewById(R.id.checkin)
	EditText checkin;

	@ViewById(R.id.checkout)
	EditText checkout;

	@ViewById(R.id.creditCard)
	EditText creditCardNumber;

	@ViewById(R.id.creditCardName)
	EditText creditCardName;

	@ViewById(R.id.creditCardExpiryDate)
	EditText expiryDate;

	@ViewById(R.id.roomPref)
	Spinner roomPref;

	@ViewById(R.id.smokingPref)
	Spinner smokingPref;

	@ViewById(R.id.creditCardType)
	Spinner creditCardTypePref;

	@ViewById(R.id.cancelButton)
	Button cancelButton;

	@ViewById(R.id.proceedButton)
	Button proceedButton;

	private Booking currentBooking = new Booking();

	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private SimpleDateFormat monthYearFormat = new SimpleDateFormat("MM/yyyy");

	@AfterViews
	@SuppressLint("NewApi")
	protected void init() {
		currentBooking.setHotel(hotel);

		long userId = prefs.loggedUserId().get();
		currentBooking.setUser(new User(userId));

		// get the current date
		today = Calendar.getInstance();

		name.setText(hotel.getName());
		address.setText(hotel.getAddress());
		city.setText(hotel.getCity());
		state.setText(hotel.getState());
		zip.setText(hotel.getZip());
		country.setText(hotel.getCountry());
		price.setText(String.valueOf(hotel.getPrice()));

		checkin.setInputType(InputType.TYPE_NULL);
		checkin.setText(dateFormat.format(today.getTime()));
		checkout.setInputType(InputType.TYPE_NULL);
		checkout.setText(dateFormat.format(today.getTime()));

		ArrayAdapter<CharSequence> roomPrefAdapter = ArrayAdapter.createFromResource(this, R.array.room_preferences, android.R.layout.simple_spinner_item);
		roomPrefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		roomPref.setAdapter(roomPrefAdapter);

		ArrayAdapter<CharSequence> smokingPrefAdapter = ArrayAdapter.createFromResource(this, R.array.smoking_preferences, android.R.layout.simple_spinner_item);
		smokingPrefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		smokingPref.setAdapter(smokingPrefAdapter);

		ArrayAdapter<CharSequence> creditCardTypeAdapter = ArrayAdapter.createFromResource(this, R.array.credit_card_preferences, android.R.layout.simple_spinner_item);
		creditCardTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		creditCardTypePref.setAdapter(creditCardTypeAdapter);

		expiryDate.setInputType(InputType.TYPE_NULL);
		expiryDate.setText(monthYearFormat.format(today.getTime()));

		// set action bar navigation on
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Click(R.id.checkin)
	public void checkinClick() {
		checkin.setError(null);
		showDialog(CHECK_IN_DATE_DIALOG_ID);
	}

	@Click(R.id.checkout)
	public void checkoutClick() {
		checkout.setError(null);
		showDialog(CHECK_OUT_DATE_DIALOG_ID);
	}

	@Click(R.id.creditCardExpiryDate)
	public void creditCardExpiryDateClick() {
		expiryDate.setError(null);
		showDialog(EXPIRY_DATE_DIALOG_ID);
	}

	@Click(R.id.cancelButton)
	public void cancelButtonClick() {
		this.finish();
	}

	@Click(R.id.proceedButton)
	public void proceedButtonClick() {
		if (validateBooking()) {
			try {
				bookingService.getDao().create(currentBooking);
			} catch (SQLException e) {
				Log.e(C.LOG_TAG, "Problem during hotel booking", e);
			}
			this.finish();
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

	@OptionsItem(android.R.id.home)
	public void home() {
		this.finish();
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

	@UiThread
	public void clearErrors() {
		checkin.setError(null);
		checkout.setError(null);
		creditCardNumber.setError(null);
		creditCardName.setError(null);
	}

	@UiThread
	public void displayTextError(EditText edit, String message) {
		edit.setError(message);
	}
}
