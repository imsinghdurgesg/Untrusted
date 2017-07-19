package app.untrusted.activity;

import android.app.ActivityOptions;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.untrusted.R;
import app.untrusted.adapter.AppSetAdapter;
import app.untrusted.model.CheckBoxState;
import app.untrusted.services.CallBarring;
import app.untrusted.utils.AppSharedPreference;
import app.untrusted.utils.CustomTypefaceSpan;
public class BlockedAppAcivity extends AppCompatActivity
{
    private RecyclerView protectRecView;
    private TextView tvTagprotect,noContact1;
    private RecyclerView.Adapter protectAdapter;
    private Button btGoToTab1;
    private RecyclerView.LayoutManager layoutManager1;
    public static ArrayList<CheckBoxState> blockAppList = null;  //to get the Current Blocked List
    private Toolbar toolbar;
    LinearLayout layoutNoContact1;
    LinearLayout layoutWithList1;



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
        setContentView(R.layout.activity_blocked_app_acivity);
        layoutNoContact1=(LinearLayout)findViewById(R.id.lay_contact2);
        layoutWithList1=(LinearLayout)findViewById(R.id.app2);
        //setting a style to ToolBar App icon Text
        tvTagprotect = (TextView) findViewById(R.id.txtprotect);
        btGoToTab1=(Button)findViewById(R.id.gototab2);
        noContact1=(TextView) findViewById(R.id.tv_noprotect);
        if (themeName != null) {
            if (themeName.equals("Redtheme")) {
                tvTagprotect.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                tvTagprotect.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
            }
        }

        Typeface fontTool = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_toxic));
        SpannableStringBuilder ss = new SpannableStringBuilder("App Protector");
        Typeface fontText = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_roboto));
        SpannableStringBuilder ss1 = new SpannableStringBuilder("Applications  You  have  Protected :");
        SpannableStringBuilder ss2 = new SpannableStringBuilder("Currently you don't have Protected any of your Application");


        ss.setSpan(new CustomTypefaceSpan("", fontTool), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss1.setSpan(new CustomTypefaceSpan("", fontText), 0, ss1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss2.setSpan(new CustomTypefaceSpan("", fontText), 0, ss2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(ss);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noContact1.setText(ss2);

        //if clicked on Button
        btGoToTab1.setOnClickListener(new View.OnClickListener()
        {
            int value=1;
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BlockedAppAcivity.this, HomeActivity.class);
                myIntent.putExtra("intValue", value);
                startActivity(myIntent);
            }

        });

        //first of all we will get the Current Blocked List from SharedPreferences So that we can set it to the Adapter
        blockAppList= CallBarring.getProtectListFromPref(this);
        if(blockAppList!=null && !blockAppList.isEmpty())
        {
            //disabling the other Layout as Block List is not empty
            layoutNoContact1.setVisibility(LinearLayout.GONE);

            tvTagprotect.setText(ss1);


            protectRecView = (RecyclerView) findViewById(R.id.recycler_view2);
            //Settting Adapter
            protectAdapter = new AppSetAdapter(this,blockAppList);
            layoutManager1 = new LinearLayoutManager(this);
            protectRecView.setLayoutManager(layoutManager1);
            protectRecView.setAdapter(protectAdapter);
        }
        else
            layoutWithList1.setVisibility(LinearLayout.GONE);

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
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, PrefsActivity.class);
        startActivity(intent);
        this.finish();

    }
}
