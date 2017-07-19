package app.untrusted.services;

/**
 * Created by DSingh on 6/12/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public  class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast toast=Toast.makeText(context,"broadcast found",Toast.LENGTH_SHORT);
        toast.show();
        Intent serviceIntent = new Intent(context,SecureMyAppsService.class);
        context.startService(serviceIntent);

        //Intent for Call Blocker
        Intent callService = new Intent(context,CallBarring.class);
        context.startService(serviceIntent);

    }
}
