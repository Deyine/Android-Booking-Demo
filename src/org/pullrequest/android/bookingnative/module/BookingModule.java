package org.pullrequest.android.bookingnative.module;

import org.pullrequest.android.bookingnative.domain.DaoProvider;
import org.pullrequest.android.bookingnative.domain.DatabaseHelper;
import org.pullrequest.android.bookingnative.domain.dao.BookingDao;
import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.dao.UserDao;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.pullrequest.android.bookingnative.domain.model.User;
import org.pullrequest.android.bookingnative.domain.service.BookingService;
import org.pullrequest.android.bookingnative.domain.service.impl.BookingServiceImpl;

import android.content.Context;

import com.google.inject.AbstractModule;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class BookingModule extends AbstractModule {

	private Context context;

	public BookingModule(Context context) {
		this.context = context;
	}

	@Override
	protected void configure() {
		bind(UserDao.class).toProvider(new DaoProvider<User, UserDao>(OpenHelperManager.getHelper(context, DatabaseHelper.class).getConnectionSource(), User.class));
		bind(HotelDao.class).toProvider(new DaoProvider<Hotel, HotelDao>(OpenHelperManager.getHelper(context, DatabaseHelper.class).getConnectionSource(), Hotel.class));
		bind(BookingDao.class).toProvider(new DaoProvider<Booking, BookingDao>(OpenHelperManager.getHelper(context, DatabaseHelper.class).getConnectionSource(), Booking.class));
		bind(BookingService.class).to(BookingServiceImpl.class);
	}
}