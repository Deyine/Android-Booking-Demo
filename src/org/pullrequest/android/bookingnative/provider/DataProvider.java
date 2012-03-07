package org.pullrequest.android.bookingnative.provider;

import java.util.HashMap;

import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;
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

	public static final String AUTHORITY = "org.pullrequest.android.bookingnative.provider.dataprovider";

	private static final UriMatcher sUriMatcher;

	private DatabaseHelper databaseHelper;
	private static HashMap<String, String> usersProjectionMap;
	private static HashMap<String, String> hotelsProjectionMap;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		sUriMatcher.addURI(AUTHORITY, DatabaseHelper.USERS_TABLE_NAME, USERS);
		usersProjectionMap = new HashMap<String, String>();
		usersProjectionMap.put(Users.ID, Users.ID);
		usersProjectionMap.put(Users.FIRST_NAME, Users.FIRST_NAME);
		usersProjectionMap.put(Users.LAST_NAME, Users.LAST_NAME);
		usersProjectionMap.put(Users.LOGIN, Users.LOGIN);
		usersProjectionMap.put(Users.PASSWORD, Users.PASSWORD);

		sUriMatcher.addURI(AUTHORITY, DatabaseHelper.HOTELS_TABLE_NAME, HOTELS);
		hotelsProjectionMap = new HashMap<String, String>();
		hotelsProjectionMap.put(Hotels.ID, Hotels.ID);
		hotelsProjectionMap.put(Hotels.NAME, Hotels.NAME);
		hotelsProjectionMap.put(Hotels.ADDRESS, Hotels.ADDRESS);
		hotelsProjectionMap.put(Hotels.CITY, Hotels.CITY);
		hotelsProjectionMap.put(Hotels.ZIP, Hotels.ZIP);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case USERS:
			count = db.delete(DatabaseHelper.USERS_TABLE_NAME, where, whereArgs);
			break;
		case HOTELS:
			count = db.delete(DatabaseHelper.HOTELS_TABLE_NAME, where, whereArgs);
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
			long rowId = db.insert(DatabaseHelper.USERS_TABLE_NAME, Users.LOGIN, values);
			if (rowId > 0) {
				Uri noteUri = ContentUris.withAppendedId(Users.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(noteUri, null);
				return noteUri;
			}
			break;
		case HOTELS:
			rowId = db.insert(DatabaseHelper.HOTELS_TABLE_NAME, Hotels.NAME, values);
			if (rowId > 0) {
				Uri noteUri = ContentUris.withAppendedId(Hotels.CONTENT_URI, rowId);
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
			qb.setTables(DatabaseHelper.USERS_TABLE_NAME);
			qb.setProjectionMap(usersProjectionMap);
			break;
		case HOTELS:
			qb.setTables(DatabaseHelper.HOTELS_TABLE_NAME);
			qb.setProjectionMap(hotelsProjectionMap);
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
			count = db.update(DatabaseHelper.USERS_TABLE_NAME, values, where, whereArgs);
			break;
		case HOTELS:
			count = db.update(DatabaseHelper.HOTELS_TABLE_NAME, values, where, whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}