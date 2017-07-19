package app.untrusted.services;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.untrusted.activity.ConfirmPatternActivity;
import app.untrusted.model.CheckBoxState;
import app.untrusted.utils.AppSharedPreference;


/**
 * Created by DSingh on 6/6/2017.
 */

/**
 * Created by DSingh on 6/6/2017.
 */

public class SecureMyAppsService extends Service {
    String CURRENT_PACKAGE_NAME;
    // public static SecureMyAppsService instance;
    public static String blockedPackage = "";
    public static String foregroundPackage = "";
    ArrayList<CheckBoxState> blockApps = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Starting a service which runs infinitely.
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.d("SecureMyAppsService--->", "Service started");
        scheduleMethod();
        return START_STICKY;
    }

    /**
     * background thread which invokes after every 1000 mili seconds
     */
    private void scheduleMethod() {
        ScheduledExecutorService scheduler = Executors
                .newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                getLollipopOrAboveFGAppPackageName();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * In this method we are getting foreground package name and blocking the selected apps accordingly.
     */
    private void getLollipopOrAboveFGAppPackageName() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                    time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(),
                            usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()){
                    String currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();

                    if (currentApp.equals(getMyAppPAckage()) || currentApp.equals(blockedPackage)){
                        return;
                    }else{
                        blockedPackage = "";
                    }

                    if (getBlockApp(currentApp)){
                        foregroundPackage = currentApp;
                        Intent mIntent = new Intent(getApplicationContext(), ConfirmPatternActivity.class);
                        mIntent.putExtra("isFromService", true);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getApplication().getApplicationContext().startActivity(mIntent);
                    }
                }
            }
        } else {
            ActivityManager am = (ActivityManager) getBaseContext().getSystemService(ACTIVITY_SERVICE);
            String currentApp = am.getRunningTasks(1).get(0).topActivity.getPackageName();
            if (currentApp.equals(getMyAppPAckage()) && currentApp.equals(blockedPackage)) {
                return;
            } else {
                blockedPackage = "";
            }
            Log.e("blockedPackage", "pehle " + blockedPackage + "--  current oackage" + currentApp);
            if (getBlockApp(currentApp)) {
                blockedPackage = currentApp;
                Log.e("blockedPackage", "baad  " + blockedPackage + "--  current oackage" + currentApp);
                Intent mIntent = new Intent(getApplicationContext(), ConfirmPatternActivity.class);
                mIntent.putExtra("isFromService", true);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplication().getApplicationContext().startActivity(mIntent);
            }
        }

    }


    /**
     * retriving saved CheckApp List from the shared preference
     *
     * @param currentApp to compare with saved list
     * @return true if current app is in Saved List and vice-versa
     */
    public boolean getBlockApp(String currentApp) {
        AppSharedPreference mSharedPref = new AppSharedPreference(getApplicationContext());
        String packages = mSharedPref.getStringData("BlockApps");
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        Type type = new TypeToken<List<CheckBoxState>>() {
        }.getType();
        blockApps = gson.fromJson(packages, type);
        for (int position = 0; position < blockApps.size(); position++) {
            if (blockApps.get(position).getPackageName().equals(currentApp)) {
                return true;
            }

        }
        return false;
    }


    /*public static void stop() {
        if (instance != null) {
            instance.stopSelf();
        }
    }*/

    /**
     * here getting current package name from package manager.
     *
     * @return
     */
    public String getMyAppPAckage() {
        PackageManager pm = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        CURRENT_PACKAGE_NAME = packageInfo.packageName;
        Log.d("CURRENT_PACKAGE_NAME", CURRENT_PACKAGE_NAME);
        return CURRENT_PACKAGE_NAME;
    }

    /**
     * Again starting the service while it is destroying
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SecureMyAppsService", "Service destroyed");
        startService(new Intent(getApplicationContext(), SecureMyAppsService.class));
    }

}