package org.pullrequest.android.bookingnative.domain.dao.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;
import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.domain.dao.BookingDao;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Booking.Bookings;

import android.database.Cursor;
import android.util.Log;

import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class BookingDaoImpl extends BaseDaoImpl<Booking, Integer> implements BookingDao {

	private ConnectionSource connectionSource;

	public BookingDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig<Booking> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
		this.connectionSource = connectionSource;
	}

	@Override
	public ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	@Override
	public Booking create(JSONObject json) {
		Booking booking = new Booking();
		try {
			booking.set_id(json.getLong(Bookings.ID));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return booking;
	}

	@Override
	public Cursor findAll() throws SQLException {
		AndroidCompiledStatement acs = (AndroidCompiledStatement) this.queryBuilder().orderBy(Bookings.CHECKIN_DATE, false).prepare().compile(this.connectionSource.getReadOnlyConnection(), StatementType.SELECT);
		return acs.getCursor();
	}
	
	public static Booking getFromCursor(Cursor cursor) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		Booking booking = new Booking();
		booking.set_id(cursor.getLong(0));
		booking.setUserId(cursor.getInt(1));
		booking.setHotelId(cursor.getLong(2));
		try {
			booking.setCheckinDate(dateFormat.parse(cursor.getString(3)));
			booking.setCheckoutDate(dateFormat.parse(cursor.getString(4)));
		} catch (ParseException e) {
			Log.w(C.LOG_TAG, "Can't parse checkin/out date", e);
		}
		booking.setCreditCardNumber(cursor.getString(5));
		booking.setCreditCardType(cursor.getString(6));
		booking.setCreditCardName(cursor.getString(7));
		booking.setCreditCardExpiryMonth(cursor.getInt(8));
		booking.setCreditCardExpiryYear(cursor.getInt(9));
		booking.setSmoking(cursor.getInt(10) == 1);
		booking.setBeds(cursor.getInt(11));
		return booking;
	}
}