package singh.durgesh.com.applocker.activity;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.services.AdminReceiver;
import singh.durgesh.com.applocker.services.CallBarring;

public class SplashScreenActivity extends BaseActivity {
    CallBarring mMyBroadcastReceiver;
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ComponentName devAdminReceiver = new ComponentName(this, AdminReceiver.class);
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(this.DEVICE_POLICY_SERVICE);
        dpm.isAdminActive(devAdminReceiver);
        if (!dpm.isAdminActive(devAdminReceiver)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    devAdminReceiver);
            startActivity(intent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && SplashScreenActivity.this.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //  requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
            ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE}, 1);
        } else {

            showNext();
        }

        //Registering The BroadCastReceiver

        //Checking multiple Permissions at the same time
        //Checking wheather App is Permitted to Read Contacts and Read Phone Status


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        showNext();


    }

    public void showNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreenActivity.this, SetPatternActivity.class);
                startActivity(homeIntent);
                finish();

            }
        }, SPLASH_TIME_OUT);

    }
}
