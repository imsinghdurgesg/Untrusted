package app.untrusted.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import app.untrusted.R;
import app.untrusted.utils.AppSharedPreference;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class GettingStartedActivity extends AppCompatActivity {

    AppSharedPreference appSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appSharedPreference = new AppSharedPreference(this);

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
    }

    private void openLauncherViewPager() {
        Intent intent = new Intent(this,LaunchScreenViewPagerActivity.class);
        startActivity(intent);
        finish();
    }

}
