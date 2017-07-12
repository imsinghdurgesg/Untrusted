package singh.durgesh.com.applocker.activity;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.services.AdminReceiver;
import singh.durgesh.com.applocker.utils.AppSharedPreference;
import singh.durgesh.com.applocker.utils.CustomTypefaceSpan;


/**
 * Created by DarshanG on 6/27/2017.
 */

public class PrefsActivity extends AppCompatActivity {

    private static int SWITCH_CONSTANT = 5;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        AppSharedPreference appSharedPreference = new AppSharedPreference(this);

        //SharedPreference to change Theme dynamically...
        String themeName = appSharedPreference.getStringData("Theme");
        if (themeName != null) {
            if (themeName.equals("Redtheme")) {
                setTheme(R.style.MyMaterialTheme);
            } else {
                setTheme(R.style.MyMaterialThemeGreen);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_main);
        //setting the Customized ToolBar
        Typeface fontTool = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_toxic));
        SpannableStringBuilder ss = new SpannableStringBuilder("App Protector");
        ss.setSpan(new CustomTypefaceSpan("", fontTool), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(ss);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = new PreferenceScreen();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            //created for the first time
            fragmentTransaction.add(R.id.relativelayout, fragment, "Preference");
            fragmentTransaction.commit();
        } else {
            fragment = getFragmentManager().findFragmentByTag("Preference");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("Lock", SWITCH_CONSTANT);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("Lock", SWITCH_CONSTANT);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.pull_in_from_left, 0);
        this.startActivity(intent, options.toBundle());
        finish();
    }

    public static class PreferenceScreen extends PreferenceFragment {

        Context context;
        AppSharedPreference appSharedPreference;

        public PreferenceScreen() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            this.context = container.getContext();
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onCreate(@Nullable final Bundle savedInstanceState) {

            appSharedPreference = new AppSharedPreference(getActivity().getBaseContext());

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            SwitchPreference switchPreference = (SwitchPreference) findPreference("SwitchTheme");
            SwitchPreference switchPreferencePatternLock = (SwitchPreference) findPreference("SwitchLock");
            SwitchPreference SwitchSecurity = (SwitchPreference) findPreference("SwitchSecurity");

            //Enables Users to chnage Theme
            switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (!((Boolean) newValue)) {
                        appSharedPreference.putStringData("Theme", "Greentheme");
                        getActivity().recreate();
                    } else {
                        appSharedPreference.putStringData("Theme", "Redtheme");
                        getActivity().recreate();
                    }
                    return true;
                }
            });

            //Enables Users to chnage Pattern Lock
            switchPreferencePatternLock.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (!((Boolean) newValue)) {
                        Log.d("DSG", "Change to App Pattern Lock");
                        appSharedPreference.putStringData("Lock", "DSG");
                    } else {
                        SWITCH_CONSTANT = 5;
                        Log.d("DSG", "Change to Phone Default theme");
                        //Open the default App Pattern Lock
                        // checkLock();
                        //  SWITCH_CONSTANT++;
                    }
                    return true;
                }
            });

            SwitchSecurity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ComponentName devAdminReceiver = new ComponentName(context, AdminReceiver.class);
                    DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
                    dpm.isAdminActive(devAdminReceiver);

                    if (!((Boolean) newValue)) {
                        return dpm.isAdminActive(devAdminReceiver);

                    } else {
                        if (!dpm.isAdminActive(devAdminReceiver)) {
                            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, devAdminReceiver);
                            startActivity(intent);
                        }
                    }
                    return true;
                }
            });
        }

        @TargetApi(Build.VERSION_CODES.M)
        private void checkLock() {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            boolean isSecure = keyguardManager.isKeyguardSecure();
            if (isSecure) {
                Intent intent = keyguardManager.createConfirmDeviceCredentialIntent(null, null);
                appSharedPreference.putStringData("Lock", "IS_PHONE_LOCK");
                startActivity(intent);
            } else {
                // no lock screen set, show the new lock needed screen
            }
        }
    }
}
