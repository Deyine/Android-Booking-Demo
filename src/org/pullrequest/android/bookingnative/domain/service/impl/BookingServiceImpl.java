package org.pullrequest.android.bookingnative.domain.service.impl;

import java.sql.SQLException;

import javax.inject.Inject;

import org.pullrequest.android.bookingnative.domain.dao.BookingDao;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.dao.UserDao;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.pullrequest.android.bookingnative.domain.model.User;
import org.pullrequest.android.bookingnative.domain.service.BookingService;

import roboguice.util.Ln;

import com.j256.ormlite.table.TableUtils;

public class BookingServiceImpl implements BookingService {

	@Inject
	private UserDao userDao;

	@Inject
	private BookingDao bookingDao;

	@Inject
	private HotelDao hotelDao;
	
	@Override
	public void resetDatabase() {
		try {
			Ln.d("Clearing database tables");
			TableUtils.clearTable(userDao.getConnectionSource(), User.class);
			TableUtils.clearTable(bookingDao.getConnectionSource(), Booking.class);
			TableUtils.clearTable(hotelDao.getConnectionSource(), Hotel.class);
		} catch (SQLException e) {
			Ln.d(e, "Problem during database reset");
		}
	}

}