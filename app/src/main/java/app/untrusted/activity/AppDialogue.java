package app.untrusted.activity;

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

import app.untrusted.R;
import app.untrusted.adapter.AppSetAdapter;
import app.untrusted.model.CheckBoxState;
import app.untrusted.services.CallBarring;
import app.untrusted.utils.AppSharedPreference;
import app.untrusted.utils.CustomTypefaceSpan;

/**
 * Created by RSharma on 7/12/2017.
 */

public class AppDialogue extends DialogFragment {
    private RecyclerView protectRecView;
    private TextView tvTagprotect, noContact1,tvTagprotect2;
    RelativeLayout rel1,rel2;
    private RecyclerView.Adapter protectAdapter;
    private Button btGoToTab1;
    ImageView imgClose1,imgClose2;
    private RecyclerView.LayoutManager layoutManager1;
    public static ArrayList<CheckBoxState> blockAppList = null;  //to get the Current Blocked List
    private Toolbar toolbar;
    LinearLayout layoutNoContact1;
    LinearLayout layoutWithList1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppSharedPreference appSharedPreference = new AppSharedPreference(getActivity());
        //SharedPreference to change Theme dynamically...
        String themeName = appSharedPreference.getStringData("Theme");
          //inflate layout with recycler view
        View v = inflater.inflate(R.layout.dialogue_app, container, false);
        imgClose1=(ImageView)v.findViewById(R.id.iv_close_fragment_btn1);
        imgClose2=(ImageView)v.findViewById(R.id.iv_close_fragment_btn2);
        imgClose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
                startActivity(getActivity().getIntent());
                getActivity().finish();


            }
        });

        imgClose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
                startActivity(getActivity().getIntent());
                getActivity().finish();

            }
        });

        layoutNoContact1 = (LinearLayout) v.findViewById(R.id.lay_contact2);
        rel1=(RelativeLayout)v.findViewById(R.id.rel_1);
        rel2=(RelativeLayout)v.findViewById(R.id.rel_2);
        layoutWithList1 = (LinearLayout) v.findViewById(R.id.app2);
        //setting a style to ToolBar App icon Text
        tvTagprotect = (TextView) v.findViewById(R.id.txtprotect);
        tvTagprotect2 = (TextView) v.findViewById(R.id.txtprotect2);
        btGoToTab1 = (Button) v.findViewById(R.id.gototab2);
        noContact1 = (TextView) v.findViewById(R.id.tv_noprotect);
        if (themeName != null)
        {
            if (themeName.equals("Redtheme")) {
                rel1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                rel1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
            }
        }

        Typeface fontTool = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.font_toxic));
        Typeface fontText = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.font_roboto));
        SpannableStringBuilder ss1 = new SpannableStringBuilder("Protected Applications ");
        SpannableStringBuilder ss2 = new SpannableStringBuilder("Currently you don't have Protected any of your Application");


        ss1.setSpan(new CustomTypefaceSpan("", fontText), 0, ss1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss2.setSpan(new CustomTypefaceSpan("", fontText), 0, ss2.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        noContact1.setText(ss2);

        //if clicked on Button
        btGoToTab1.setOnClickListener(new View.OnClickListener() {
            int value = 1;

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), HomeActivity.class);
                myIntent.putExtra("intValue", value);
                startActivity(myIntent);
            }

        });

        //first of all we will get the Current Blocked List from SharedPreferences So that we can set it to the Adapter
        blockAppList = CallBarring.getProtectListFromPref(getActivity());
        if (blockAppList != null && !blockAppList.isEmpty()) {
            //disabling the other Layout as Block List is not empty
            layoutNoContact1.setVisibility(LinearLayout.GONE);

            tvTagprotect.setText(ss1);


            protectRecView = (RecyclerView) v.findViewById(R.id.recycler_view2);
            //Settting Adapter
            protectAdapter = new AppSetAdapter(getActivity(), blockAppList);
            layoutManager1 = new LinearLayoutManager(getActivity());
            protectRecView.setLayoutManager(layoutManager1);
            protectRecView.setAdapter(protectAdapter);

        } else
        {
            if (themeName != null)
            {
                if (themeName.equals("Redtheme")) {
                    rel2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    rel2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                }
            }
            tvTagprotect2.setText(ss1);
            layoutWithList1.setVisibility(LinearLayout.GONE);

        }
    return  v;
    }


}
