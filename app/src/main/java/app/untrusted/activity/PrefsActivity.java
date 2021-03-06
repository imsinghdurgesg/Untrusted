package app.untrusted.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;
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

import app.untrusted.R;
import app.untrusted.services.AdminReceiver;
import app.untrusted.services.CallBarring;
import app.untrusted.utils.AppSharedPreference;
import app.untrusted.utils.CustomTypefaceSpan;

/**
 * Created by DarshanG on 6/27/2017.
 */

public class PrefsActivity extends AppCompatActivity  {

    private Toolbar toolbar;
    static int BLOCKED_CONTACTS = 0;
    static int BLOCKED_APPS = 0;
    Preference appPref;

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

        if (item.getItemId() == android.R.id.home) {
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

  /*  @Override
    public void onFinishDialog(int size) {

    }

    @Override
    public void onFinishAppDialog(int size) {


    }
*/
    public static class PreferenceScreen extends PreferenceFragment {
        CheckBoxPreference SwitchSecurity;
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

            final ComponentName devAdminReceiver = new ComponentName(getActivity().getApplication().getApplicationContext(), AdminReceiver.class);
            final DevicePolicyManager dpm = (DevicePolicyManager) getActivity().getApplication().getApplicationContext().getSystemService(DEVICE_POLICY_SERVICE);

            if (CallBarring.getBlockListFromPref(getActivity()) != null) {
                BLOCKED_CONTACTS = CallBarring.getBlockListFromPref(getActivity()).size();
            }
            if (CallBarring.getProtectListFromPref(getActivity()) != null) {
                BLOCKED_APPS = CallBarring.getProtectListFromPref(getActivity()).size();
            }


            appSharedPreference = new AppSharedPreference(getActivity().getBaseContext());
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            final SwitchPreference switchPreference = (SwitchPreference) findPreference("SwitchTheme");
          //  SwitchSecurity = (CheckBoxPreference) findPreference("SwitchSecurity");

            Preference lockPreference = findPreference("changeLock");
            lockPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    openConfirmActivity();
                    return true;
                }
            });

            SwitchSecurity = (CheckBoxPreference) findPreference("SwitchSecurity");
            Preference appPref = findPreference("app");
            if (BLOCKED_APPS > 0) {
                appPref.setSummary(BLOCKED_APPS + " applications protected");
            } else {
                appPref.setSummary("No application protected yet");
            }
            appPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    AppDialogue mFrag = new AppDialogue();
                    mFrag.show(getFragmentManager(), "frag_apps");
                    mFrag.setCancelable(false);
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
                    CallDialogue mFrag1 = new CallDialogue();
                    mFrag1.show(getFragmentManager(), "frag_calls");
                    mFrag1.setCancelable(false);
                    return true;
                }

            });




        }

        private void openConfirmActivity() {

            Intent intent = new Intent(context, ConfirmPatternActivity.class);
            intent.putExtra("PatternLock", true);
            startActivity(intent);
            getActivity().finish();


        }

        @Override
        public void onResume() {
            super.onResume();
            Log.d("here ------App", "önResume Calleed");
            final ComponentName devAdminReceiver = new ComponentName(getActivity().getApplication().getApplicationContext(), AdminReceiver.class);
            final DevicePolicyManager dpm = (DevicePolicyManager) getActivity().getApplication().getApplicationContext().getSystemService(DEVICE_POLICY_SERVICE);
         //   SwitchSecurity.setChecked(dpm.isAdminActive(devAdminReceiver));
        }

       /* @Override
        public void onFinishAppDialog(int size) {
            Log.d("here ------App", "" + size);
            BLOCKED_APPS = size;
            Preference appPref = findPreference("call");
            if (BLOCKED_APPS > 0) {
                appPref.setSummary(BLOCKED_APPS + " applications protected");
            } else {
                appPref.setSummary("No application protected yet");
            }


        }*/

     /*   public void onFinishDialog(int size) {
            Log.d("here ------Call", "" + size);
            BLOCKED_CONTACTS = size;
            Preference contactPref = findPreference("call");
            if (BLOCKED_CONTACTS > 0) {
                contactPref.setSummary(BLOCKED_CONTACTS + " contacts blocked");
            } else {
                contactPref.setSummary("No contact blocked yet");
            }

        }*/
       /* @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the EditNameDialogListener so we can send events to the host
//              CallDialogue.CountBlockList listener = (CallDialogue.CountBlockList) context;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(context.toString()
                        + " must implement EditNameDialogListener");
            }
        }*/


    }
}
