package org.pullrequest.android.bookingnative.provider;

import java.util.HashMap;

import org.pullrequest.android.bookingnative.domain.DatabaseHelper;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Booking.Bookings;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;
import org.pullrequest.android.bookingnative.domain.model.User;
import org.pullrequest.android.bookingnative.domain.model.User.Users;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DataProvider extends ContentProvider {

	private static final int USERS = 1;
	private static final int HOTELS = 2;
	private static final int BOOKINGS = 3;

	public static final String AUTHORITY = "org.pullrequest.android.bookingnative.provider.dataprovider";

	private static final UriMatcher sUriMatcher;

	private DatabaseHelper databaseHelper;
	private static HashMap<String, String> usersProjectionMap;
	private static HashMap<String, String> hotelsProjectionMap;
	private static HashMap<String, String> bookingsProjectionMap;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		sUriMatcher.addURI(AUTHORITY, User.TABLE_NAME, USERS);
		usersProjectionMap = new HashMap<String, String>();
		usersProjectionMap.put(Users.ID, Users.ID);
		usersProjectionMap.put(Users.FIRST_NAME, Users.FIRST_NAME);
		usersProjectionMap.put(Users.LAST_NAME, Users.LAST_NAME);
		usersProjectionMap.put(Users.LOGIN, Users.LOGIN);
		usersProjectionMap.put(Users.PASSWORD, Users.PASSWORD);

		sUriMatcher.addURI(AUTHORITY, Hotel.TABLE_NAME, HOTELS);
		hotelsProjectionMap = new HashMap<String, String>();
		hotelsProjectionMap.put(Hotels.ID, Hotels.ID);
		hotelsProjectionMap.put(Hotels.NAME, Hotels.NAME);
		hotelsProjectionMap.put(Hotels.ADDRESS, Hotels.ADDRESS);
		hotelsProjectionMap.put(Hotels.CITY, Hotels.CITY);
		hotelsProjectionMap.put(Hotels.ZIP, Hotels.ZIP);
		hotelsProjectionMap.put(Hotels.STATE, Hotels.STATE);
		hotelsProjectionMap.put(Hotels.COUNTRY, Hotels.COUNTRY);
		hotelsProjectionMap.put(Hotels.STARS, Hotels.STARS);
		hotelsProjectionMap.put(Hotels.PRICE, Hotels.PRICE);

		sUriMatcher.addURI(AUTHORITY, Booking.TABLE_NAME, BOOKINGS);
		bookingsProjectionMap = new HashMap<String, String>();
		bookingsProjectionMap.put(Bookings.ID, Bookings.ID);
		bookingsProjectionMap.put(Bookings.USER_ID, Bookings.USER_ID);
		bookingsProjectionMap.put(Bookings.HOTEL_ID, Bookings.HOTEL_ID);
		bookingsProjectionMap.put(Bookings.CHECKIN_DATE, Bookings.CHECKIN_DATE);
		bookingsProjectionMap.put(Bookings.CHECKOUT_DATE, Bookings.CHECKOUT_DATE);
		bookingsProjectionMap.put(Bookings.CREDIT_CARD_NUMBER, Bookings.CREDIT_CARD_NUMBER);
		bookingsProjectionMap.put(Bookings.CREDIT_CARD_TYPE, Bookings.CREDIT_CARD_TYPE);
		bookingsProjectionMap.put(Bookings.CREDIT_CARD_NAME, Bookings.CREDIT_CARD_NAME);
		bookingsProjectionMap.put(Bookings.CREDIT_CARD_EXPIRY_MONTH, Bookings.CREDIT_CARD_EXPIRY_MONTH);
		bookingsProjectionMap.put(Bookings.CREDIT_CARD_EXPIRY_YEAR, Bookings.CREDIT_CARD_EXPIRY_YEAR);
		bookingsProjectionMap.put(Bookings.SMOKING, Bookings.SMOKING);
		bookingsProjectionMap.put(Bookings.BEDS, Bookings.BEDS);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case USERS:
			count = db.delete(User.TABLE_NAME, where, whereArgs);
			break;
		case HOTELS:
			count = db.delete(Hotel.TABLE_NAME, where, whereArgs);
			break;
		case BOOKINGS:
			count = db.delete(Booking.TABLE_NAME, where, whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case USERS:
			return Users.CONTENT_TYPE;
		case HOTELS:
			return Hotels.CONTENT_TYPE;
		case BOOKINGS:
			return Bookings.CONTENT_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		switch (sUriMatcher.match(uri)) {
		case USERS:
			long rowId = db.insert(User.TABLE_NAME, Users.LOGIN, values);
			if (rowId > 0) {
				Uri noteUri = ContentUris.withAppendedId(Users.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(noteUri, null);
				return noteUri;
			}
			break;
		case HOTELS:
			rowId = db.insert(Hotel.TABLE_NAME, Hotels.NAME, values);
			if (rowId > 0) {
				Uri noteUri = ContentUris.withAppendedId(Hotels.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(noteUri, null);
				return noteUri;
			}
			break;
		case BOOKINGS:
			rowId = db.insert(Booking.TABLE_NAME, Bookings.USER_ID, values);
			if (rowId > 0) {
				Uri noteUri = ContentUris.withAppendedId(Bookings.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(noteUri, null);
				return noteUri;
			}
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		databaseHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case USERS:
			qb.setTables(User.TABLE_NAME);
			qb.setProjectionMap(usersProjectionMap);
			break;
		case HOTELS:
			qb.setTables(Hotel.TABLE_NAME);
			qb.setProjectionMap(hotelsProjectionMap);
			break;
		case BOOKINGS:
			qb.setTables(Booking.TABLE_NAME);
			qb.setProjectionMap(bookingsProjectionMap);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case USERS:
			count = db.update(User.TABLE_NAME, values, where, whereArgs);
			break;
		case HOTELS:
			count = db.update(Hotel.TABLE_NAME, values, where, whereArgs);
			break;
		case BOOKINGS:
			count = db.update(Booking.TABLE_NAME, values, where, whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}