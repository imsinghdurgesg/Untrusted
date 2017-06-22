package singh.durgesh.com.applocker.Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;

import singh.durgesh.com.applocker.Adapter.ContactsAdapter;
import singh.durgesh.com.applocker.Models.Contact;
import singh.durgesh.com.applocker.R;

import static android.provider.ContactsContract.DisplayPhoto.CONTENT_URI;

public class CallFragment extends Fragment
{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Contact> contactList;

    int counter;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Cursor cursor,phoneCursor;
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
        // Inflate the layout for this fragment
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        contactList= (ArrayList<Contact>) requestContacts().clone();
       adapter=new ContactsAdapter(getActivity(),contactList);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
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
                contactList.add(contact);

            }
        }
        return contactList;


    }
    private ArrayList<Contact> requestContacts()
    {
       ArrayList<Contact> list1=new ArrayList<Contact>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
          list1= getContacts();
        }
        return  list1;
    }

}
