package mockme.evolvan.com.mockme;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
public class OnMapClick extends AppCompatActivity implements GoogleMap.OnMapClickListener,
        OnMapReadyCallback, GoogleMap.OnMapLongClickListener, View.OnClickListener, GoogleMap.OnMyLocationButtonClickListener {

    public static final int RequestPermissionCode2 = 2;
    private GoogleMap mMap;
    LatLng loc, lc;
    ImageView btnMock, btnStopMock, cntricn, fab, fab1, fab2, fab3, currentlocation,guide;
    double _longitude;
    double _latitude;
    double longitude1;
    double latitude1;
    private float mLatitude;
    private float mLongitude;
    Location l = null;
    LocationManager locationMgr;
    List<String> providers;
    SupportMapFragment mapFragment;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String mypreference = "loginpreference";
    public static final String UserNumnber = "UserNumnber";
    public static final String lattit = "lattit";
    public static final String longi = "longi";
    private final String DefaultUnameValue = "", latDefaultUnameValue = "", longDefaultUnameValue = "";
    private String mocking, latfetch, longfetch;
    private double latstore, longstore;
    private Boolean isFabOpen = false;
    private Animation fab_open, fab_close;
    MarkerOptions markerOptions = new MarkerOptions();
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_map_click);
        setTitle("");
        btnMock = (ImageView) findViewById(R.id.btnMock);
        btnStopMock = (ImageView) findViewById(R.id.btnStopMock);
        cntricn = (ImageView) findViewById(R.id.centericon);
        currentlocation = (ImageView) findViewById(R.id.currentlocation);
        guide=(ImageView) findViewById(R.id.guide);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fab = (ImageView) findViewById(R.id.fab);
        fab1 = (ImageView) findViewById(R.id.fab1);
        fab2 = (ImageView) findViewById(R.id.fab2);
        fab3 = (ImageView) findViewById(R.id.fab3);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        mocking = sharedpreferences.getString(UserNumnber, DefaultUnameValue);
        latfetch = sharedpreferences.getString(lattit, latDefaultUnameValue);
        longfetch = sharedpreferences.getString(longi, longDefaultUnameValue);
        if (latfetch != "" && longfetch != "") {
            latstore = Double.parseDouble(latfetch);
            longstore = Double.parseDouble(longfetch);
            lc = new LatLng(latstore, longstore);
            Log.d("latfetch", latfetch);
            Log.d("lc", String.valueOf(lc));
        } else {
            Toast.makeText(getApplicationContext(), "Mock your location...", Toast.LENGTH_LONG).show();
        }
        btnMock.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                loc = mMap.getCameraPosition().target;
                _latitude = loc.latitude;
                _longitude = loc.longitude;
                String lats = Double.toString(_latitude);
                String longs = Double.toString(_longitude);
                mLatitude = Float.parseFloat(String.valueOf(_latitude));
                mLongitude = Float.parseFloat(String.valueOf(_longitude));
                if (isAllowMockLocation() == true && isMockLocationEnabled()==true) {
                    Intent intent = new Intent(OnMapClick.this, MockLocation.class);
                    intent.putExtra(getString(R.string.latitude), mLatitude);
                    Log.d("lng", "Lat1");
                    intent.putExtra(getString(R.string.longitude), mLongitude);
                    Log.d("lng", "lon1");
                    startService(intent);
                    Toast.makeText(getApplicationContext(), "Mocking enable..", Toast.LENGTH_SHORT).show();
                    cntricn.setVisibility(View.GONE);
                    editor = sharedpreferences.edit();
                    editor.putString(UserNumnber, "mock");
                    editor.putString(lattit, lats);
                    editor.putString(longi, longs);
                    Log.d("latlng1", lats);
                    editor.commit();
                    btnMock.setVisibility(View.GONE);
                    btnStopMock.setVisibility(View.VISIBLE);
                    animation();
                    markerOptions.position(loc);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current1));
                    mMap.addMarker(markerOptions);
                } else {
                    enableMockLocationsInSettings();
                }
            }
        });
        btnStopMock.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                mMap.clear();
                _latitude=0;
                _longitude=0;
                stopMock();
                Toast.makeText(getApplicationContext(), "Mocking disable...", Toast.LENGTH_SHORT).show();
                sharedpreferences = getSharedPreferences(OnMapClick.mypreference, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                btnMock.setVisibility(View.VISIBLE);
                btnStopMock.setVisibility(View.GONE);
                cntricn.setVisibility(View.VISIBLE);
                v.clearAnimation();
            }
        });
        currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                providers = locationMgr.getProviders(true);
                for (int i = 0; i < providers.size(); i++) {
                    l = locationMgr.getLastKnownLocation(providers.get(i));
                    if (l != null) {
                        latitude1 = l.getLatitude();
                        longitude1 = l.getLongitude();
                        break;
                    }
                }
                if(_latitude==0 && _longitude==0)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude1, longitude1), 15.5f), 2000, null);
                else
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(_latitude, _longitude), 15.5f), 2000, null);
            }
        });
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent guide=new Intent(getApplicationContext(),GuideLine.class);
                startActivity(guide);
            }
        });
    }

    public void stopMock() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.getProvider(LocationManager.GPS_PROVIDER) == null) {
                return;
            }
            locationManager.removeUpdates(MockLocation.listener);
            locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, false);
            Log.v("", "fake location disabled");
            if (MockLocation.mTimer != null) {
                MockLocation.mTimer.cancel();
                MockLocation.mTimer = null;
            }
            locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void enableMockLocationsInSettings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Something Wrong");
        alertDialog.setMessage("Permission is not enabled or Mock Location app not select . Do you want to go to settings menu ?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /*Intent i = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                        startActivityForResult(i, RequestPermissionCode2);*/
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                        ComponentName componentName = intent.resolveActivity(getPackageManager());
                        if (componentName == null) {
                            Intent guide=new Intent(getApplicationContext(),GuideLine.class);
                            startActivity(guide);
                        } else {
                            startActivity(intent);
                        }
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==RequestPermissionCode2) {
            //Toast.makeText(getApplicationContext(),"permission granted, Mock Location",Toast.LENGTH_SHORT).show();
            Log.d("Settings", "Settings");
        }
        else {
            Toast.makeText(getApplicationContext(),"permission denied, go to setting",Toast.LENGTH_SHORT).show();
        }
    }*/
    public void animation() {
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(800);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        btnStopMock.startAnimation(mAnimation);
    }

    //map types
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.fab2:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.fab3:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }
    }
    public void animateFAB(){

        if(isFabOpen){
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }

    //map ready
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json1));
            if (!success) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
            //  mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (mocking.equals("mock")) {
            btnStopMock.setVisibility(View.VISIBLE);
            btnMock.setVisibility(View.GONE);
            cntricn.setVisibility(View.GONE);
            markerOptions.position(lc);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current1));
            mMap.addMarker(markerOptions);
        }
    }
    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        //cntricn.setVisibility(View.VISIBLE);
        return false;
    }
    @Override
    public void onMapClick(LatLng point) {

    }
    @Override
    public void onMapLongClick(LatLng point) {}

    public boolean isAllowMockLocation(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager mAppOps = (AppOpsManager) getApplication().getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = getApplication().getApplicationInfo();
            String pkg = getApplication().getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            Class appOpsClass;
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());

                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);

                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException |
                    IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public boolean isMockLocationEnabled()
    {
        boolean isMockLocation = false;
        try
        {
            //if marshmallow
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                AppOpsManager opsManager = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
                isMockLocation = (opsManager.checkOp(AppOpsManager.OPSTR_MOCK_LOCATION, android.os.Process.myUid(), BuildConfig.APPLICATION_ID)== AppOpsManager.MODE_ALLOWED);
            }
            else
            {
                // in marshmallow this will always return true
                isMockLocation = !android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), "mock_location").equals("0");
            }
        }
        catch (Exception e)
        {
            return isMockLocation;
        }

        return isMockLocation;
    }
}