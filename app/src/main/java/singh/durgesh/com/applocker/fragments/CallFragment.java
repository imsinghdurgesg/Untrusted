package singh.durgesh.com.applocker.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import singh.durgesh.com.applocker.adapter.ContactsAdapter;
import singh.durgesh.com.applocker.model.Contact;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.utils.CustomTypefaceSpan;

public class CallFragment extends BaseFragment
{
    private RecyclerView recyclerView;
    private CheckBox cb;
    public static ArrayList<Contact> oldBlockedList=null;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Contact> contactList;
    CheckBox cBox;
    TextView txt_name,txt_phone;

    int counter;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Cursor cursor;
    Contact contact;
    public CallFragment()
    {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_call, container, false);



        //First of all getting all the BlockedContacts list that user has selected last time
        SharedPreferences appSharedPrefs = getActivity().getSharedPreferences("BlockedContacts", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("BlockedContacts", "");
//        Contact mStudentObject = gson.fromJson(json, Contact.class);
        Type type = new TypeToken<ArrayList<Contact>>() {}.getType();
       oldBlockedList = gson.fromJson(json, type);
        // Inflate the layout for this fragment
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        cBox=(CheckBox)view.findViewById(R.id.checkBoxBlocked);
        contactList= (ArrayList<Contact>) requestContacts().clone();
        cb=(CheckBox)view.findViewById(R.id.checkBoxBlocked);
       adapter=new ContactsAdapter(getActivity(),contactList);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        return view;
    }




    //the method which gets all the Contacts from Phone

    public ArrayList<Contact> getContacts()
    {
        contact=new Contact();
        contactList=new ArrayList<Contact>();
        String phoneNumber=null;
        ArrayList<String> phone_numbers=new ArrayList<String>();
        Uri CONTENT_URIL = ContactsContract.Contacts.CONTENT_URI;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String _ID = ContactsContract.Contacts._ID;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        StringBuffer output;
        ContentResolver contentResolver = getActivity().getContentResolver();
        cursor = contentResolver.query(CONTENT_URIL, null,null, null, null);
        //iterate every phone in the contact
        if (cursor.getCount() > 0)
        {
            counter = 0;
            while (cursor.moveToNext())
            {
                contact=new Contact();

                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                contact.setCName(name);
                //  contact.setCPhone(phone);
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                contact.setCName(name.toString());
                if (hasPhoneNumber > 0)
                {
                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext())
                    {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

                    }

                    contact.setCPhone(phoneNumber);
                    phoneCursor.close();
                }
                else
                {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext())
                    {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

                    }

                    contact.setCPhone("");
                    phoneCursor.close();
                }
                contactList.add(contact);

            }
        }
        return contactList;


    }
    private ArrayList<Contact> requestContacts()
    {
        ArrayList<Contact> illegalList=new ArrayList<Contact>();
        ArrayList<Contact> filteredList=new ArrayList<Contact>();
        ArrayList<Contact> legalList=new ArrayList<Contact>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
        } else
        {
            illegalList= getContacts();

/*
            Now Filtering the ArrayList Of Contacts as that List may contain some Contacts
            which dont have Contact Number
*/
          for(int j=0;j<illegalList.size();j++)
          {
              if(!(illegalList.get(j).getCPhone().equals("")))
              {
                  legalList.add(illegalList.get(j));
              }
          }
        }
         Collections.sort(legalList, new Comparator<Contact>(){
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getCName().compareToIgnoreCase(o2.getCName());
            }
        });
        return  legalList;
    }


}
