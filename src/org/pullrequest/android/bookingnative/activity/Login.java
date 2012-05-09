package org.pullrequest.android.bookingnative.activity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.PreferencesManager;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.UserDao;
import org.pullrequest.android.bookingnative.domain.model.User;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.inject.Inject;

public class Login extends RoboActivity implements OnClickListener {

	@Inject
	private UserDao userDao;
	
	@InjectView(R.id.loginButton)
	private Button loginButton;
	
	@InjectView(R.id.login)
	private EditText login;
	
	@InjectView(R.id.password)
	private EditText password;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		loginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		new LoginTask().execute(login.getText().toString(), password.getText().toString());
	}

	private class LoginTask extends AsyncTask<String, Void, Long> {

		@Override
		protected Long doInBackground(String... params) {
			Map<String, String> loginParams = new HashMap<String, String>();
			loginParams.put("login", params[0]);
			loginParams.put("password", params[1]);
			User user = userDao.findByLogin(params[0]);
			return (user != null) ? user.getId() : -1L;
		}

		@Override
		protected void onPreExecute() {
			// display login progress
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Long userId) {
			if (userId == -1L) {
				Toast.makeText(Login.this, "login failed", 1000).show();
			} else {
				// set logged
				PreferencesManager.getInstance().savePref(Login.this, PreferencesManager.PREF_LOGGED, userId);

				// result ok and back to main activity
				setResult(RESULT_OK);
				Login.this.finish();
			}
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.debug, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_create_demo:
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
			return true;

		case R.id.menu_reset:
			//getHelper().reset();
			return true;
		}
		return false;
	}
}
