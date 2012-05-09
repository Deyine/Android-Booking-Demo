package org.pullrequest.android.bookingnative.domain;

import java.sql.SQLException;

import com.google.inject.Provider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class DaoProvider<T, D extends Dao<T, ?>> implements Provider<D> {
	protected ConnectionSource conn;
	protected Class<T> clazz;

	public DaoProvider(ConnectionSource conn, Class<T> clazz) {
		this.conn = conn;
		this.clazz = clazz;
	}

	@Override
	public D get() {
		try {
			D dao = DaoManager.createDao(conn, clazz);
			return dao;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}