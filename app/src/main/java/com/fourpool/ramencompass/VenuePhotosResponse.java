package com.fourpool.ramencompass;

import java.util.List;

/**
 * Created by mattm on 9/19/14.
 */
public class VenuePhotosResponse {
    Response response;
    public static class Response {
        Photos photos;
        public static class Photos {
            List<Photo> items;
            public static class Photo {
                String prefix;
                String suffix;
                int width;
                int height;

                public String getUrl() {
                    return prefix + width + "x" + height + suffix;
                }
            }
        }
    }
}
