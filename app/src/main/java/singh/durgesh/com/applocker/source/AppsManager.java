package singh.durgesh.com.applocker.source;

/**
 * Created by DSingh on 6/5/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.model.CheckBoxState;
import singh.durgesh.com.applocker.utils.AppSharedPreference;


public class AppsManager {
    private Context mContext;
    ArrayList<CheckBoxState> packageNames = new ArrayList<>();
    AppSharedPreference msharedPref;
    public String CURRENT_PACKAGE_NAME="";
    public AppsManager(Context context) {
        mContext = context;
    }

    /**
     * Get a list of installed app
     * @return
     */
    public ArrayList<CheckBoxState> getInstalledPackages() {
        msharedPref = new AppSharedPreference(mContext);
        // Initialize a new Intent which action is main
        Intent intent = new Intent(Intent.ACTION_MAIN, null);

        // Set the newly created intent category to launcher
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // Set the intent flags
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        );

        // Generate a list of ResolveInfo object based on intent filter
        List<ResolveInfo> resolveInfoList = mContext.getPackageManager().queryIntentActivities(intent, 0);

        // Initialize a new ArrayList for holding non system package names
        /* CheckBoxState checkBoxStateForState=new CheckBoxState();
        packageNames.add(apps);*/
        // Loop through the ResolveInfo list
        for (ResolveInfo resolveInfo : resolveInfoList) {
            CheckBoxState checkBoxState = new CheckBoxState();
            // Get the ActivityInfo from current ResolveInfo
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            checkBoxState.setPackageName(activityInfo.applicationInfo.packageName);
            //      checkBoxState.setPosition(stateofCheckBoxes.get().getPosition());

            // If this is not a system app package
            if (!isSystemPackage(resolveInfo) && !checkBoxState.getPackageName().equals(getCurrentPAckage())) {
                // Add the non system package to the list
                packageNames.add(checkBoxState);
            }
            String label = getApplicationLabelByPackageName(activityInfo.applicationInfo.packageName);
            checkBoxState.setAppLabel(label);

        }
        // Sorting App Alphabetiacally with their label
        Collections.sort(packageNames, new Comparator<CheckBoxState>() {
            @Override
            public int compare(CheckBoxState o1, CheckBoxState o2) {
                return o1.getAppLabel().compareToIgnoreCase(o2.getAppLabel());
            }
        });
        //  getBlockAppsList(packageNames);
        return packageNames;
    }

    // Custom method to determine an app is system app
    public boolean isSystemPackage(ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    // Custom method to get application icon by package name
    public Drawable getAppIconByPackageName(String packageName) {
        Drawable icon;
        try {
            icon = mContext.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // Get a default icon
            icon = ContextCompat.getDrawable(mContext, R.drawable.icon_avatat);
        }
        return icon;
    }


    // Custom method to get application label by package name
    public String getApplicationLabelByPackageName(String packageName) {
        CheckBoxState appLabel = new CheckBoxState();
        PackageManager packageManager = mContext.getPackageManager();
        ApplicationInfo applicationInfo;
        String label = "Unknown";
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                label = (String) packageManager.getApplicationLabel(applicationInfo);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return label;
    }
    public String getCurrentPAckage() {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        CURRENT_PACKAGE_NAME = packageInfo.packageName;
        Log.d("CURRENT_PACKAGE_NAME", CURRENT_PACKAGE_NAME);
        return CURRENT_PACKAGE_NAME;
    }
}
