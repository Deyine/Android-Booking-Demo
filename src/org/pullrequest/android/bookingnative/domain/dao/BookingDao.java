package org.pullrequest.android.bookingnative.domain.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Booking.Bookings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class BookingDao {

	private Context context;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private static BookingDao instance;
	private static String[] projection = new String[] { Bookings.ID, Bookings.USER_ID, Bookings.HOTEL_ID, Bookings.CHECKIN_DATE, Bookings.CHECKOUT_DATE, Bookings.CREDIT_CARD_NUMBER, Bookings.CREDIT_CARD_TYPE, Bookings.CREDIT_CARD_NAME,
			Bookings.CREDIT_CARD_EXPIRY_MONTH, Bookings.CREDIT_CARD_EXPIRY_YEAR, Bookings.SMOKING, Bookings.BEDS };

	public static BookingDao getInstance(Context context) {
		if (instance == null) {
			instance = new BookingDao(context);
		}
		return instance;
	}

	private BookingDao(Context context) {
		this.context = context;
	}

	public Uri add(Booking booking) {
		ContentValues values = new ContentValues();
		values.put(Bookings.USER_ID, booking.getUserId());
		values.put(Bookings.HOTEL_ID, booking.getHotelId());
		values.put(Bookings.CHECKIN_DATE, dateFormat.format(booking.getCheckinDate()));
		values.put(Bookings.CHECKOUT_DATE, dateFormat.format(booking.getCheckoutDate()));
		values.put(Bookings.CREDIT_CARD_NUMBER, booking.getCreditCardNumber());
		values.put(Bookings.CREDIT_CARD_TYPE, booking.getCreditCardType());
		values.put(Bookings.CREDIT_CARD_NAME, booking.getCreditCardName());
		values.put(Bookings.CREDIT_CARD_EXPIRY_MONTH, booking.getCreditCardExpiryMonth());
		values.put(Bookings.CREDIT_CARD_EXPIRY_YEAR, booking.getCreditCardExpiryYear());
		values.put(Bookings.SMOKING, booking.isSmoking());
		values.put(Bookings.BEDS, booking.getBeds());
		return context.getContentResolver().insert(Bookings.CONTENT_URI, values);
	}

	public void update(Booking booking) {
		ContentValues values = new ContentValues();
		values.put(Bookings.USER_ID, booking.getUserId());
		values.put(Bookings.HOTEL_ID, booking.getHotelId());
		values.put(Bookings.CHECKIN_DATE, dateFormat.format(booking.getCheckinDate()));
		values.put(Bookings.CHECKOUT_DATE, dateFormat.format(booking.getCheckoutDate()));
		values.put(Bookings.CREDIT_CARD_NUMBER, booking.getCreditCardNumber());
		values.put(Bookings.CREDIT_CARD_TYPE, booking.getCreditCardType());
		values.put(Bookings.CREDIT_CARD_NAME, booking.getCreditCardName());
		values.put(Bookings.CREDIT_CARD_EXPIRY_MONTH, booking.getCreditCardExpiryMonth());
		values.put(Bookings.CREDIT_CARD_EXPIRY_YEAR, booking.getCreditCardExpiryYear());
		values.put(Bookings.SMOKING, booking.isSmoking());
		values.put(Bookings.BEDS, booking.getBeds());
		context.getContentResolver().update(Bookings.CONTENT_URI, values, Bookings.ID + " = ?", new String[] { String.valueOf(booking.getId()) });
	}

	public Booking getById(long id) {
		Booking booking = null;
		Cursor cursor = context.getContentResolver().query(Bookings.CONTENT_URI, projection, Bookings.ID + " = ?", new String[] { String.valueOf(id) }, null);
		if (cursor.moveToFirst()) {
			booking = getFromCursor(cursor);
		}
		cursor.close();
		return booking;
	}

	public Cursor getAll() {
		return context.getContentResolver().query(Bookings.CONTENT_URI, projection, null, null, Bookings.CHECKIN_DATE + " asc");
	}

	public int removeAll() {
		return context.getContentResolver().delete(Bookings.CONTENT_URI, null, null);
	}

	public int delete(long id) {
		return context.getContentResolver().delete(Bookings.CONTENT_URI, Bookings.ID + " = " + String.valueOf(id), null);
	}

	public int count() {
		return this.getAll().getCount();
	}

	public static Booking getFromCursor(Cursor cursor) {
		Booking booking = new Booking();
		booking.setId(cursor.getLong(0));
		booking.setUserId(cursor.getInt(1));
		booking.setHotelId(cursor.getLong(2));
		try {
			booking.setCheckinDate(dateFormat.parse(cursor.getString(3)));
			booking.setCheckoutDate(dateFormat.parse(cursor.getString(4)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		booking.setCreditCardNumber(cursor.getString(5));
		booking.setCreditCardType(cursor.getString(6));
		booking.setCreditCardName(cursor.getString(7));
		booking.setCreditCardExpiryMonth(cursor.getInt(8));
		booking.setCreditCardExpiryYear(cursor.getInt(9));
		booking.setSmoking(cursor.getInt(10) == 1);
		booking.setBeds(cursor.getInt(11));
		return booking;
	}

	public Booking convertFromJson(JSONObject json) throws JSONException {
		Booking booking = new Booking();
		if (!json.isNull(Booking.Bookings.ID)) {
			booking.setId(json.getLong(Booking.Bookings.ID));
		}
		booking.setUserId(json.getInt(Booking.Bookings.USER_ID));
		booking.setHotelId(json.getLong(Booking.Bookings.HOTEL_ID));
		try {
			booking.setCheckinDate(dateFormat.parse(json.getString(Booking.Bookings.CHECKIN_DATE)));
			booking.setCheckoutDate(dateFormat.parse(json.getString(Booking.Bookings.CHECKOUT_DATE)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		booking.setCreditCardNumber(json.getString(Booking.Bookings.CREDIT_CARD_NUMBER));
		booking.setCreditCardType(json.getString(Booking.Bookings.CREDIT_CARD_TYPE));
		booking.setCreditCardName(json.getString(Booking.Bookings.CREDIT_CARD_NAME));
		booking.setCreditCardExpiryMonth(json.getInt(Booking.Bookings.CREDIT_CARD_EXPIRY_MONTH));
		booking.setCreditCardExpiryYear(json.getInt(Booking.Bookings.CREDIT_CARD_EXPIRY_YEAR));
		booking.setSmoking(json.getInt(Booking.Bookings.SMOKING) == 1);
		booking.setBeds(json.getInt(Booking.Bookings.BEDS));
		return booking;
	}
}