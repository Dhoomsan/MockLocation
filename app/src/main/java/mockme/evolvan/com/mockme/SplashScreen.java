package mockme.evolvan.com.mockme;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ScrollingTabContainerView;
import android.util.Log;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission_group.CAMERA;

public class SplashScreen extends AppCompatActivity {
    // Splash screen timer
    public static final int RequestPermissionCode = 1;
    public static final int RequestPermissionCode1 = 2;
    private static int SPLASH_TIME_OUT = 2000;
    ImageView splash;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String mypreference = "loginpreference";
    public static final String UserNumnber = "UserNumnber";
    private final String DefaultUnameValue = "";
    private String mocking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        mocking = sharedpreferences.getString(UserNumnber, DefaultUnameValue);
        setContentView(R.layout.activity_splash_screen);
            if (checkPermission()) {
                interneteconnection();
            } else {

                requestPermission();
            }

    }

    public void checkMocking()
    {
        if (mocking.equals("mock")) {
            Intent home = new Intent(getApplication(), OnMapClick.class);
            startActivity(home);
        } else {
            splash = (ImageView) findViewById(R.id.imgLogo);
            ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 1f, ScaleAnimation.RELATIVE_TO_SELF, 1f);
            scale.setDuration(500);
            scale.setInterpolator(new OvershootInterpolator());
            splash.startAnimation(scale);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, OnMapClick.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
    public void interneteconnection() {
        if(checkInternetConenction()==true)
        {
            checkMocking();
        }
        /*else
        {
            createNetErrorDialog();
        }*/
    }
    public boolean checkPermission() {
        int FINE_LOCATION = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        return FINE_LOCATION == PackageManager.PERMISSION_GRANTED ;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(SplashScreen.this, new String[]
                {
                        ACCESS_FINE_LOCATION
                }, RequestPermissionCode);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean FINE_LOCATION = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (FINE_LOCATION) {
                        //Toast.makeText(SplashScreen.this, "Permission Granted", Toast.LENGTH_LONG).show();
                        Intent acti=new Intent(getApplicationContext(),SplashScreen.class);
                        startActivity(acti);
                                Log.d("Settings", "Settings");
                    }
                    else {
                        Toast.makeText(SplashScreen.this,"Permission Denied",Toast.LENGTH_LONG).show();
                        Intent acti=new Intent(getApplicationContext(),SplashScreen.class);
                        startActivity(acti);
                    }
                }
                break;
        }
    }
    private boolean checkInternetConenction() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            createNetErrorDialog();
            return false;
        }
    }
    protected void createNetErrorDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("internet connection is slow or not Connected for this app. Please turn on mobile network or Wi-Fi in Settings.")
                .setTitle("Unable to connect")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivityForResult(i, RequestPermissionCode1);
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                interneteconnection();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==RequestPermissionCode1)
        {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent acti=new Intent(getApplicationContext(),SplashScreen.class);
                        startActivity(acti);
                        Log.d("Settings", "Settings");
                    }
                }, 2000);

        }
    }
}
