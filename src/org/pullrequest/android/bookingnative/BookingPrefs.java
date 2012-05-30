package org.pullrequest.android.bookingnative;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultLong;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref.Scope;

@SharedPref(value=Scope.UNIQUE)
public interface BookingPrefs {
	
	@DefaultLong(-1L)
	long loggedUserId();
}
