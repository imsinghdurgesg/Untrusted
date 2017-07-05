package singh.durgesh.com.applocker.utils;

/**
 * Created by DSingh on 6/13/2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

public  class ViewAccessibilityCompat {

    public ViewAccessibilityCompat() {}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void announceForAccessibility(View view, CharSequence announcement) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.announceForAccessibility(announcement);
        } else {
            Context context = view.getContext();
            AccessibilityManager manager = (AccessibilityManager)context.getSystemService(
                    Context.ACCESSIBILITY_SERVICE);
            if (!manager.isEnabled()) {
                return;
            }
            // According to platform_packages_apps/Camera2
            // com.android.camera.util.AccessibilityUtils.java
            AccessibilityEvent event = AccessibilityEvent.obtain(
                    AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
            event.setSource(view);
            event.setClassName(view.getClass().getName());
            event.setPackageName(context.getPackageName());
            event.setEnabled(view.isEnabled());
            event.getText().add(announcement);
            manager.sendAccessibilityEvent(event);
        }
    }
}