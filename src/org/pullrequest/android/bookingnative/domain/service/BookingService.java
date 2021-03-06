package org.pullrequest.android.bookingnative.domain.service;

import org.pullrequest.android.bookingnative.domain.dao.BookingDao;
import org.pullrequest.android.bookingnative.domain.model.Booking;

import android.content.Context;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class BookingService extends Service<Booking> {

	@RootContext
	Context context;

	public BookingService() {
		
	}
	
	@AfterInject
	public void setDao() {
		this.setDao(context, Booking.class);
	}

	@Override
	public BookingDao getDao() {
		return (BookingDao) this.dao;
	}
}
