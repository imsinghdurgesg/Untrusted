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
import singh.durgesh.com.applocker.services.CallBarring;
import singh.durgesh.com.applocker.services.SecureMyAppsService;
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
        finish();
        return super.onOptionsItemSelected(item);
    }


    public static class PreferenceScreen extends PreferenceFragment {

        Context context;
        AppSharedPreference appSharedPreference;
        int BLOCKED_CONTACTS = 0;
        int BLOCKED_APPS = 0;

        public PreferenceScreen() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            this.context = container.getContext();
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onCreate(@Nullable final Bundle savedInstanceState) {
            if (CallBarring.getBlockListFromPref(getActivity()) != null) {
                BLOCKED_CONTACTS = CallBarring.getBlockListFromPref(getActivity()).size();
            }
            if (CallBarring.getProtectListFromPref(getActivity()) != null) {
                BLOCKED_APPS = CallBarring.getProtectListFromPref(getActivity()).size();
            }

            appSharedPreference = new AppSharedPreference(getActivity().getBaseContext());
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);

            Preference lockPreference = findPreference("changeLock");
            lockPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    openConfirmActivity();
                    return true;
                }
            });

            Preference appPref = (Preference) findPreference("app");
            if (BLOCKED_APPS > 0) {
                appPref.setSummary(BLOCKED_APPS + " applications protected");
            } else {
                appPref.setSummary("No application protected yet");
            }
            appPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(context, BlockedAppAcivity.class);
                    startActivity(intent);
                    return true;
                }
            });


            Preference contactPref = (Preference) findPreference("call");
            if (BLOCKED_CONTACTS > 0) {
                contactPref.setSummary(BLOCKED_CONTACTS + " contacts blocked");
            } else {
                contactPref.setSummary("No contact blocked yet");
            }


            contactPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(context, BlockedContactsActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

          /*  SwitchSecurity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
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
            });*/
        }

        private void openConfirmActivity() {

            Intent intent = new Intent(context, ConfirmPatternActivity.class);
            intent.putExtra("PatternLock", true);
            startActivity(intent);
        }


    }
}
