package org.pullrequest.android.bookingnative.provider;

import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;
import org.pullrequest.android.bookingnative.domain.model.User.Users;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String USERS_TABLE_NAME = "users";
	public static final String HOTELS_TABLE_NAME = "hotels";

	private static final String DATABASE_NAME = "booking.db";
	private static final int DATABASE_VERSION = 6;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + USERS_TABLE_NAME + " (" + Users.ID + " INTEGER PRIMARY KEY, " + Users.FIRST_NAME + " TEXT," + Users.LAST_NAME + " TEXT," + Users.LOGIN + " TEXT," + Users.PASSWORD + " TEXT);");
		db.execSQL("CREATE TABLE " + HOTELS_TABLE_NAME + " (" + Hotels.ID + " INTEGER PRIMARY KEY, " + Hotels.NAME + " TEXT," + Hotels.ADDRESS + " TEXT," + Hotels.CITY + " TEXT," + Hotels.STATE + " TEXT," + Hotels.COUNTRY + " TEXT," + Hotels.ZIP + " TEXT," + Hotels.STARS + " INTEGER," + Hotels.PRICE + " REAL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HOTELS_TABLE_NAME);
		onCreate(db);
	}

	public void reset() {
		SQLiteDatabase db = this.getWritableDatabase();
		this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
		this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + HOTELS_TABLE_NAME);
		this.onCreate(db);
		db.close();
	}
}