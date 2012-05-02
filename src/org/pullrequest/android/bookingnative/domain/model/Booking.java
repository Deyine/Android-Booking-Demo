package org.pullrequest.android.bookingnative.domain.model;

import java.io.Serializable;
import java.util.Date;

import org.pullrequest.android.bookingnative.domain.dao.impl.BookingDaoImpl;
import org.pullrequest.android.bookingnative.provider.DataProvider;

import android.net.Uri;
import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(daoClass = BookingDaoImpl.class, tableName = Booking.TABLE_NAME)
public final class Booking implements Serializable {

	private static final long serialVersionUID = -4626796501390211104L;

	public static final String TABLE_NAME = "bookings";
	
	@DatabaseField(generatedId = true,columnName = Bookings.ID)
    private long _id;
    
    @DatabaseField(canBeNull = false, foreign = true)
    private User user;
    
    @DatabaseField(canBeNull = false, foreign = true)
    private Hotel hotel;
    
    @DatabaseField(columnName = Bookings.CHECKIN_DATE)
    private Date checkinDate;
    
    @DatabaseField(columnName = Bookings.CHECKOUT_DATE)
    private Date checkoutDate;
    
    @DatabaseField(columnName = Bookings.CREDIT_CARD_NUMBER)
    private String creditCardNumber;
    
    @DatabaseField(columnName = Bookings.CREDIT_CARD_TYPE)
    private String creditCardType;
    
    @DatabaseField(columnName = Bookings.CREDIT_CARD_NAME)
    private String creditCardName;
    
    @DatabaseField(columnName = Bookings.CREDIT_CARD_EXPIRY_MONTH)
    private int creditCardExpiryMonth;
    
    @DatabaseField(columnName = Bookings.CREDIT_CARD_EXPIRY_YEAR)
    private int creditCardExpiryYear;
    
    @DatabaseField(columnName = Bookings.SMOKING)
    private boolean smoking;
    
    @DatabaseField(columnName = Bookings.BEDS)
    private int beds;
    
    public Booking() {
    	
    }

	public long get_id() {
		return _id;
	}

	public void set_id(long id) {
		this._id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
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