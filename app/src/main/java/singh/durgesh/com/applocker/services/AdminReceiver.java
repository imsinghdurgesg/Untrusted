package singh.durgesh.com.applocker.services;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by DSingh on 7/10/2017.
 */

public class AdminReceiver extends DeviceAdminReceiver {
    /**
     *
     * @param context
     * @param intent get called when user try to uncheck as administrator
     * @return dialog of charsequence
     */
    @Override
    public CharSequence onDisableRequested(final Context context, Intent intent) {
        DevicePolicyManager deviceManger = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        deviceManger.lockNow();
        Log.i(TAG, "DEVICE ADMINISTRATION DISABLE REQUESTED & LOCKED PHONE");
        return "Do you want to remove uninstallation security ";
    }

}
