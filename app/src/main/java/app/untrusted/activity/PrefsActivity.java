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

        if (item.getItemId() == android.R.id.home)
        {
//            Intent intent = new Intent(this, HomeActivity.class);
//            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

   /* @Override
/*
    @Override
    public void onFinishDialog(int size) {
        Log.d("here ------",""+size);
        BLOCKED_CONTACTS=size;

    }*/

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
            SwitchSecurity = (CheckBoxPreference) findPreference("SwitchSecurity");

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
                public boolean onPreferenceClick(Preference preference)
                {

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
                public boolean onPreferenceClick(Preference preference)
                {
                    CallDialogue mFrag1 = new CallDialogue();
                    mFrag1.show(getFragmentManager(), "frag_calls");
                    mFrag1.setCancelable(false);
                    return true;
                }

            });


            SwitchSecurity.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    if (!dpm.isAdminActive(devAdminReceiver)) {
                        SwitchSecurity.setChecked(dpm.isAdminActive(devAdminReceiver));
                        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, devAdminReceiver);
                        startActivity(intent);
                        //SwitchSecurity.setChecked(dpm.isAdminActive(devAdminReceiver));
                        return true;
                    } else {

                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                        } else {
                            builder = new AlertDialog.Builder(context);
                            builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                        }
                        builder.setTitle("Security")
                                .setMessage("Are you sure you want to change the permission?")
                                .setCancelable(false)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                        dialog.dismiss();
                                        getActivity().recreate();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return false;
                    }


                }

            });


//                     SwitchSecurity.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//
//                    SwitchSecurity.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                        @Override
//                        public boolean onPreferenceClick(Preference preference) {
//                   /* ComponentName devAdminReceiver = new ComponentName(context, AdminReceiver.class);
//                    DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
//                    dpm.isAdminActive(devAdminReceiver);*/
//
//                            if (!dpm.isAdminActive(devAdminReceiver)) {
//                                SwitchSecurity.setChecked(dpm.isAdminActive(devAdminReceiver));
//                                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, devAdminReceiver);
//                                startActivity(intent);
//                                //SwitchSecurity.setChecked(dpm.isAdminActive(devAdminReceiver));
//                                return true;
//                            } else {
//                      /*  Log.d("called", "called here");
//                        SwitchSecurity.setChecked(dpm.isAdminActive(devAdminReceiver));
//                        Snackbar.make(getView(),"Sorry",Snackbar.LENGTH_SHORT).show();
//                        return false;*/
//                                AlertDialog.Builder builder;
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
//                                } else {
//                                    builder = new AlertDialog.Builder(context);
//                                }
//                                builder.setTitle("Security")
//                                        .setMessage("Are you sure you want to change the permission?")
//                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                // continue with delete
//                                                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
//                                                startActivity(intent);
//                                                dialog.dismiss();
//                                            }
//                                        })
//                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                // do nothing
//                                                dialog.dismiss();
//                                                getActivity().recreate();
//                                            }
//                                        })
//                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                        .show();
//                                return false;
//                            }
//
//
//                        }
//                    });
//                    if (!dpm.isAdminActive(devAdminReceiver)) {
//                        SwitchSecurity.setChecked(dpm.isAdminActive(devAdminReceiver));
//                        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, devAdminReceiver);
//                        startActivity(intent);
//                        //SwitchSecurity.setChecked(dpm.isAdminActive(devAdminReceiver));
//                        return true;
//                    } else {
//                        AlertDialog.Builder builder;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
//                        } else {
//                            builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
//                        }
//                        builder.setTitle("Security")
//                                .setMessage("Please search Device Administrator permission in settings")
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS);
//                                        dialogIntent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, devAdminReceiver);
//                                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(dialogIntent);
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // do nothing
//                                        dialog.dismiss();
//                                        SwitchSecurity.setChecked(dpm.isAdminActive(devAdminReceiver));
//                                    }
//                                })
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
//                        return false;
//                    }
//                }
//            });

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
            final ComponentName devAdminReceiver = new ComponentName(getActivity().getApplication().getApplicationContext(), AdminReceiver.class);
            final DevicePolicyManager dpm = (DevicePolicyManager) getActivity().getApplication().getApplicationContext().getSystemService(DEVICE_POLICY_SERVICE);
            SwitchSecurity.setChecked(dpm.isAdminActive(devAdminReceiver));
        }
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
