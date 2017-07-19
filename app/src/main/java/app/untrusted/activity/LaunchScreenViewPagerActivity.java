package app.untrusted.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import app.untrusted.R;
import app.untrusted.adapter.ViewPagerAdapter;
import app.untrusted.utils.AppSharedPreference;


public class LaunchScreenViewPagerActivity extends AppCompatActivity {

    AppSharedPreference appSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appSharedPreference = new AppSharedPreference(this);

        String launcherName = appSharedPreference.getStringData("LaunchValue");
        if (launcherName != null) {
            if (launcherName.equals("LauncherActivated")) {
                Intent intent = new Intent(this, SplashScreenActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            appSharedPreference.putStringData("LaunchValue", "LauncherActivated");
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerlaunchscreen);
        viewPager.setAdapter(new ViewPagerAdapter(this));
    }
}
