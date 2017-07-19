
package app.untrusted.activity;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.untrusted.R;
import app.untrusted.fragments.AppFragment;
import app.untrusted.fragments.CallFragment;
import app.untrusted.model.CheckBoxState;
import app.untrusted.model.Contact;
import app.untrusted.source.AppsManager;
import app.untrusted.utils.AppSharedPreference;
import app.untrusted.utils.CustomTypefaceSpan;

public class HomeActivity extends BaseActivity {

    SpannableString str = new SpannableString("App-Protector");
    private Toolbar toolbar;
    String themeName;
    public GetList getList;
    public static List<String> blockedNumbers;
    private TabLayout tabLayout;
    private String tab1str = "Protect My Apps";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private String tab2str = "Do Not Disturb";
    AppSharedPreference mSharedPref;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_phone_locked_black_24dp,
            R.drawable.ic_phonelink_lock_black_24dp,
            R.drawable.ic_phone_locked_white_24dp,
            R.drawable.ic_phonelink_lock_white_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getList=(GetList)this;

        mSharedPref = new AppSharedPreference(this);
        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("intValue", 0);

        Intent intent = getIntent();
        int SWITCH_CONSTANT = intent.getIntExtra("Lock", 0);
        if (SWITCH_CONSTANT == 6 || SWITCH_CONSTANT == 5) {
        } else {
            String lockName = mSharedPref.getStringData("Lock");
            if (lockName != null) {
                if (lockName.equals("IS_PHONE_LOCK")) {
                    checkforSecurity();
                    Intent mintent = getIntent();
                    boolean isFromService = mintent.getBooleanExtra("isFromServiceisFromService", false);
                    if (isFromService) {
                        finish();
                    }
                }
            }
        }
        //SharedPreference to change Theme dynamically...
       /* themeName = mSharedPref.getStringData("Theme");
        if (themeName != null) {
            if (themeName.equals("Redtheme")) {
                setTheme(R.style.MyMaterialTheme);
            } else {
                setTheme(R.style.MyMaterialThemeGreen);
            }
        }*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //setting a style to ToolBar App icon Text
        Typeface fontTool = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_toxic));
        SpannableStringBuilder ss = new SpannableStringBuilder("App Protector");

        ss.setSpan(new CustomTypefaceSpan("", fontTool), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(ss);
        mSharedPref.putBooleanData("userCompleteProcess", true);

//        disabled HomeBack Button

        // disabled HomeBack Button
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        hideProgressDialog();
        tabLayout.setupWithViewPager(viewPager);
        if (intValue > 0) {
            if (intValue == 2) {
                viewPager.setCurrentItem(1);
                Toast.makeText(this, "Select the Contacts to block", Toast.LENGTH_LONG).show();

            } else {
                viewPager.setCurrentItem(0);
                Toast.makeText(this, "Select the Applications to Protect", Toast.LENGTH_LONG).show();

            }
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {

                    tabLayout.getTabAt(0).setIcon(tabIcons[1]);
                    tabLayout.getTabAt(1).setIcon(tabIcons[2]);

                } else if (tab.getPosition() == 0) {
                    tabLayout.getTabAt(0).setIcon(tabIcons[3]);
                    tabLayout.getTabAt(1).setIcon(tabIcons[0]);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //method that attaches icons with the Tabs
        setupTabIcons();
    }
/*This will capture the "back" button press event
 and send the user to the first item in the ViewPager.*/


    @TargetApi(Build.VERSION_CODES.M)
    private void checkforSecurity() {

        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        Intent intent = keyguardManager.createConfirmDeviceCredentialIntent(null, null);
        startActivity(intent);
    }


    //method setupTabIcons
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[3]);
        tabLayout.getTabAt(1).setIcon(tabIcons[0]);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startMain);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSetting:
                openPreferenceActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPreferenceActivity() {
        Intent intent = new Intent(this, PrefsActivity.class);
        startActivity(intent);
        //  finish();
        // finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        showProgressDialog();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //in order to give giving  Customized Tab Fonts
        Typeface font = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
        SpannableStringBuilder SS1 = new SpannableStringBuilder(tab1str);
        SpannableStringBuilder SS2 = new SpannableStringBuilder(tab2str);
        SS1.setSpan(new CustomTypefaceSpan("", font), 0, SS1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        SS2.setSpan(new CustomTypefaceSpan("", font), 0, SS2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        adapter.addFragment(new AppFragment(), SS1);
        adapter.addFragment(new CallFragment(), SS2);
        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<SpannableStringBuilder> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, SpannableStringBuilder title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

/*
    @Override
    protected void onResume() {
        super.onResume();
        themeName = mSharedPref.getStringData("Theme");
        if (themeName != null) {
            if (themeName.equals("Redtheme")) {
                setTheme(R.style.MyMaterialTheme);
                setContentView(R.layout.activity_home);
            } else {
                setTheme(R.style.MyMaterialThemeGreen);
                setContentView(R.layout.activity_home);

            }
        }*/


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        Log.d("reqCode", "" + reqCode);
        Log.d("resultCode", "" + resultCode);

        Log.d("Intent", "" + data);

        // Identify our request code
       /* switch (reqCode) {
            case SETTINGS:*/
        if (resultCode == 200) {
            themeName = mSharedPref.getStringData("Theme");
            if (themeName != null) {
                if (themeName.equals("Redtheme")) {
                    setTheme(R.style.MyMaterialTheme);
                    setContentView(R.layout.activity_home);
                } else {
                    setTheme(R.style.MyMaterialThemeGreen);
                    setContentView(R.layout.activity_home);

                }
            }
        }

    }
    //interface
    public interface GetValueFromAsync
    {
        void onReceiveList(ArrayList<?> list);
    }

    //Async Class
    public static class FetchData extends AsyncTask<ArrayList<String>, Void, ArrayList<?>> {
        Contact contact;
        Cursor cursor;
        Context myContext;
        int counter;
        Context mContext;
        int value;
        public HomeActivity.GetValueFromAsync getValueFromAsync;
        ArrayList<Contact> contactList;
        private RecyclerView.Adapter adapter;
        ArrayList<CheckBoxState> packageInstalled = new ArrayList<CheckBoxState>();


        @Override
        protected ArrayList<?> doInBackground(ArrayList<String>... params) {

            ArrayList<?> legalList = new ArrayList<>();
            ArrayList<?> packageInstalled = new ArrayList<>();
            legalList = getContacts();
            packageInstalled = new AppsManager(myContext).getInstalledPackages();

/*
            Now Filtering the ArrayList Of Contacts as that List may contain some Contacts
            which dont have Contact Number
*/
/*
          for(int j=0;j<illegalList.size();j++)
          {
              if(!(illegalList.get(j).getCPhone().equals("")))
              {
                  legalList.add(illegalList.get(j));
              }
          }
*/

            Collections.sort((ArrayList<Contact>) legalList, new Comparator<Contact>() {
                @Override
                public int compare(Contact o1, Contact o2) {
                    return o1.getCName().compareToIgnoreCase(o2.getCName());
                }
            });

            if (value == 1) {
                return legalList;
            } else if (value == 2) {
                return packageInstalled;
            } else
                return legalList;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<?> list) {
            super.onPostExecute(list);
        }
        public ArrayList<Contact> getContacts() {
            contact = new Contact();
            contactList = new ArrayList<Contact>();
            String phoneNumber = null;
            ArrayList<String> allContacts = new ArrayList<String>();
            ArrayList<String> phone_numbers = new ArrayList<String>();
            Uri CONTENT_URIL = ContactsContract.Contacts.CONTENT_URI;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
            String _ID = ContactsContract.Contacts._ID;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            StringBuffer output;
            ContentResolver contentResolver = myContext.getContentResolver();
            cursor = contentResolver.query(CONTENT_URIL, null, null, null, null);
            //iterate every phone in the contact
            if (cursor.getCount() > 0) {
                counter = 0;
                while (cursor.moveToNext()) {
                    contact = new Contact();

                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                    //     contact.setCName(name);
                    //  contact.setCPhone(phone);
                    String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                    //   contact.setCName(name.toString());
                    if (hasPhoneNumber > 0) {
                        //This is to read multiple phone numbers associated with the same contact
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (phoneCursor.moveToNext()) {
                            contact = new Contact();
                            contact.setCName(name);
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            contact.setCPhone(phoneNumber);
                            contactList.add(contact);

                        }

//                    contact.setCPhone(phoneNumber);
//                    contact.setCPhone("");

                        phoneCursor.close();
                    }
/*
                else
                {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext())
                    {
                        contact=new Contact();
                        contact.setCName(name);
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contact.setCPhone("");
                        contactList.add(contact);
                    }

                    phoneCursor.close();
                }
*/
                }
            }
            return contactList;


        }
    }

    //interface to send List
    public interface GetList
    {
        void getList(ArrayList<?> list);
    }




}
