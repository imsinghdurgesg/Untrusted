package singh.durgesh.com.applocker.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.services.CallBarring;

public class SplashScreenActivity extends AppCompatActivity
{
    CallBarring mMyBroadcastReceiver;
    private static int SPLASH_TIME_OUT=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //SharedPreference to change Theme dynamically...
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String themeName = sharedPreferences.getString("Theme", null);
        if (themeName != null) {
            if (themeName.equals("Redtheme")) {
                setTheme(R.style.MyMaterialTheme);
            } else {
                setTheme(R.style.MyMaterialThemeGreen);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //Registering The BroadCastReceiver

        //Checking wheather App is Permitted to Read Contacts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && SplashScreenActivity.this.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            //  requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
            ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && SplashScreenActivity.this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        else
        {
            showNext();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        showNext();


    }

    public void showNext()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent homeIntent=new Intent(SplashScreenActivity.this,SetPatternActivity.class);
                startActivity(homeIntent);
                finish();

            }
        },SPLASH_TIME_OUT);

    }
}
