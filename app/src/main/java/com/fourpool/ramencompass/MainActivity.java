package com.fourpool.ramencompass;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends Activity implements LocationListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CompassView compassView;
    private ImageView image;

    private LocationClient locationClient;
    private Foursquare foursquare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://api.foursquare.com").build();
        foursquare = restAdapter.create(Foursquare.class);

        // Create the LocationRequest object
        final LocationRequest locationRequest = LocationRequest.create();
        // Use high accuracy
        locationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        locationRequest.setInterval(500);
        // Set the fastest update interval to 1 second
        locationRequest.setFastestInterval(100);

        locationClient = new LocationClient(this, new GooglePlayServicesClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                locationClient.requestLocationUpdates(locationRequest, MainActivity.this);
            }

            @Override
            public void onDisconnected() {

            }
        }, new GooglePlayServicesClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                Toast.makeText(MainActivity.this, "failed - " + connectionResult.toString(), Toast.LENGTH_SHORT);
            }
        });


        compassView = (CompassView) findViewById(R.id.compass);
        image = (ImageView) findViewById(R.id.image);
    }

    @Override
    protected void onStart() {
        super.onStart();

        locationClient.connect();
        compassView.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        locationClient.disconnect();
        compassView.disconnect();
    }

    @Override
    public void onLocationChanged(final Location location) {
        String ll = location.getLatitude() + "," + location.getLongitude();
        foursquare.getNearestRamen(ll, new Callback<RamenResponse>() {
            @Override
            public void success(RamenResponse ramenResponse, Response response) {
                RamenResponse.Response.Venue ramenVenue = ramenResponse.response.venues.get(0);
                final String ramenVenueId = ramenVenue.id;
                Location ramenLocation = new Location("foursquare");

//                float esbLat = 40.754977f;
//                float esbLng = -73.942407f;
//                ramenLocation.setLongitude(esbLng);
//                ramenLocation.setLatitude(esbLat);
                ramenLocation.setLongitude(ramenVenue.location.lng);
                ramenLocation.setLatitude(ramenVenue.location.lat);

                compassView.setTargetLocation(location, ramenLocation);

                foursquare.getVenuePhotoUrl(ramenVenue.id, new Callback<VenuePhotosResponse>() {
                    @Override
                    public void success(VenuePhotosResponse venuePhotosResponse, Response response) {
                        Picasso.with(MainActivity.this).load(venuePhotosResponse.response.photos.items.get(0).getUrl()).into(image);
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "http://www.foursquare.com/v/" + ramenVenueId;
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

                Toast.makeText(MainActivity.this, "nearest is " + ramenResponse.response.venues.get(0).name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("mattm", "got here failure" +  error.toString());
                Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT);
            }
        });
    }
}
