package org.pullrequest.android.bookingnative.domain.service;

import org.pullrequest.android.bookingnative.domain.dao.UserDao;
import org.pullrequest.android.bookingnative.domain.model.User;

import android.content.Context;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class UserService extends Service<User> {

	@RootContext
	Context context;

	public UserService() {
		
	}
	
	@AfterInject
	public void setDao() {
		this.setDao(context, User.class);
	}

	@Override
	public UserDao getDao() {
		return (UserDao) this.dao;
	}
}
