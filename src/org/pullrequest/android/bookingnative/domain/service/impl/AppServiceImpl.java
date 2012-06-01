package org.pullrequest.android.bookingnative.domain.service.impl;

import java.sql.SQLException;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.domain.DatabaseHelper;
import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.pullrequest.android.bookingnative.domain.model.User;
import org.pullrequest.android.bookingnative.domain.service.AppService;

import android.content.Context;
import android.util.Log;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

@EBean
public class AppServiceImpl implements AppService {

	@RootContext
	Context context;
	
	@Override
	public void resetDatabase() {
		try {
			Log.d(C.LOG_TAG, "Clearing database tables");
			ConnectionSource conn = OpenHelperManager.getHelper(context, DatabaseHelper.class).getConnectionSource();
			TableUtils.clearTable(conn, User.class);
			TableUtils.clearTable(conn, Booking.class);
			TableUtils.clearTable(conn, Hotel.class);
		} catch (SQLException e) {
			Log.d(C.LOG_TAG, "Problem during database reset", e);
		}
	}

}