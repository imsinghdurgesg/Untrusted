//package singh.durgesh.com.applocker;
//
//import android.app.Application;
//import android.content.Context;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.android.internal.telephony.ITelephony;
//
//import java.lang.reflect.Method;
//
///**
// * Created by RSharma on 6/28/2017.
// */
//
//public class App extends Application
//{
//    private final String TAG = "App";
//    private static App singleton;
//    private ITelephony telephony = null;
//
//    @Override
//    public final void onCreate() {
//        super.onCreate();
//        singleton = this;
//        telephony = getTeleService(this);
//    }
//
//    private ITelephony getTeleService(Context context) {
//        TelephonyManager tm = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//
//        try {
//            Class<?> c = Class.forName(tm.getClass().getName());
//            Method m = c.getDeclaredMethod("getITelephony");
//            m.setAccessible(true);
//            ITelephony telephony = (ITelephony) m.invoke(tm);
//            return telephony;
//        } catch (Exception e) {
//            Log.e(TAG, "getTeleService: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public static App getInstance() {
//        return singleton;
//    }
//
//    public ITelephony getTelephony() {
//        return telephony;
//    }
//
//}
