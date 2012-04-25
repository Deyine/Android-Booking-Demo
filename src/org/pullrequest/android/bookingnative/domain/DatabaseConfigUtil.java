package org.pullrequest.android.bookingnative.domain;

import org.pullrequest.android.bookingnative.domain.model.Booking;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.pullrequest.android.bookingnative.domain.model.User;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {

	private static final Class<?>[] classes = new Class[] { Hotel.class, User.class, Booking.class };

	public static void main(String[] args) throws Exception {
		writeConfigFile("ormlite_config.txt", classes);
	}
}
