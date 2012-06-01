package org.pullrequest.android.bookingnative.domain;

import java.sql.SQLException;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.BookingDao;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.dao.UserDao;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.pullrequest.android.bookingnative.domain.model.User;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "bookingv5.db";
	private static final int DATABASE_VERSION = 5;

	private Dao<Hotel, Long> hotelDao;
	private Dao<User, Long> userDao;
	private Dao<Booking, Long> bookingDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Hotel.class);
			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, Booking.class);
		} catch (SQLException e) {
			Log.e(C.LOG_TAG, "Unable to create datbases", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, Booking.class, true);
			TableUtils.dropTable(connectionSource, User.class, true);
			TableUtils.dropTable(connectionSource, Hotel.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(C.LOG_TAG, "Unable to upgrade database from version " + oldVer + " to new " + newVer, e);
		}
	}

	public HotelDao getHotelDao() {
		if (hotelDao == null) {
			try {
				hotelDao = getDao(Hotel.class);
				hotelDao.setObjectCache(true);
			} catch (Exception e) {
				Log.e(C.LOG_TAG, "unable to get hotel dao", e);
			}
		}
		return (HotelDao) hotelDao;
	}

	public UserDao getUserDao() {
		if (userDao == null) {
			try {
				userDao = getDao(User.class);
			} catch (Exception e) {
				Log.e(C.LOG_TAG, "unable to get user dao", e);
			}
		}
		return (UserDao) userDao;
	}

	public BookingDao getBookingDao() {
		if (bookingDao == null) {
			try {
				bookingDao = getDao(Booking.class);
			} catch (Exception e) {
				Log.e(C.LOG_TAG, "unable to get booking dao", e);
			}
		}
		return (BookingDao) bookingDao;
	}
	
	public void reset() {
		this.onUpgrade(this.getWritableDatabase(), this.getConnectionSource(), DATABASE_VERSION, DATABASE_VERSION);
	}
}