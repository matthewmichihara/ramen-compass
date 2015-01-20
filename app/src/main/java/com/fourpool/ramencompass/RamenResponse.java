package com.fourpool.ramencompass;

import java.util.List;

/**
 * Created by mattm on 9/18/14.
 */
public class RamenResponse {
    Response response;

    public static class Response {
        List<Venue> venues;

        public static class Venue {
            String id;
            String name;
            Location location;

            String getPhotoUrl() {
                return "https://api.foursquare.com/v2/venues/" + id + "/photos" +
                        "?client_id=" + Secrets.CLIENT_ID + "&client_secret=" + Secrets.CLIENT_SECRET + "&v=20140918&limit=1";
            }

            public static class Location {
                double lat;
                double lng;
            }
        }
    }
}
