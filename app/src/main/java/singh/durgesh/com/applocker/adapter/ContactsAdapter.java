package singh.durgesh.com.applocker.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import singh.durgesh.com.applocker.fragments.CallFragment;
import singh.durgesh.com.applocker.model.Contact;
import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.utils.AppSharedPreference;
import singh.durgesh.com.applocker.utils.CustomTypefaceSpan;

/**
 * Created by RSharma on 6/21/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.RecyclerViewHolder> {
    Context context;
    List<Contact> listOfContacts = new ArrayList<Contact>();
    ArrayList<Contact> oldBlockedList = new ArrayList<Contact>();
    ArrayList<String> blockedNumbers = new ArrayList<String>();
    List<Contact> listOfBContacts = new ArrayList<>();
    private String letter;
    ColorGenerator generator = ColorGenerator.MATERIAL;
    private String contNameCap,nameCap;
    int color = generator.getColor("#4000ff");


    //constructor
    public ContactsAdapter(Context context, ArrayList<Contact> listOfContacts) {
        this.listOfContacts = listOfContacts;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(context,view, oldBlockedList, listOfContacts);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        //Getting the OldBlockList
       // SharedPreferences appSharedPrefs = context.getSharedPreferences("BlockedContacts", Context.MODE_PRIVATE);
        final AppSharedPreference appSharedPrefs = new AppSharedPreference(context);
        Gson gson = new Gson();
        String json = appSharedPrefs.getStringData("BlockedContacts");
//        Contact mStudentObject = gson.fromJson(json, Contact.class);
        Type type = new TypeToken<ArrayList<Contact>>() {}.getType();
        oldBlockedList = gson.fromJson(json, type);
        if(oldBlockedList==null)
        {
            oldBlockedList=new ArrayList<Contact>();
        }

        String phNo = listOfContacts.get(position).getCPhone();
        contNameCap = listOfContacts.get(position).getCName();
        nameCap=contNameCap.substring(0, 1).toUpperCase() + contNameCap.substring(1);

        //work regarding getting the First Alphabet and setting it to ImageView
        letter = String.valueOf(listOfContacts.get(position).getCName().charAt(0)).toUpperCase();
        TextDrawable drawable = TextDrawable.builder().buildRound(letter,R.color.check);
        //HERE SETTING THE fONT STYLE to TEXTVIEWS
        Typeface fontText = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.font_roboto));
        SpannableStringBuilder ssName = new SpannableStringBuilder(nameCap);
        SpannableStringBuilder ssPhone = new SpannableStringBuilder(phNo);

        ssName.setSpan(new CustomTypefaceSpan("", fontText), 0, ssName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ssPhone.setSpan(new CustomTypefaceSpan("", fontText), 0, ssPhone.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        holder.txt_name.setText(ssName);
        holder.contact_img.setImageDrawable(drawable);
        holder.txt_phone.setText(ssPhone);
        holder.cb.setTag(listOfContacts.get(position).CPhone);

//        this code will first check wheather the current row already comes under blockList(old)
//         ..if yes,it will check that corresponding row
        if (oldBlockedList.isEmpty())
        {
            //Toast.makeText(context, "No Number is Blocked Yet", Toast.LENGTH_SHORT).show();

        } else
            {
                holder.cb.setOnCheckedChangeListener(null);
            if (containsPhoneNo(phNo)) {
                holder.cb.setChecked(true);
            } else {
                holder.cb.setChecked(false);
            }
        }
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            Contact objContact=new Contact();

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!oldBlockedList.isEmpty())
                {
                    //In order to make a ArrayList consisting of Unique Values only
                    Set<Contact> unique = new HashSet<Contact>();
                    unique.addAll(oldBlockedList);
                    listOfBContacts.clear();
                    listOfBContacts.addAll(unique);

                }
                if (isChecked)
                {
                    //   holder.rl.setBackgroundColor(holder.itemView.getResources().getColor(R.color.check));
                    listOfBContacts.add(listOfContacts.get(position));                }
                if (!isChecked)
                {
                    //     holder.rl.setBackgroundColor(holder.itemView.getResources().getColor(R.color.white));
                    int x=getPosition(listOfContacts.get(position));
                   listOfBContacts.remove(x);

                    //listOfBContacts.remove(c1);
                //    Log.d("RK", "log boolean " + b);
                }
                Gson gson = new Gson();
                String json = gson.toJson(listOfBContacts);
                appSharedPrefs.putStringData("BlockedContacts", json);
                notifyDataSetChanged();


            }
        });



    }


    protected boolean containsPhoneNo(String phoneNo) {
        for (int i = 0; i < oldBlockedList.size(); i++) {
            if (oldBlockedList.get(i).getCPhone().equals(phoneNo))
            {
                return true;
            }
        }

        return false;
    }

    public int getPosition(Contact contact) {
        int x = 0;
        for (int j = 0; j < listOfBContacts.size(); j++) {
            if (listOfBContacts.get(j).getCPhone().equals(contact.getCPhone())) {
                x = j;
            }
        }
        return x;

    }

    @Override
    public int getItemCount() {
        return listOfContacts.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_phone;
        ImageView contact_img;
        List<Contact> listOfContacts1 = new ArrayList<Contact>();
        ArrayList<Contact> oldBlockedList1 = new ArrayList<Contact>();
        List<Contact> listOfBContacts1 = new ArrayList<>();
        RelativeLayout rl;
         LinearLayout preCard;
        Context context;
        CheckBox cb;

        //constructor
        public RecyclerViewHolder(final Context context, final View itemView, final ArrayList<Contact> oldBlockedList1, final List<Contact> listOfContacts1) {
            super(itemView);
            this.context=context;
            this.oldBlockedList1 = oldBlockedList1;
            this.listOfContacts1 = listOfContacts1;
            preCard = (LinearLayout) itemView.findViewById(R.id.card_view);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_phone = (TextView) itemView.findViewById(R.id.txt_phone);
          //  rl = (RelativeLayout) itemView.findViewById(R.id.cfull_view);
            contact_img = (ImageView) itemView.findViewById(R.id.contact_letter);
            cb = (CheckBox) itemView.findViewById(R.id.checkBoxBlocked);


        }

    }


}
