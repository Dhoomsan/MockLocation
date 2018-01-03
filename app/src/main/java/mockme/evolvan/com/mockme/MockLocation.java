package mockme.evolvan.com.mockme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MockLocation extends IntentService {

    static Timer mTimer;
    public static final String TAG = "FakeLocation";
    static LocationListener listener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.v(TAG, "location changed" + location.toString());
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };
    LocationManager locationManager;
    //0.5 seconds
    private final int delay = 5 * 2;
    //2 seconds
    private final int lastTime = 5 * 50;
    double mLatitude=0;
    double mLongitude=0;

    public MockLocation() {
        super("FakeLocationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mLatitude = (double) extras.getFloat("lat");
            Log.d("lng", "Lat2");
            mLongitude = (double) extras.getFloat("long");
            Log.d("lng", "lon2");
        }
        mockLocation();
        Log.d("lng", "000");
    }

    public void mockLocation() {
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        try {
            Log.d("lng", "-----try");
            locationManager.addTestProvider(LocationManager.GPS_PROVIDER, true, false, false, false, true, false, true, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);

            locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    setFakeLocation(mLatitude, mLongitude);
                    Log.d("lng","-----setFakeLocation"+mLatitude+"--"+mLongitude);
                }
            }, delay, lastTime);
        } catch (SecurityException e) {
            e.printStackTrace();
            //Security Exception - User has not enabled Mock-Locations
            Log.v(TAG, "Mock location is not enabled on your device");
            locationManager=null;
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            //probably the 'unknown provider issue'
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Log.d("lng","ex");
        }
    }

    @SuppressLint("NewApi")
    public void setFakeLocation(double latitude, double longitude) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        location.setLatitude(latitude);
        Log.d("lng","-----mLatitude"+mLatitude);
        location.setLongitude(longitude);
        Log.d("lng","-----mLongitude"+mLongitude);
        location.setAccuracy(16F);
        location.setAltitude(0D);
        location.setTime(System.currentTimeMillis());
        location.setBearing(0F);
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);
        Log.v(TAG, "fake location enabled");
    }

}