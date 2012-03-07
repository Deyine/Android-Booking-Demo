package org.pullrequest.android.bookingnative.domain.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.pullrequest.android.bookingnative.provider.DataProvider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Hotel {

    private long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private int stars;
    private double price;

	public Hotel() {

	}

	public Hotel(JSONObject json) {
		try {
			this.id = json.getLong(Hotels.ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public static final class Hotels implements BaseColumns {
		private Hotels() {
			//
		}

		/**
		 * 
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + DataProvider.AUTHORITY + "/hotels");

		/**
		 * 
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.booking.hotels";

		/**
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String ID = "_id";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String NAME = "name";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String ADDRESS = "address";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String CITY = "city";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String STATE = "state";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String ZIP = "zip";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String COUNTRY = "country";

		/**
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String STARS = "stars";

		/**
		 * <P>
		 * Type: REAL
		 * </P>
		 */
		public static final String PRICE = "price";
	}
}