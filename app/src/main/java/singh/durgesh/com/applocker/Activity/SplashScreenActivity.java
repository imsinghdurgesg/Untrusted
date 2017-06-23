package singh.durgesh.com.applocker.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import singh.durgesh.com.applocker.R;

public class SplashScreenActivity extends AppCompatActivity
{
    private static int SPLASH_TIME_OUT=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Intent homeIntent=new Intent(SplashScreenActivity.this,SetPatternActivity.class);
                startActivity(homeIntent);
                //Checking wheather App is Permitted to Read Contacts
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && SplashScreenActivity.this.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                {
                    //  requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
                    ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                }

                finish();

            }
        },SPLASH_TIME_OUT);

    }
}
