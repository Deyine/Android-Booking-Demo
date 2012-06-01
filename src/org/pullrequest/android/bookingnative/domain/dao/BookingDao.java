package org.pullrequest.android.bookingnative.domain.dao;

import java.sql.SQLException;

import org.json.JSONObject;
import org.pullrequest.android.bookingnative.domain.model.Booking;

import android.database.Cursor;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public interface BookingDao extends Dao<Booking, Long> {

	ConnectionSource getConnectionSource();
	
	Booking create(JSONObject json);
	
	Cursor findAll() throws SQLException;
	
	Cursor findByUserId(long userId) throws SQLException;
}
