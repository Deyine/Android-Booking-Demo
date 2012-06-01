package org.pullrequest.android.bookingnative.activity;

import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.actionbar.ActionBarActivity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.view.Menu;
import android.widget.SearchView;

public abstract class SearchableActivity extends ActionBarActivity {

	@Override
    @SuppressLint("NewApi")
    public boolean onCreateOptionsMenu(Menu menu) {
    	// configure search widget
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
			searchView.setIconifiedByDefault(true);
		}

        return super.onCreateOptionsMenu(menu);
    }
}