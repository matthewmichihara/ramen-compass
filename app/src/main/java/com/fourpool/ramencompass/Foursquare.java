package com.fourpool.ramencompass;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface Foursquare {

    @GET("/v2/venues/search?client_id=" + Secrets.CLIENT_ID + "&client_secret=" + Secrets.CLIENT_SECRET + "&v=20140918&query=ramen&limit=1")
    void getNearestRamen(@Query("ll") String ll, Callback<RamenResponse> cb);

    @GET("/v2/venues/{venueId}/photos?client_id=" + Secrets.CLIENT_ID + "&client_secret=" + Secrets.CLIENT_SECRET + "&v=20140918&limit=1")
    void getVenuePhotoUrl(@Path("venueId") String venueId, Callback<VenuePhotosResponse> cb);
}
