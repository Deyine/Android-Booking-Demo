package org.pullrequest.android.bookingnative.domain.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.pullrequest.android.bookingnative.provider.DataProvider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class User {

	private int id;
	private String firstName;
	private String lastName;
	private String login;
	private String password;

	public User() {

	}

	public User(JSONObject json) {
		try {
			this.id = json.getInt(Users.ID);
			this.firstName = json.getString(Users.FIRST_NAME);
			this.lastName = json.getString(Users.LAST_NAME);
			this.login = json.getString(Users.LOGIN);
			this.password = json.getString(Users.PASSWORD);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
