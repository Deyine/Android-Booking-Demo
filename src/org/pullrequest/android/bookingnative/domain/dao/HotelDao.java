package org.pullrequest.android.bookingnative.domain.dao;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.pullrequest.android.bookingnative.domain.model.Hotel;

import android.database.Cursor;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public interface HotelDao extends Dao<Hotel, Long> {

	ConnectionSource getConnectionSource();
	
	Hotel create(JSONObject json);
	
	boolean updateList(JSONArray hotels);

	boolean updateList(List<Hotel> hotels);
	
	Cursor findAll() throws SQLException;
	
	Cursor search(String query) throws SQLException;
}
