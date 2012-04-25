package org.pullrequest.android.bookingnative.domain.dao;

import org.json.JSONObject;
import org.pullrequest.android.bookingnative.domain.model.User;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public interface UserDao extends Dao<User, Integer> {

	ConnectionSource getConnectionSource();
	
	User create(JSONObject json);
	
	User findByLogin(String login);
}
