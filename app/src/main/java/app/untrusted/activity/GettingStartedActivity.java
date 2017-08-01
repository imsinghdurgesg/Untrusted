package app.untrusted.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import app.untrusted.R;
import app.untrusted.utils.AppSharedPreference;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import app.untrusted.utils.CustomTypefaceSpan;


public class GettingStartedActivity extends AppCompatActivity {

    AppSharedPreference appSharedPreference;
    private TextView tvApp;
    private TextView tvAppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        appSharedPreference = new AppSharedPreference(this);
        //setting FontStyle
        Typeface fontTool = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_toxic));
        Typeface fontTool1 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_roboto));

        SpannableStringBuilder ss = new SpannableStringBuilder("App Protector");
        SpannableStringBuilder ss1 = new SpannableStringBuilder("Smart,\n" +
                "Application Protector and Call Blocker ");

        ss.setSpan(new CustomTypefaceSpan("", fontTool), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss1.setSpan(new CustomTypefaceSpan("", fontTool1), 0, ss1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        String gettingStartedValue = appSharedPreference.getStringData("gettindStartedvalue");
        if (gettingStartedValue != null) {
            if (gettingStartedValue.equals("gettingStartedActivated")) {
                Intent intent = new Intent(this, LaunchScreenViewPagerActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            appSharedPreference.putStringData("gettindStartedvalue", "gettingStartedActivated");
        }

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_getting_started);

        Button button = (Button)findViewById(R.id.gettinstartedbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              openLauncherViewPager();
            }
        });
        tvApp=(TextView)findViewById(R.id.app);
        tvAppInfo=(TextView)findViewById(R.id.appinfo);
        tvApp.setText(ss);
        tvAppInfo.setText(ss1);

    }

    private void openLauncherViewPager() {
        Intent intent = new Intent(this,LaunchScreenViewPagerActivity.class);
        startActivity(intent);
        finish();
    }

}
