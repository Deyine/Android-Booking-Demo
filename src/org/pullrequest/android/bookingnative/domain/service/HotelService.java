package org.pullrequest.android.bookingnative.domain.service;

import org.pullrequest.android.bookingnative.domain.dao.HotelDao;
import org.pullrequest.android.bookingnative.domain.model.Hotel;

import android.content.Context;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class HotelService extends Service<Hotel> {

	@RootContext
	Context context;

	public HotelService() {
		
	}
	
	@AfterInject
	public void setDao() {
		this.setDao(context, Hotel.class);
	}

	@Override
	public HotelDao getDao() {
		return (HotelDao) this.dao;
	}
}
