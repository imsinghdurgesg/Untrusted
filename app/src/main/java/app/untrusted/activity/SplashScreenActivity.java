package app.untrusted.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import app.untrusted.R;
import app.untrusted.services.CallBarring;
import app.untrusted.utils.AppSharedPreference;

public class SplashScreenActivity extends BaseActivity {
    CallBarring mMyBroadcastReceiver;
    private static int SPLASH_TIME_OUT = 2000;
    boolean notFirstTimeSplash;
    AppSharedPreference mSared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mSared=new AppSharedPreference(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && SplashScreenActivity.this.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //  requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
            ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            openAppLock();
        }

        //Registering The BroadCastReceiver

        //Checking multiple Permissions at the same time
        //Checking wheather App is Permitted to Read Contacts and Read Phone Status


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        openAppLock();


    }

    public void openAppLock() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreenActivity.this, SetPatternActivity.class);
                startActivity(homeIntent);
                finish();
              boolean firstTime=  mSared.getBooleanData("notFirstTimeSplash",false);
                if (firstTime==false) {
                    checkManuFacturer();
                }
                mSared.putBooleanData("notFirstTimeSplash", true);

            }
        }, SPLASH_TIME_OUT);

    }

    public void showMsg() {
        Toast.makeText(this, "Please enable permission for Untrusted to protect your apps", Toast.LENGTH_LONG).show();
    }

    public void checkManuFacturer() {
        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            Log.d("manufacurer----",manufacturer);
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
               // startActivity(intent);
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
              //  startActivity(intent);
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
               // startActivity(intent);
            }

            List<ResolveInfo> list = getApplicationContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                startActivity(intent);
                showMsg();
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }
   /* public boolean isdrawOverOtherAppPermision(){
        if (Settings.canDrawOverlays(this)) {
            return true;
        }
        else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
        return false;
    }*/
}
