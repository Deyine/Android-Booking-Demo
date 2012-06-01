package org.pullrequest.android.bookingnative.domain.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.pullrequest.android.bookingnative.domain.model.Hotel.Hotels;

import android.database.Cursor;
import android.util.Log;

import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class HotelDaoImpl extends BaseDaoImpl<Hotel, Long> implements HotelDao {

	private ConnectionSource connectionSource;

	public HotelDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig<Hotel> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
		this.connectionSource = connectionSource;
	}

	@Override
	public ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	@Override
	public Hotel create(JSONObject json) {
		Hotel hotel = new Hotel();
		try {
			hotel.setId(json.getLong(Hotels.ID));
			hotel.setName(json.getString(Hotels.NAME));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hotel;
	}

	@Override
	public boolean updateList(JSONArray hotels) {
		try {
			for (int i = 0; i < hotels.length(); i++) {
				JSONObject jsonHotel = hotels.getJSONObject(i);
				Hotel hotel = convertFromJson(jsonHotel);
				if(this.queryForId(hotel.getId()) != null) {
					this.update(hotel);
				}
				else {
					this.create(hotel);
				}
			}
		} catch (JSONException e) {
			Log.e(C.LOG_TAG, "Problem during hotels json parsing", e);
			return false;
		} catch (SQLException e) {
			Log.e(C.LOG_TAG, "Problem during hotels sql persisting", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean updateList(List<Hotel> hotels) {
		try {
			for(Hotel hotel : hotels) {
				if(this.queryForId(hotel.getId()) != null) {
					this.update(hotel);
				}
				else {
					this.create(hotel);
				}
			}
		} catch (SQLException e) {
			Log.e(C.LOG_TAG, "Problem during hotels sql persisting", e);
			return false;
		}
		return true;
	}
	
	private Hotel convertFromJson(JSONObject json) throws JSONException {
		Hotel hotel = new Hotel();
		if (!json.isNull(Hotels.ID)) {
			hotel.setId(json.getLong(Hotels.ID));
		}
		hotel.setName(json.getString(Hotels.NAME));
		hotel.setAddress(json.getString(Hotels.ADDRESS));
		hotel.setCity(json.getString(Hotels.CITY));
		if (json.has(Hotels.COUNTRY)) {
			hotel.setCountry(json.getString(Hotels.COUNTRY));
		}
		if (json.has(Hotels.STATE)) {
			hotel.setState(json.getString(Hotels.STATE));
		}
		if (json.has(Hotels.ZIP)) {
			hotel.setZip(json.getString(Hotels.ZIP));
		}
		if (json.has(Hotels.PRICE)) {
			hotel.setPrice(json.getDouble(Hotels.PRICE));
		}
		if (json.has(Hotels.STARS)) {
			hotel.setStars(json.getInt(Hotels.STARS));
		}
		return hotel;
	}

	@Override
	public Cursor findAll() throws SQLException {
		AndroidCompiledStatement acs = (AndroidCompiledStatement) this.queryBuilder().orderBy(Hotels.NAME, false).prepare().compile(this.connectionSource.getReadOnlyConnection(), StatementType.SELECT);
		return acs.getCursor();
	}
	
	public static Hotel getFromCursor(Cursor cursor) {
		Hotel hotel = new Hotel();
		hotel.setId(cursor.getInt(0));
		hotel.setName(cursor.getString(1));
		hotel.setAddress(cursor.getString(2));
		hotel.setCity(cursor.getString(3));
		hotel.setZip(cursor.getString(4));
		hotel.setState(cursor.getString(5));
		hotel.setCountry(cursor.getString(6));
		hotel.setStars(cursor.getInt(7));
		hotel.setPrice(cursor.getDouble(8));
		return hotel;
	}

	@Override
	public Cursor search(String query) throws SQLException {
		AndroidCompiledStatement acs = (AndroidCompiledStatement)  this.queryBuilder().where().like(Hotels.NAME, "%" + query + "%").prepare().compile(this.connectionSource.getReadOnlyConnection(), StatementType.SELECT);
		return acs.getCursor();
	}

}