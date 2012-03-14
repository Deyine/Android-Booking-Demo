package org.pullrequest.android.bookingnative.domain.model;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;
import org.pullrequest.android.bookingnative.provider.DataProvider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Booking implements Serializable {
	
	private static final long serialVersionUID = 5929577397946339674L;
	
    private Long id;
    private int userId;
    private Long hotelId;
    private Date checkinDate;
    private Date checkoutDate;
    private String creditCardNumber;
    private String creditCardType;
    private String creditCardName;
    private int creditCardExpiryMonth;
    private int creditCardExpiryYear;
    private boolean smoking;
    private int beds;
    
	public Booking() {

	}

	public Booking(JSONObject json) {
		try {
			this.id = json.getLong(Hotels.ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public Date getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(Date checkinDate) {
		this.checkinDate = checkinDate;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getCreditCardType() {
		return creditCardType;
	}

	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	public String getCreditCardName() {
		return creditCardName;
	}

	public void setCreditCardName(String creditCardName) {
		this.creditCardName = creditCardName;
	}

	public int getCreditCardExpiryMonth() {
		return creditCardExpiryMonth;
	}

	public void setCreditCardExpiryMonth(int creditCardExpiryMonth) {
		this.creditCardExpiryMonth = creditCardExpiryMonth;
	}

	public int getCreditCardExpiryYear() {
		return creditCardExpiryYear;
	}

	public void setCreditCardExpiryYear(int creditCardExpiryYear) {
		this.creditCardExpiryYear = creditCardExpiryYear;
	}

	public boolean isSmoking() {
		return smoking;
	}

	public void setSmoking(boolean smoking) {
		this.smoking = smoking;
	}

	public int getBeds() {
		return beds;
	}

	public void setBeds(int beds) {
		this.beds = beds;
	}


	public static final class Bookings implements BaseColumns {
		private Bookings() {
			//
		}

		/**
		 * 
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + DataProvider.AUTHORITY + "/bookings");

		/**
		 * 
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.booking.bookings";

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
		public static final String HOTEL_ID = "hotel_id";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String USER_ID = "user_id";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String CHECKIN_DATE = "checkinDate";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String CHECKOUT_DATE = "checkoutDate";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String CREDIT_CARD_NUMBER = "creditCardNumber";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String CREDIT_CARD_TYPE = "creditCardType";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String CREDIT_CARD_NAME = "creditCardName";

		/**
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String CREDIT_CARD_EXPIRY_MONTH = "creditCardExpiryMonth";

		/**
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String CREDIT_CARD_EXPIRY_YEAR = "creditCardExpiryYear";

		/**
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String SMOKING = "smoking";

		/**
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String BEDS = "beds";
	}
}