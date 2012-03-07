package org.pullrequest.android.bookingnative.activity;

import java.util.HashMap;
import java.util.Map;

import org.pullrequest.android.bookingnative.PreferencesManager;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.UserDao;
import org.pullrequest.android.bookingnative.domain.model.User;
import org.pullrequest.android.bookingnative.provider.DatabaseHelper;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		EditText login = (EditText) findViewById(R.id.login);
		EditText password = (EditText) findViewById(R.id.password);
		new LoginTask().execute(login.getText().toString(), password.getText().toString());
	}

	private class LoginTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			Map<String, String> loginParams = new HashMap<String, String>();
			loginParams.put("login", params[0]);
			loginParams.put("password", params[1]);
			User user = UserDao.getInstance(Login.this).getByLogin(params[0]);
			return (user != null) ? user.getLogin() : "ko";
		}

		@Override
		protected void onPreExecute() {
			// display login progress
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String login) {
			if (login == null || (login.equalsIgnoreCase("ko"))) {
				Toast.makeText(Login.this, "login failed", 1000).show();
			} else {
				// set logged
				PreferencesManager.getInstance().savePref(Login.this, PreferencesManager.PREF_LOGGED, login);

				// result ok and back to main activity
				setResult(RESULT_OK);
				Login.this.finish();
			}
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.debug_menu, menu);
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
			UserDao.getInstance(Login.this).add(demoUser);
			return true;

		case R.id.menu_reset:
			DatabaseHelper helper = new DatabaseHelper(this);
			helper.reset();
			return true;
		}
		return false;
	}
}
