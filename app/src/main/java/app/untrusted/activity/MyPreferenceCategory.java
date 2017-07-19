package app.untrusted.activity;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import app.untrusted.utils.AppSharedPreference;


/**
 * Created by DarshanG on 6/28/2017.
 */

public class MyPreferenceCategory extends PreferenceCategory {

    AppSharedPreference appSharedPreference;

    public MyPreferenceCategory(Context context) {
        super(context);

    }

    public MyPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPreferenceCategory(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(View view) {

        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        appSharedPreference = new AppSharedPreference(getContext());
        String textColor = appSharedPreference.getStringData("Theme");
        if (textColor != null) {
            if (textColor.equals("Redtheme")) {
                titleView.setTextColor(Color.RED);
            } else {
                titleView.setTextColor(Color.parseColor("#4d9933"));
            }
        }
    }

}
