package singh.durgesh.com.applocker.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
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
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        }
    }
}
