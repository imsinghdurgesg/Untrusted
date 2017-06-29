package singh.durgesh.com.applocker.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Date;

import com.android.internal.telephony.ITelephony;


/**
 * Created by RSharma on 6/28/2017.
 */

public class CallBarring extends BroadcastReceiver
{
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;

    public CallBarring()
    {

    }
    // This String will hold the incoming phone number

    private final String TAG = "CallListener";
  //  private ITelephony telephony = App.getInstance().getTelephony();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int state = 0;
        String status=intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
        if(status.equals(TelephonyManager.EXTRA_STATE_RINGING))
        {
          //  disconnectPhoneItelephony(context);
//            state = TelephonyManager.CALL_STATE_RINGING;
//            onCallStateChanged(context,state);
        }

//        // If, the received action is not a type of "Phone_State", ignore it
//        if (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
//            return;
//
//            // Else, try to do some action
//        else
//        {
//            disconnectPhoneItelephony(context);

            // disconnectPhoneItelephony(context);
//            // Fetch the number of incoming call
//            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//
//            // Check, whether this is a member of "Black listed" phone numbers stored in the database
//           if(number.equals("8699038702"))
//            {
//                // If yes, invoke the method
//                disconnectPhoneItelephony(context);
//                return;
//            }
    }


    // Method to disconnect phone automatically and programmatically
    // Keep this method as it is
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void disconnectPhoneItelephony(Context context)
    {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    //method 2
    public void onCallStateChanged(Context context, int state)
    {
        if(lastState == state){
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();

                Toast.makeText(context, "Incoming Call Ringing" , Toast.LENGTH_SHORT).show();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = new Date();
                    Toast.makeText(context, "Outgoing Call Started" , Toast.LENGTH_SHORT).show();
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    //Ring but no pickup-  a miss
                    Toast.makeText(context, "Ringing but no pickup" + savedNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();
                }
                else if(isIncoming){

                    Toast.makeText(context, "Incoming " + savedNumber + " Call time " + callStartTime  , Toast.LENGTH_SHORT).show();
                }
                else{

                    Toast.makeText(context, "outgoing " + savedNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();

                }

                break;
        }
        lastState = state;
    }

}
