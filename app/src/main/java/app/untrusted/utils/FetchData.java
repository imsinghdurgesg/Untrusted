package app.untrusted.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.untrusted.fragments.AppFragment;
import app.untrusted.fragments.CallFragment;
import app.untrusted.model.CheckBoxState;
import app.untrusted.model.Contact;
import app.untrusted.source.AppsManager;

/**
 * Created by RSharma on 7/19/2017.
 */
public class FetchData extends AsyncTask<ArrayList<String>, Void, ArrayList<?>>
{
    Contact contact;
    Cursor cursor;
    int counter;
    int value;
    public GetList getAList;
    private ProgressDialog dialog;
    Context ctx;
    RecyclerView.Adapter adapter;
    ArrayList<Contact> contactList;
    ArrayList<CheckBoxState> packageInstalled = new ArrayList<CheckBoxState>();
    CallFragment callFragment;

    public FetchData(int value, Context context, CallFragment callFragment, AppFragment appFragment)
    {

        this.callFragment = callFragment;
        if(callFragment==null)
        {
            getAList =  (GetList) appFragment;
        }
        if(appFragment==null)
        {
            getAList =  (GetList) callFragment;

        }
        dialog=new ProgressDialog(context);
        ctx = context;
        this.value=value;
    }
    @Override
    protected ArrayList<?> doInBackground(ArrayList<String>... params)
    {
        ArrayList<?> legalList=new ArrayList<>();
        ArrayList<?> packageInstalled=new ArrayList<>();

/*
            Now Filtering the ArrayList Of Contacts as that List may contain some Contacts
            which dont have Contact Number
*/
/*
          for(int j=0;j<illegalList.size();j++)
          {
              if(!(illegalList.get(j).getCPhone().equals("")))
              {
                  legalList.add(illegalList.get(j));
              }
          }
*/

        Collections.sort((ArrayList<Contact>)legalList, new Comparator<Contact>()
        {
            @Override
            public int compare(Contact o1, Contact o2)
            {
                return o1.getCName().compareToIgnoreCase(o2.getCName());
            }
        });
        if(value==1)
        {
            legalList= getContacts();
            return legalList;
        }
        else if(value==2)
        {
            packageInstalled=new AppsManager(ctx).getInstalledPackages();
            return packageInstalled;
        }
        else
            return legalList;



    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        dialog.setMessage("Loading data...");
        dialog.setIndeterminate(true);
      //  dialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<?> list)
    {
        super.onPostExecute(list);
        getAList.getList(list);
     //   dialog.dismiss();


    }

    //this method will return the List of all Contacts
    public ArrayList<Contact> getContacts()
    {
        contact=new Contact();
        contactList=new ArrayList<Contact>();
        String phoneNumber=null;
        ArrayList<String> allContacts = new ArrayList<String>();
        ArrayList<String> phone_numbers=new ArrayList<String>();
        Uri CONTENT_URIL = ContactsContract.Contacts.CONTENT_URI;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String _ID = ContactsContract.Contacts._ID;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        StringBuffer output;
        ContentResolver contentResolver = ctx.getContentResolver();
        cursor = contentResolver.query(CONTENT_URIL, null,null, null, null);
        //iterate every phone in the contact
        if (cursor.getCount() > 0)
        {
            counter = 0;
            while (cursor.moveToNext())
            {
                contact=new Contact();

                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                //     contact.setCName(name);
                //  contact.setCPhone(phone);
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                //   contact.setCName(name.toString());
                if (hasPhoneNumber > 0)
                {
                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext())
                    {
                        contact=new Contact();
                        contact.setCName(name);
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contact.setCPhone(phoneNumber);
                        contactList.add(contact);

                    }

//                    contact.setCPhone(phoneNumber);
//                    contact.setCPhone("");

                    phoneCursor.close();
                }
/*
                else
                {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext())
                    {
                        contact=new Contact();
                        contact.setCName(name);
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contact.setCPhone("");
                        contactList.add(contact);
                    }

                    phoneCursor.close();
                }
*/
            }
        }
        return contactList;


    }
    public interface GetList extends Serializable
    {
        void getList(ArrayList<?> list);
    }

}

