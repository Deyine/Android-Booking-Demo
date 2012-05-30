package org.pullrequest.android.bookingnative.activity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.pullrequest.android.bookingnative.BookingPrefs_;
import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.UserDao;
import org.pullrequest.android.bookingnative.domain.model.User;
import org.pullrequest.android.bookingnative.domain.service.BookingService;

import roboguice.activity.RoboActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.login)
@OptionsMenu(R.menu.debug)
@NoTitle
public class Login extends RoboActivity {

	@Pref
	BookingPrefs_ prefs;
	
	@Inject
	private UserDao userDao;

	@Inject
	private BookingService bookingService;

	@ViewById(R.id.loginButton)
	Button loginButton;

	@ViewById(R.id.login)
	EditText login;

	@ViewById(R.id.password)
	EditText password;

	@Click(R.id.loginButton)
	public void loginButtonClick(View v) {
		login(login.getText().toString(), password.getText().toString());
	}

	@Background
	public void login(String login, String password) {
		Map<String, String> loginParams = new HashMap<String, String>();
		loginParams.put("login", login);
		loginParams.put("password", password);
		loginResult(userDao.findByLogin(login));
	}

	@UiThread
	public void loginResult(User user) {
		if (user == null) {
			Toast.makeText(Login.this, "login failed", Toast.LENGTH_LONG).show();
		} else {
			// set logged
			prefs.loggedUserId().put(user.getId());

			// result ok and back to main activity
			setResult(RESULT_OK);
			Login.this.finish();
		}
	}

	@OptionsItem(R.id.menu_create_demo)
	@Background
	public void createDemoUser() {
		User demoUser = new User();
		demoUser.setLogin("demo");
		demoUser.setPassword("demo");
		demoUser.setFirstName("John");
		demoUser.setLastName("Travis");
		try {
			userDao.create(demoUser);
		} catch (SQLException e) {
			Log.w(C.LOG_TAG, "Problem during demo user creation", e);
		}
	}

	@OptionsItem(R.id.menu_reset)
	@Background
	public void resetDb() {
		bookingService.resetDatabase();
	}
}
