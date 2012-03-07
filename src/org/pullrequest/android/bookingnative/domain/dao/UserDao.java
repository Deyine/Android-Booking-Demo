package org.pullrequest.android.bookingnative.domain.dao;

import org.pullrequest.android.bookingnative.domain.model.User;
import org.pullrequest.android.bookingnative.domain.model.User.Users;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class UserDao {

	private Context context;

	private static UserDao instance;
	private static String[] projection = new String[] { Users.ID, Users.FIRST_NAME, Users.LAST_NAME, Users.LOGIN, Users.PASSWORD };

	public static UserDao getInstance(Context context) {
		if (instance == null) {
			instance = new UserDao(context);
		}
		return instance;
	}

	private UserDao(Context context) {
		this.context = context;
	}

	public Uri add(User user) {
		ContentValues values = new ContentValues();
		values.put(Users.FIRST_NAME, user.getFirstName());
		values.put(Users.LAST_NAME, user.getLastName());
		values.put(Users.LOGIN, user.getLogin());
		values.put(Users.PASSWORD, user.getPassword());
		return context.getContentResolver().insert(Users.CONTENT_URI, values);
	}

	public User getById(int id) {
		User user = null;
		Cursor cursor = context.getContentResolver().query(Users.CONTENT_URI, projection, Users.ID + " = ?", new String[] { String.valueOf(id) }, null);
		if (cursor.moveToFirst()) {
			user = convertToUser(cursor);
		}
		cursor.close();
		return user;
	}

	public User getByLogin(String login) {
		User user = null;
		Cursor cursor = context.getContentResolver().query(Users.CONTENT_URI, projection, Users.LOGIN + " = ?", new String[] { login }, null);
		if (cursor.moveToFirst()) {
			user = convertToUser(cursor);
		}
		cursor.close();
		return user;
	}

	public Cursor getAll() {
		return context.getContentResolver().query(Users.CONTENT_URI, null, null, null, Users.FIRST_NAME + " asc");
	}

	public int removeAll() {
		return context.getContentResolver().delete(Users.CONTENT_URI, null, null);
	}

	public int delete(int id) {
		return context.getContentResolver().delete(Users.CONTENT_URI, Users.ID + " = " + id, null);
	}

	public int count() {
		return this.getAll().getCount();
	}

	private User convertToUser(Cursor cursor) {
		User user = new User();
		user.setId(cursor.getInt(0));
		user.setFirstName(cursor.getString(1));
		user.setLastName(cursor.getString(2));
		user.setLogin(cursor.getString(3));
		user.setPassword(cursor.getString(4));
		return user;
	}

}
