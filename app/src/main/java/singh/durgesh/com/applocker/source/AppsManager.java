package singh.durgesh.com.applocker.source;

/**
 * Created by DSingh on 6/5/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

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

    public AppsManager(Context context) {
        mContext = context;
    }

    // Get a list of installed app
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
            if (!isSystemPackage(resolveInfo)) {
                // Add the non system package to the list
                packageNames.add(checkBoxState);
            }
            String label = getApplicationLabelByPackageName(activityInfo.applicationInfo.packageName);
            checkBoxState.setAppLabel(label);

        }
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

  /*  public ArrayList<CheckBoxState> getBlockAppsList(ArrayList<CheckBoxState> packageNames) {
        this.packageNames = packageNames;
        ArrayList<CheckBoxState> stateofCheckBoxes = new ArrayList<>();

        //getting state of checkboxes
        String packages = msharedPref.getStringData("BlockApps");
//        Log.e("reading here", packages);
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        Type type = new TypeToken<List<CheckBoxState>>() {
        }.getType();
        stateofCheckBoxes = gson.fromJson(packages, type);
        //  msharedPref.getArrayList("BlockApps")''
        if (stateofCheckBoxes != null && stateofCheckBoxes.size() > 0) {
            for (int i = 0; i < stateofCheckBoxes.size(); i++) {
                CheckBoxState checkBoxState = new CheckBoxState();
                checkBoxState.setPackageName(stateofCheckBoxes.get(i).getPackageName());
                checkBoxState.setPosition(stateofCheckBoxes.get(i).getPosition());
                checkBoxState.setSelected(true);
                packageNames.add(checkBoxState);
            }
        }
        Collections.sort(packageNames, new Comparator<CheckBoxState>() {
            @Override
            public int compare(CheckBoxState o1, CheckBoxState o2) {
                return o1.getPackageName().compareToIgnoreCase(o2.getPackageName());
            }
        });

        return packageNames;
//        Log.d("packages to be block", packages);
    }*/

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
            /*Collections.sort(packageNames, new Comparator<CheckBoxState>() {
                @Override
                public int compare(CheckBoxState o1, CheckBoxState o2) {
                    return o1.getAppLabel().compareToIgnoreCase(o2.getAppLabel());
                }
            });*/
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return label;
    }
}
