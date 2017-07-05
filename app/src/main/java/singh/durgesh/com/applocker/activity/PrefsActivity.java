package singh.durgesh.com.applocker.activity;

<<<<<<< HEAD
import android.annotation.TargetApi;
import android.app.ActivityOptions;
=======
import android.app.Activity;
>>>>>>> 0e5f57362cf700ca7aff7ad68f33a8eea777a673
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.KeyguardManager;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import singh.durgesh.com.applocker.R;


/**
 * Created by DarshanG on 6/27/2017.
 */

public class PrefsActivity extends AppCompatActivity {

    private static int i = 5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //SharedPreference to change Theme dynamically...
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String themeName = sharedPreferences.getString("Theme", null);
        if (themeName != null) {
            if (themeName.equals("Redtheme")) {
                setTheme(R.style.MyMaterialTheme);
            } else {
                setTheme(R.style.MyMaterialThemeGreen);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            intent.putExtra("Lock",i);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("Lock",i);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.pull_in_from_left, 0);
        this.startActivity(intent, options.toBundle());
        finish();
    }

    public static class PreferenceScreen extends PreferenceFragment {

        Context context;

        public PreferenceScreen() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            this.context = container.getContext();
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onCreate(@Nullable final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            SwitchPreference switchPreference = (SwitchPreference) findPreference("SwitchTheme");
            SwitchPreference switchPreferencePatternLock = (SwitchPreference) findPreference("SwitchLock");

            //Enables Users to chnage Theme
            switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (!((Boolean) newValue)) {
                        Log.d("DSG", "Set Green Theme..");
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Theme", "Greentheme");
                        editor.commit();
                        getActivity().recreate();
                    } else {
                        Log.d("DSG", "Set Red Theme...");
                        //
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Theme", "Redtheme");
                        editor.commit();
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
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Lock", "DSG");
                        editor.commit();
                    } else {
                        i = 5;
                        Log.d("DSG", "Change to Phone Default theme");
                        //Open the default App Pattern Lock
                        checkLock();
                        i++;
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
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Lock", "PhoneLock");
                editor.commit();
                startActivity(intent);
            } else {
                // no lock screen set, show the new lock needed screen
            }
        }
    }
}
