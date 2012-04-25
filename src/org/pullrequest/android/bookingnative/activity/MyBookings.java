package org.pullrequest.android.bookingnative.activity;

import org.pullrequest.android.bookingnative.PreferencesManager;
import org.pullrequest.android.bookingnative.R;
import org.pullrequest.android.bookingnative.domain.dao.BookingDao;
import org.pullrequest.android.bookingnative.domain.model.Booking.Bookings;
import org.pullrequest.android.bookingnative.provider.DatabaseHelper;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MyBookings extends SearchableActivity implements OnClickListener {

	private PreferencesManager preferencesManager = PreferencesManager.getInstance();
	
	public static final int LOGIN_ACTIVITY_CODE = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bookings);
        setTitle("My bookings");
        ListView bookingList = (ListView) findViewById(R.id.bookingList);
        Cursor bookingCursor = BookingDao.getInstance(this).getAll();
        startManagingCursor(bookingCursor);
        bookingList.setAdapter(new BookingListAdapter(this, R.layout.booking_item, bookingCursor, new String[] { Bookings.CHECKIN_DATE }, new int[] { R.id.checkin }));
        
        Button hotelsButton = (Button) findViewById(R.id.buttonHotels);
		Drawable newContentImg = getResources().getDrawable(R.drawable.ic_book_hotel);
		newContentImg.setBounds(0, 0, newContentImg.getIntrinsicWidth(), newContentImg.getIntrinsicHeight());
        hotelsButton.setCompoundDrawables(newContentImg, null, null, null);
        hotelsButton.setOnClickListener(this);
    }
    
    @Override
    protected void onResume() {
    	if (preferencesManager.getStringPref(this, PreferencesManager.PREF_LOGGED) == null) {
			// show login activity
			startActivityForResult(new Intent(this, Login.class), LOGIN_ACTIVITY_CODE);
		}
    	super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bookings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                onSearchRequested();
                break;
                
    		case R.id.menu_logout:
    			PreferencesManager.getInstance().removePref(MyBookings.this, PreferencesManager.PREF_LOGGED);
    			MyBookings.this.finish();
    			MyBookings.this.startActivity(new Intent(MyBookings.this, MyBookings.class));
    			return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onStop() {
    	new DatabaseHelper(this).close();
    	super.onStop();
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonHotels) {
			startActivity(new Intent(this, HotelsList.class));
		}
	}
}