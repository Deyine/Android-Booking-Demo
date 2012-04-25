package org.pullrequest.android.bookingnative.domain.model;

import java.io.Serializable;

import org.pullrequest.android.bookingnative.domain.dao.impl.UserDaoImpl;
import org.pullrequest.android.bookingnative.provider.DataProvider;

import android.net.Uri;
import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(daoClass = UserDaoImpl.class, tableName = User.TABLE_NAME)
public final class User implements Serializable {

	private static final long serialVersionUID = -3366808703621227882L;

	public static final String TABLE_NAME = "users";
	
	@DatabaseField(generatedId = true, columnName = Users.ID)
	private long id;
	
	@DatabaseField(columnName = Users.FIRST_NAME)
	private String firstName;
	
	@DatabaseField(columnName = Users.LAST_NAME)
	private String lastName;
	
	@DatabaseField(columnName = Users.LOGIN)
	private String login;
	
	@DatabaseField(columnName = Users.PASSWORD)
	private String password;

	public User() {

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

	public static final class Users implements BaseColumns {
		private Users() {
			//
		}

		/**
		 * 
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + DataProvider.AUTHORITY + "/users");

		/**
		 * 
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.booking.users";

		/**
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String ID = "id";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String FIRST_NAME = "first_name";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String LAST_NAME = "last_name";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String LOGIN = "login";

		/**
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String PASSWORD = "password";
	}
}
