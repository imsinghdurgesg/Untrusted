package singh.durgesh.com.applocker.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.model.Contact;
import singh.durgesh.com.applocker.utils.AppSharedPreference;
import singh.durgesh.com.applocker.utils.CustomTypefaceSpan;

/**
 * Created by RSharma on 7/10/2017.
 */

public class CallSetAdapter extends RecyclerView.Adapter<CallSetAdapter.RecyclerViewHolder>
{

    ArrayList<Contact> blockList = new ArrayList<Contact>();
    ArrayList<Contact> blockListFinal1 = new ArrayList<Contact>();
    ArrayList<Contact> blockListTemp = new ArrayList<Contact>();
    ArrayList<Contact> blockListFinal2 = new ArrayList<Contact>();
    Context myContext;
    private String letter;
    private String contNameCap,nameCap;
    ColorGenerator generator = ColorGenerator.MATERIAL;



    //constructor
    public CallSetAdapter(Context context,ArrayList<Contact> listOfContacts)
    {
        blockList=listOfContacts;
        myContext=context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(myContext,view,blockList);
        return recyclerViewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position)
    {
        //preparing the Final List which has to be Updated in Preferences
        blockListFinal1=(ArrayList<Contact>)blockList.clone();
        blockListTemp=(ArrayList<Contact>)blockList.clone();

        //setting/binding the data to their respective Views
        String phNo = blockList.get(position).getCPhone();
        contNameCap = blockList.get(position).getCName();
        nameCap=contNameCap.substring(0, 1).toUpperCase() + contNameCap.substring(1);

        //work regarding getting the First Alphabet and setting it to ImageView
        letter = String.valueOf(blockList.get(position).getCName().charAt(0)).toUpperCase();
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, R.color.check);
        //HERE SETTING THE fONT STYLE to TEXTVIEWS
        Typeface fontText = Typeface.createFromAsset(myContext.getAssets(),myContext.getResources().getString(R.string.font_roboto));
        SpannableStringBuilder ssName = new SpannableStringBuilder(nameCap);
        SpannableStringBuilder ssPhone = new SpannableStringBuilder(phNo);

        ssName.setSpan(new CustomTypefaceSpan("", fontText), 0, ssName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ssPhone.setSpan(new CustomTypefaceSpan("", fontText), 0, ssPhone.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        holder.txt_name.setText(ssName);
        holder.contact_img.setImageDrawable(drawable);
        holder.txt_phone.setText(ssPhone);
        holder.cb.setTag(blockList.get(position).CPhone);
        holder.cb.setChecked(true);

        //Controlling the Functionality of Checking and UnChecking the Functionality of CheckBox\
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Contact objContact1=new Contact();
                Contact objContact2=new Contact();

/*
                if (!blockList.isEmpty())
                {
                    //In order to make a ArrayList consisting of Unique Values only
                    Set<Contact> unique = new HashSet<Contact>();
                    unique.addAll(blockList);
                    blockListFinal.clear();
                    blockListFinal.addAll(unique);

                }

*/
                if(isChecked)
                {
                    int x=getPosition(blockList.get(position));
                    objContact1=blockListTemp.get(x);
                    blockListFinal1.add(objContact1);
                    String name=objContact1.getCName();
                    Toast.makeText(myContext, name+" is Blocked Again", Toast.LENGTH_SHORT).show();

                }
                else if(!isChecked)
                {
                    int x=getPosition(blockList.get(position));
                    objContact2=blockListTemp.get(x);
                    String name=objContact2.getCName();
                    Toast.makeText(myContext, name+" is UnBlocked Now", Toast.LENGTH_SHORT).show();

                    blockListFinal1.remove(objContact2);

                }
                AppSharedPreference appSharedPrefs = new AppSharedPreference(myContext);
                Gson gson = new Gson();
                //Removing the Duplicates
                Set<Contact> unique = new HashSet<Contact>();
                unique.addAll(blockListFinal1);
                blockListFinal2.clear();
                blockListFinal2.addAll(unique);

                String json = gson.toJson(blockListFinal2);
                appSharedPrefs.putStringData("BlockedContacts", json);


            }
        });


    }
    //getting that particular contact whose checkBox has been Unchecked
    public int getPosition(Contact contact)
    {
        int x = 0;
        for (int j = 0; j < blockList.size(); j++) {
            if (blockList.get(j).getCPhone().equals(contact.getCPhone())) {
                x = j;
            }
        }
        return x;

    }

    @Override
    public int getItemCount()
    {
        return blockList.size();
    }

    //RecyclerViewHolder Class
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        Context context;
        ArrayList<Contact> blockList;
        public CardView preCard;
        TextView txt_name, txt_phone;
        RelativeLayout rl;
        ImageView contact_img;
        CheckBox cb;

        //constructor
        public RecyclerViewHolder(Context context, final View itemView,ArrayList<Contact> blockList)
        {
            super(itemView);
            this.context=context;
            this.blockList=blockList;
            //setting other View Items
//            preCard = (CardView) itemView.findViewById(R.id.card_view);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_phone = (TextView) itemView.findViewById(R.id.txt_phone);
        //    rl = (RelativeLayout) itemView.findViewById(R.id.cfull_view);
            contact_img = (ImageView) itemView.findViewById(R.id.contact_letter);
            cb = (CheckBox) itemView.findViewById(R.id.checkBoxBlocked);
        }


    }
}
