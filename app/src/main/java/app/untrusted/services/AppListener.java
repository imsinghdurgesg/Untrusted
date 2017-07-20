package app.untrusted.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.untrusted.model.CheckBoxState;
import app.untrusted.utils.AppSharedPreference;

import static android.content.ContentValues.TAG;

/**
 * Created by DSingh on 7/13/2017.
 */

public class AppListener extends BroadcastReceiver {
    ArrayList<CheckBoxState> blockApps = new ArrayList<>();
    Context mcOntext;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.v(TAG, "there is a broadcast");
        mcOntext = context;
        Uri uri = intent.getData();
        String pkg = uri != null ? uri.getSchemeSpecificPart() : null;
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            if (getBlockApp(pkg)) {
                for (int position = 0; position < blockApps.size(); position++) {
                    if (blockApps.get(position).getPackageName().equals(pkg)) {
                        blockApps.remove(position);
                    }
                    AppSharedPreference mShared = new AppSharedPreference(context);
                    Gson gson = new Gson();
                    String json = gson.toJson(blockApps);
                    mShared.putStringData("BlockApps", json);

                }
            }
        }
    }

    public boolean getBlockApp(String currentApp) {
        AppSharedPreference mSharedPref = new AppSharedPreference(mcOntext);
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

}

