package singh.durgesh.com.applocker.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import singh.durgesh.com.applocker.Models.Contact;
import singh.durgesh.com.applocker.R;

/**
 * Created by RSharma on 6/21/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.RecyclerViewHolder>
{
    Context context;
    ArrayList<Contact> listOfContacts=new ArrayList<Contact>();

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
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.txt_name.setText(listOfContacts.get(position).getCName());
        holder.txt_phone.setText(listOfContacts.get(position).getCPhone());


    }

    @Override
    public int getItemCount() {
        return listOfContacts.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_name,txt_phone;
        public CardView preCard;
        //constructor
        public RecyclerViewHolder(final View itemView)
        {
            super(itemView);
            preCard=(CardView)itemView.findViewById(R.id.card_view);
            txt_name=(TextView)itemView.findViewById(R.id.txt_name);
            txt_phone=(TextView)itemView.findViewById(R.id.txt_phone);


            //setting OnLongClick Listener
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int p=getLayoutPosition();
                    preCard.setCardBackgroundColor(itemView.getResources().getColor(R.color.tab_background_unselected));
                    return true;// returning true instead of false, works for me
                }
            });

            //setting OnClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                }
            });

        }
    }


}
