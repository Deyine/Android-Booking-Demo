package org.pullrequest.android.bookingnative.network;

import org.pullrequest.android.bookingnative.C;
import org.pullrequest.android.bookingnative.domain.model.Hotel;
import org.springframework.web.client.RestTemplate;

import com.googlecode.androidannotations.annotations.rest.Accept;
import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Rest;
import com.googlecode.androidannotations.api.rest.MediaType;

@Rest(C.API_URL)
public interface BookingRestApi {
	
	@Get("/hotels")
	@Accept(MediaType.APPLICATION_JSON)
	HotelList getHotels();
	
	@Get("/hotel/{id}")
	@Accept(MediaType.APPLICATION_JSON)
	Hotel getHotel(long id);
	
	void setRestTemplate(RestTemplate restTemplate);
}