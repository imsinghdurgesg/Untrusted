package singh.durgesh.com.applocker.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.adapter.AppSetAdapter;
import singh.durgesh.com.applocker.adapter.CallSetAdapter;
import singh.durgesh.com.applocker.model.CheckBoxState;
import singh.durgesh.com.applocker.model.Contact;
import singh.durgesh.com.applocker.services.CallBarring;
import singh.durgesh.com.applocker.utils.AppSharedPreference;
import singh.durgesh.com.applocker.utils.CustomTypefaceSpan;

/**
 * Created by RSharma on 7/13/2017.
 */

public class CallDialogue extends DialogFragment
{
    private RecyclerView blockRecView;
    private TextView tvTagblock, noContactblock1,tvTagblock2;
    RelativeLayout relblock1,relblock2;
    private RecyclerView.Adapter callAdapter;
    private Button btGoToTab;
    ImageView imgCloseblock1,imgCloseblock2;
    private RecyclerView.LayoutManager layoutManagerblock;
    public static ArrayList<Contact> blockCallList = null;  //to get the Current Blocked List
    private Toolbar toolbar;
    LinearLayout layoutNoContactblock1;
    LinearLayout layoutWithListblock1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppSharedPreference appSharedPreference = new AppSharedPreference(getActivity());
        //SharedPreference to change Theme dynamically...
        String themeName = appSharedPreference.getStringData("Theme");
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.dialogue_call, container, false);
        imgCloseblock1=(ImageView)v.findViewById(R.id.iv_close_fragment_btn1_block);
        imgCloseblock2=(ImageView)v.findViewById(R.id.iv_close_fragment_btn2_block);
        imgCloseblock1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), PrefsActivity.class);
                startActivity(intent);
                dismiss();


            }
        });

        imgCloseblock2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), PrefsActivity.class);
                startActivity(intent);
                dismiss();


            }
        });

        layoutNoContactblock1 = (LinearLayout) v.findViewById(R.id.lay_contact2_block);
        relblock1=(RelativeLayout)v.findViewById(R.id.rel_1_block);
        relblock2=(RelativeLayout)v.findViewById(R.id.rel_2_block);
        layoutWithListblock1 = (LinearLayout) v.findViewById(R.id.app2_block);
        //setting a style to ToolBar App icon Text
        tvTagblock = (TextView) v.findViewById(R.id.txtprotect_block);
        tvTagblock2= (TextView) v.findViewById(R.id.txtprotect2_block);
        btGoToTab = (Button) v.findViewById(R.id.gototab);
        noContactblock1 = (TextView) v.findViewById(R.id.tv_noprotect_block);
        if (themeName != null)
        {
            if (themeName.equals("Redtheme")) {
                relblock1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                relblock1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
            }
        }

        Typeface fontTool = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.font_toxic));
        Typeface fontText = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.font_roboto));
        SpannableStringBuilder ss1 = new SpannableStringBuilder("Blocked Contacts");
        SpannableStringBuilder ss2 = new SpannableStringBuilder("Currently you don't have Blocked any of your Contacts");


        ss1.setSpan(new CustomTypefaceSpan("", fontText), 0, ss1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss2.setSpan(new CustomTypefaceSpan("", fontText), 0, ss2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        noContactblock1.setText(ss2);

        //if clicked on Button
        btGoToTab.setOnClickListener(new View.OnClickListener() {
            int value = 2;

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), HomeActivity.class);
                myIntent.putExtra("intValue", value);
                startActivity(myIntent);
            }

        });

        //first of all we will get the Current Blocked List from SharedPreferences So that we can set it to the Adapter
        blockCallList = CallBarring.getBlockListFromPref(getActivity());
        if (blockCallList != null && !blockCallList.isEmpty()) {
            //disabling the other Layout as Block List is not empty
            layoutNoContactblock1.setVisibility(LinearLayout.GONE);

            tvTagblock.setText(ss1);


            blockRecView = (RecyclerView) v.findViewById(R.id.recycler_view2_block);
            //Settting Adapter
            callAdapter = new CallSetAdapter(getActivity(),blockCallList);
            layoutManagerblock = new LinearLayoutManager(getActivity());
            blockRecView.setLayoutManager(layoutManagerblock);
            blockRecView.setAdapter(callAdapter);

        } else
        {
            if (themeName != null)
            {
                if (themeName.equals("Redtheme")) {
                    relblock2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    relblock2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                }
            }
            tvTagblock2.setText(ss1);
            layoutWithListblock1.setVisibility(LinearLayout.GONE);

        }
        return  v;
    }



}
