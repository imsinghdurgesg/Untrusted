package singh.durgesh.com.applocker.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import com.android.internal.telephony.ITelephony;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import singh.durgesh.com.applocker.fragments.CallFragment;
import singh.durgesh.com.applocker.model.Contact;


/**
 * Created by RSharma on 6/28/2017.
 */

public class CallBarring extends BroadcastReceiver {
    private String bNumber,number, numberToVerify;
    public ArrayList<String> blockNumbers =new ArrayList<String >();
    public ArrayList<Contact> blockList = null;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        blockList=getBlockListFromPref(context);
        //putting all the Blocked Numbers into a list of Blocked Numbers
        for(int j=0;j<blockList.size();j++)
        {
            int length=blockList.get(j).getCPhone().length();
            String num1=blockList.get(j).getCPhone();
            if(length>10)
            {
                bNumber=num1.substring(num1.length()-10);
            }
            else
            {
                bNumber=num1;
            }
            blockNumbers.add(bNumber);
            bNumber=null;  //emptying String number for refilling

        }
        ArrayList<String> cc = blockNumbers;
        Toast.makeText(context, "Hello ...BroadcastReceiver is working fine now", Toast.LENGTH_LONG);
        // If, the received action is not a type of "Phone_State", ignore it
        if (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
            return;

            // Else, try to do some action
        else {
            // Fetch the number of incoming call
            number = intent.getExtras().getString(
                    TelephonyManager.EXTRA_INCOMING_NUMBER);
            numberToVerify = number.substring(number.length() - 10);
            if(blockNumbers.contains(numberToVerify))
            {
                disconnectPhoneItelephony(context);
            }
        }
    }

    private void disconnectPhoneItelephony(Context context) {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Contact> getBlockListFromPref(Context context) {
        SharedPreferences appSharedPrefs = context.getSharedPreferences("BlockedContacts", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("BlockedContacts", "");
        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        blockList = gson.fromJson(json, type);
        return blockList;

    }

}
