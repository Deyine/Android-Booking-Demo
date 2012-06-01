package org.pullrequest.android.bookingnative.domain.service;

import java.sql.SQLException;

import org.pullrequest.android.bookingnative.domain.DatabaseHelper;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

public abstract class Service<T> {

	protected Dao<T, Long> dao;

	public void setDao(Context context, Class<T> clazz) {
		try {
			dao = DaoManager.createDao(OpenHelperManager.getHelper(context, DatabaseHelper.class).getConnectionSource(), clazz);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Dao<T, Long> getDao() {
		return this.dao;
	}
}
