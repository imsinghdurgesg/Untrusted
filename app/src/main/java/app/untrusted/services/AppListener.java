package app.untrusted.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import static android.content.ContentValues.TAG;

/**
 * Created by DSingh on 7/13/2017.
 */

public class AppListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        // TODO Auto-generated method stub
        Log.v(TAG, "there is a broadcast");
    }
}
