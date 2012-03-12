package org.pullrequest.android.bookingnative.domain.dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class HotelDao {

	private Context context;

	private static HotelDao instance;
	private static String[] projection = new String[] { Hotels.ID, Hotels.NAME, Hotels.ADDRESS, Hotels.CITY, Hotels.ZIP, Hotels.STATE, Hotels.COUNTRY, Hotels.STARS, Hotels.PRICE };

	public static HotelDao getInstance(Context context) {
		if (instance == null) {
			instance = new HotelDao(context);
		}
		return instance;
	}

	private HotelDao(Context context) {
		this.context = context;
	}

	public Uri add(Hotel hotel) {
		ContentValues values = new ContentValues();
		values.put(Hotels.NAME, hotel.getName());
		values.put(Hotels.ADDRESS, hotel.getAddress());
		values.put(Hotels.CITY, hotel.getCity());
		values.put(Hotels.STATE, hotel.getState());
		values.put(Hotels.ZIP, hotel.getZip());
		values.put(Hotels.STARS, hotel.getStars());
		values.put(Hotels.PRICE, hotel.getPrice());
		return context.getContentResolver().insert(Hotels.CONTENT_URI, values);
	}

	public void update(Hotel hotel) {
		ContentValues values = new ContentValues();
		values.put(Hotels.NAME, hotel.getName());
		values.put(Hotels.ADDRESS, hotel.getAddress());
		values.put(Hotels.CITY, hotel.getCity());
		values.put(Hotels.STATE, hotel.getState());
		values.put(Hotels.ZIP, hotel.getZip());
		values.put(Hotels.STARS, hotel.getStars());
		values.put(Hotels.PRICE, hotel.getPrice());
		context.getContentResolver().update(Hotels.CONTENT_URI, values, Hotels.ID + " = ?", new String[] { String.valueOf(hotel.getId()) });
	}

	public Hotel getById(long id) {
		Hotel hotel = null;
		Cursor cursor = context.getContentResolver().query(Hotels.CONTENT_URI, projection, Hotels.ID + " = ?", new String[] { String.valueOf(id) }, null);
		if (cursor.moveToFirst()) {
			hotel = getFromCursor(cursor);
		}
		cursor.close();
		return hotel;
	}

	public Hotel getByName(String name) {
		Hotel hotel = null;
		Cursor cursor = context.getContentResolver().query(Hotels.CONTENT_URI, projection, Hotels.NAME + " = ?", new String[] { name }, null);
		if (cursor.moveToFirst()) {
			hotel = getFromCursor(cursor);
		}
		cursor.close();
		return hotel;
	}

	public Cursor search(String query) {
		return context.getContentResolver().query(Hotels.CONTENT_URI, projection, Hotels.NAME + " like '%" + query + "%'", null, null);
	}

	public Cursor getAll() {
		return context.getContentResolver().query(Hotels.CONTENT_URI, projection, null, null, Hotels.NAME + " asc");
	}

	public int removeAll() {
		return context.getContentResolver().delete(Hotels.CONTENT_URI, null, null);
	}

	public int delete(long id) {
		return context.getContentResolver().delete(Hotels.CONTENT_URI, Hotels.ID + " = " + String.valueOf(id), null);
	}

	public int count() {
		return this.getAll().getCount();
	}

	public static Hotel getFromCursor(Cursor cursor) {
		Hotel hotel = new Hotel();
		hotel.setId(cursor.getInt(0));
		hotel.setName(cursor.getString(1));
		hotel.setAddress(cursor.getString(2));
		hotel.setCity(cursor.getString(3));
		hotel.setZip(cursor.getString(4));
		hotel.setState(cursor.getString(5));
		hotel.setCountry(cursor.getString(6));
		hotel.setStars(cursor.getInt(7));
		hotel.setPrice(cursor.getDouble(8));
		return hotel;
	}

	/**
	 * 
	 * @param hotels
	 */
	public boolean updateList(JSONArray hotels) {
		try {
			for (int i = 0; i < hotels.length(); i++) {
				JSONObject jsonHotel = hotels.getJSONObject(i);
				Hotel hotel = convertFromJson(jsonHotel);
				if(getById(hotel.getId()) != null) {
					update(hotel);
				}
				else {
					add(hotel);
				}
			}
		} catch (JSONException e) {
			Log.e(C.LOG_TAG, "Problem during hotels json parsing", e);
			return false;
		}
		return true;
	}

	public Hotel convertFromJson(JSONObject json) throws JSONException {
		Hotel hotel = new Hotel();
		if (!json.isNull(Hotel.Hotels.ID)) {
			hotel.setId(json.getLong(Hotel.Hotels.ID));
		}
		hotel.setName(json.getString(Hotel.Hotels.NAME));
		hotel.setAddress(json.getString(Hotel.Hotels.ADDRESS));
		hotel.setCity(json.getString(Hotel.Hotels.CITY));
		if (json.has(Hotel.Hotels.COUNTRY)) {
			hotel.setCountry(json.getString(Hotel.Hotels.COUNTRY));
		}
		if (json.has(Hotel.Hotels.STATE)) {
			hotel.setState(json.getString(Hotel.Hotels.STATE));
		}
		if (json.has(Hotel.Hotels.ZIP)) {
			hotel.setZip(json.getString(Hotel.Hotels.ZIP));
		}
		if (json.has(Hotel.Hotels.PRICE)) {
			hotel.setPrice(json.getDouble(Hotel.Hotels.PRICE));
		}
		if (json.has(Hotel.Hotels.STARS)) {
			hotel.setStars(json.getInt(Hotel.Hotels.STARS));
		}
		return hotel;
	}
}