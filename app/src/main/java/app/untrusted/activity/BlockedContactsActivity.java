package app.untrusted.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import app.untrusted.R;
import app.untrusted.adapter.CallSetAdapter;
import app.untrusted.model.Contact;
import app.untrusted.services.CallBarring;
import app.untrusted.utils.AppSharedPreference;
import app.untrusted.utils.CustomTypefaceSpan;


public class BlockedContactsActivity extends AppCompatActivity
{
    private RecyclerView blockRecView;
    private TextView tvTagBlock,noContact;
    private RecyclerView.Adapter blockAdapter;
    private Button btGoToTab;
    private RecyclerView.LayoutManager layoutManager;
    public static ArrayList<Contact> blockList = null;  //to get the Current Blocked List
    private Toolbar toolbar;
    LinearLayout layoutNoContact;
    LinearLayout layoutWithList;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
        setContentView(R.layout.activity_blocked_contacts);
        layoutNoContact=(LinearLayout)findViewById(R.id.lay_contact1);
        layoutWithList=(LinearLayout)findViewById(R.id.app1);
        //setting a style to ToolBar App icon Text
        tvTagBlock = (TextView) findViewById(R.id.txtBlock);
        if (themeName != null) {
            if (themeName.equals("Redtheme")) {
              tvTagBlock.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                tvTagBlock.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
            }
        }
        btGoToTab=(Button)findViewById(R.id.gototab1);
        noContact=(TextView) findViewById(R.id.tv_nocontact);
        Typeface fontTool = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_toxic));
        SpannableStringBuilder ss = new SpannableStringBuilder("App Protector");
        Typeface fontText = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_roboto));
        SpannableStringBuilder ss1 = new SpannableStringBuilder("Contacts  You  have  Blocked :");
        SpannableStringBuilder ss2 = new SpannableStringBuilder("Currently you don't have Blocked any of your Contact");


        ss.setSpan(new CustomTypefaceSpan("", fontTool), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss1.setSpan(new CustomTypefaceSpan("", fontText), 0, ss1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss2.setSpan(new CustomTypefaceSpan("", fontText), 0, ss2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(ss);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noContact.setText(ss2);

        //if clicked on Button
        btGoToTab.setOnClickListener(new View.OnClickListener()
        {
            int value=2;
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BlockedContactsActivity.this, HomeActivity.class);
                myIntent.putExtra("intValue", value);
                startActivity(myIntent);
            }

        });

        //first of all we will get the Current Blocked List from SharedPreferences So that we can set it to the Adapter
        blockList= CallBarring.getBlockListFromPref(this);
        if(blockList!=null && !blockList.isEmpty())
        {
            //disabling the other Layout as Block List is not empty
            layoutNoContact.setVisibility(LinearLayout.GONE);

            tvTagBlock.setText(ss1);


            blockRecView = (RecyclerView) findViewById(R.id.recycler_view1);
            //Settting Adapter
            blockAdapter = new CallSetAdapter(this, blockList);
            layoutManager = new LinearLayoutManager(this);
            blockRecView.setLayoutManager(layoutManager);
            blockRecView.setAdapter(blockAdapter);
        }
        else
            layoutWithList.setVisibility(LinearLayout.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, PrefsActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, PrefsActivity.class);
        startActivity(intent);
        this.finish();

    }
}
