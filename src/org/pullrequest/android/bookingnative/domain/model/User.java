package org.pullrequest.android.bookingnative.domain.model;

import java.io.Serializable;

import org.pullrequest.android.bookingnative.domain.dao.impl.UserDaoImpl;
import org.pullrequest.android.bookingnative.domain.model.Booking.Bookings;
import org.pullrequest.android.bookingnative.provider.DataProvider;

import android.net.Uri;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(daoClass = UserDaoImpl.class, tableName = User.TABLE_NAME)
public final class User implements Serializable {

	private static final long serialVersionUID = -3366808703621227882L;

	public static final String TABLE_NAME = "users";
	
	@DatabaseField(generatedId = true, columnName = Schema.ID)
	private long id;
	
	@DatabaseField(columnName = Schema.FIRST_NAME)
	private String firstName;
	
	@DatabaseField(columnName = Schema.LAST_NAME)
	private String lastName;
	
	@DatabaseField(columnName = Schema.LOGIN)
	private String login;
	
	@DatabaseField(columnName = Schema.PASSWORD)
	private String password;

	@ForeignCollectionField(eager = true, orderColumnName = Bookings.CHECKIN_DATE)
	private ForeignCollection<Booking> bookings;
	
	public User() {

	}

	public User(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ForeignCollection<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(ForeignCollection<Booking> bookings) {
		this.bookings = bookings;
	}

	public static final class Schema {
		private Schema() {
			//
		}

		public static final Uri CONTENT_URI = Uri.parse("content://" + DataProvider.AUTHORITY + "/users");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.booking.users";

		public static final String ID = "id";
		public static final String FIRST_NAME = "first_name";
		public static final String LAST_NAME = "last_name";
		public static final String LOGIN = "login";
		public static final String PASSWORD = "password";
	}
}
