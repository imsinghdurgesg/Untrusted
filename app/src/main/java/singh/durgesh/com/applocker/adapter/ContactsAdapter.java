package singh.durgesh.com.applocker.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import singh.durgesh.com.applocker.fragments.CallFragment;
import singh.durgesh.com.applocker.model.Contact;
import singh.durgesh.com.applocker.R;

/**
 * Created by RSharma on 6/21/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.RecyclerViewHolder>
{
    Context context;
    ArrayList<Contact> listOfContacts=new ArrayList<Contact>();
    public static ArrayList<Contact> listOfBContacts=new ArrayList<Contact>();
    private String letter;
    ColorGenerator generator = ColorGenerator.MATERIAL;
    private String contNameCap;
    int color = generator.getColor("#4000ff");


    //constructor
    public ContactsAdapter(Context context, ArrayList<Contact> listOfContacts)
    {
        this.listOfContacts=listOfContacts;
        this.context=context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);

        RecyclerViewHolder recyclerViewHolder=new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        contNameCap=listOfContacts.get(position).getCName();
        //work regarding getting the First Alphabet and setting it to ImageView
        letter=String.valueOf(listOfContacts.get(position).getCName().charAt(0)).toUpperCase();
        TextDrawable drawable=TextDrawable.builder().buildRound(letter,generator.getRandomColor());

        holder.txt_name.setText(contNameCap.substring(0, 1).toUpperCase() + contNameCap.substring(1));
        holder.contact_img.setImageDrawable(drawable);
        holder.txt_phone.setText(listOfContacts.get(position).getCPhone());
        holder.cb.setChecked(listOfContacts.get(position).isBlocked());
        holder.cb.setTag(listOfContacts.get(position).CPhone);

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                 //   holder.rl.setBackgroundColor(holder.itemView.getResources().getColor(R.color.check));
                    listOfBContacts.add(listOfContacts.get(position));
                }
                else if(!isChecked)
                {
               //     holder.rl.setBackgroundColor(holder.itemView.getResources().getColor(R.color.white));
                    listOfBContacts.remove(listOfContacts.get(position));

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return listOfContacts.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_name,txt_phone;
        ImageView contact_img;
        RelativeLayout rl;
        public CardView preCard;
        CheckBox cb;
        //constructor
        public RecyclerViewHolder(final View itemView)
        {
            super(itemView);
            preCard=(CardView)itemView.findViewById(R.id.card_view);
            txt_name=(TextView)itemView.findViewById(R.id.txt_name);
            txt_phone=(TextView)itemView.findViewById(R.id.txt_phone);
            rl=(RelativeLayout)itemView.findViewById(R.id.cfull_view);
            contact_img=(ImageView)itemView.findViewById(R.id.contact_letter);
            cb=(CheckBox)itemView.findViewById(R.id.checkBoxBlocked);
//            contact_img.setOnClickListener(
//                    new View.OnClickListener() {
//                @Override
//                public void onClick(View v)
//                {
//                    final ArrayList<Contact> listOfBContacts = ContactsAdapter.listOfBContacts;
//                }
//            });

        }
    }


}
