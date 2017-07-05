package singh.durgesh.com.applocker.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by RSharma on 7/4/2017.
 */

public class CallReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "Hello ...BroadcastReceiver is working fine now" , Toast.LENGTH_LONG);


    }
}
