package singh.durgesh.com.applocker.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;


import java.util.ArrayList;
import java.util.List;


import singh.durgesh.com.applocker.Fragments.AppFragment;
import singh.durgesh.com.applocker.Fragments.CallFragment;
import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.Utils.CustomTypefaceSpan;

public class HomeActivity extends AppCompatActivity
{

    SpannableString  str =new SpannableString("App-Protector");
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private String tab1str="Protect My Apps";
    private String tab2str="Do Not Disturb";
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //setting a style to ToolBar App icon Text
        Typeface font = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.font_toxic));
        SpannableStringBuilder ss = new SpannableStringBuilder("App Protector");
        ss.setSpan (new CustomTypefaceSpan("", font), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(ss);

//        disabled HomeBack Button
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);




        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if(tab.getPosition()==0)
                {
                    tabLayout.getTabAt(0).setIcon(tabIcons[1]);
                    tabLayout.getTabAt(1).setIcon(tabIcons[2]);

                }
                else if(tab.getPosition()==1)
                {
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

    //method setupTabIcons
    private void setupTabIcons()
    {
        tabLayout.getTabAt(0).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).setIcon(tabIcons[2]);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //in order to give giving  Customized Tab Fonts
        Typeface font = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
        SpannableStringBuilder SS1 = new SpannableStringBuilder(tab1str);
        SpannableStringBuilder SS2 = new SpannableStringBuilder(tab2str);
        SS1.setSpan (new CustomTypefaceSpan("", font), 0, SS1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        SS2.setSpan (new CustomTypefaceSpan("", font), 0, SS2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        adapter.addFragment(new AppFragment(), SS1);
        adapter.addFragment(new CallFragment(),SS2);
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
}
