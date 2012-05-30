package org.pullrequest.android.bookingnative.domain.model;

import java.io.Serializable;

import org.pullrequest.android.bookingnative.domain.dao.impl.HotelDaoImpl;
import org.pullrequest.android.bookingnative.provider.DataProvider;

import android.net.Uri;
import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(daoClass = HotelDaoImpl.class, tableName = Hotel.TABLE_NAME)
public final class Hotel implements Serializable {

	private static final long serialVersionUID = -4682119973261761774L;

	public static final String TABLE_NAME = "hotels";

	@DatabaseField(id = true, columnName = Hotels.ID)
	private long id;

	@DatabaseField(columnName = Hotels.NAME)
	private String name;

	@DatabaseField(columnName = Hotels.ADDRESS)
	private String address;

	@DatabaseField(columnName = Hotels.CITY)
	private String city;

	@DatabaseField(columnName = Hotels.STATE)
	private String state;

	@DatabaseField(columnName = Hotels.ZIP)
	private String zip;

	@DatabaseField(columnName = Hotels.COUNTRY)
	private String country;

	@DatabaseField(columnName = Hotels.STARS)
	private int stars;

	@DatabaseField(columnName = Hotels.PRICE)
	private double price;

	public Hotel() {

	}

	public Hotel(long id) {
		this.id = id;
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