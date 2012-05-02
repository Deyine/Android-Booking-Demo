package org.pullrequest.android.bookingnative.domain.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.domain.dao.UserDao;
import org.pullrequest.android.bookingnative.domain.model.User;
import org.pullrequest.android.bookingnative.domain.model.User.Schema;

import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class UserDaoImpl extends BaseDaoImpl<User, Integer> implements UserDao {

	private ConnectionSource connectionSource;

	public UserDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig<User> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
		this.connectionSource = connectionSource;
	}

	@Override
	public ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	@Override
	public User create(JSONObject json) {
		User user = new User();
		try {
			user.setId(json.getLong(Schema.ID));
			user.setFirstName(json.getString(Schema.FIRST_NAME));
			user.setLastName(json.getString(Schema.LAST_NAME));
			user.setLogin(json.getString(Schema.LOGIN));
			user.setPassword(json.getString(Schema.PASSWORD));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User findByLogin(String login) {
		try {
			List<User> users = this.queryForEq(Schema.LOGIN, login);
			if(!users.isEmpty()) {
				return users.get(0);
			}
		} catch (SQLException e) {
			Log.e(C.LOG_TAG, "Error querying users for " + login + " login.", e);
		}
		return null;
	}
}