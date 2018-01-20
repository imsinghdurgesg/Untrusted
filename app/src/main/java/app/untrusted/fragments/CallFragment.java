package app.untrusted.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.untrusted.BuildConfig;
import app.untrusted.R;
import app.untrusted.activity.HomeActivity;
import app.untrusted.adapter.ContactsAdapter;
import app.untrusted.model.Contact;
import app.untrusted.utils.FetchData;


public class CallFragment extends BaseFragment implements FetchData.GetList {
    private RecyclerView recyclerView;
    private CheckBox cb;
    public static ArrayList<Contact> oldBlockedList = null;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Contact> contactList;
    ArrayList<Contact> contactListTemp = new ArrayList<Contact>();
    CheckBox cBox;
    Button permission, reload;
    LinearLayout layoutNoContact;
    LinearLayout layoutNoData;
    private RelativeLayout rootlayout;
    int counter;
    Cursor cursor;
    Contact contact;

    public CallFragment() {
        // Required empty public constructor
        //  new FetchData(1,getActivity()).execute();
        Log.e("Hello", "Constructor");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Hello", "OnCreate");
        showProgressDialog();
    }

    //overriding GetList METHOD
    @Override
    public void getList(ArrayList<?> list) {
        if (list != null && list.size() > 0) {
            layoutNoData.setVisibility(LinearLayout.GONE);
            contactListTemp = (ArrayList<Contact>) ((ArrayList<Contact>) list).clone();
            //Sorting List
            contactList.addAll(contactListTemp);
            Collections.sort((contactList), new Comparator<Contact>() {
                @Override
                public int compare(Contact o1, Contact o2) {
                    return o1.getCName().compareToIgnoreCase(o2.getCName());
                }
            });
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }


        } else {
            layoutNoData.setVisibility(LinearLayout.VISIBLE);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("Hello", "OnCreateView");
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        layoutNoContact = (LinearLayout) view.findViewById(R.id.lay_contact);
        layoutNoData = (LinearLayout) view.findViewById(R.id.lay_no_data);
        rootlayout = (RelativeLayout) view.findViewById(R.id.mainll);
        View parentLayout = view.findViewById(android.R.id.content);
        permission = (Button) view.findViewById(R.id.permission);
        reload = (Button) view.findViewById(R.id.permission_load);
        //      permit= (Button) view.findViewById(R.id.btn_permit);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Showing the SnackBar if User has denied the Access to Contatcs
            // contactList = (ArrayList<Contact>) requestContacts().clone();
            layoutNoData.setVisibility(LinearLayout.GONE);
            permission.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",
                            BuildConfig.APPLICATION_ID, null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            });
            reload.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Adding Contacts ... please wait", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "Contacts Added Succesfully !", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getActivity(), "Please Press Enable first and get the Permissions for getting Contacts !", Toast.LENGTH_LONG).show();
                    }


                }
            });
        } else {
            new FetchData(1, getActivity(), this, null).execute();

            layoutNoData.setVisibility(LinearLayout.GONE);
            layoutNoContact.setVisibility(LinearLayout.GONE);
            //First of all getting all the BlockedContacts list that user has selected last time
            SharedPreferences appSharedPrefs = getActivity().getSharedPreferences("BlockedContacts", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = appSharedPrefs.getString("BlockedContacts", "");
//        Contact mStudentObject = gson.fromJson(json, Contact.class);
            Type type = new TypeToken<ArrayList<Contact>>() {
            }.getType();
            oldBlockedList = gson.fromJson(json, type);
            // Inflate the layout for this fragment
            cBox = (CheckBox) view.findViewById(R.id.checkBoxBlocked);
            //   contactList = (ArrayList<Contact>) requestContacts().clone();
/*
            try {
                contactListTemp.addAll((ArrayList<Contact>)new HomeActivity.FetchData().execute().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
*/
            //   new FetchData(1,getActivity()).execute();


            contactList = (ArrayList<Contact>) contactListTemp.clone();
            cb = (CheckBox) view.findViewById(R.id.checkBoxBlocked);
            adapter = new ContactsAdapter(getActivity(), contactList);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            hideProgressDialog();
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }


    }


//the method which gets all the Contacts from Phone

    public ArrayList<Contact> getContacts() {
        contact = new Contact();
        contactList = new ArrayList<Contact>();
        String phoneNumber = null;
        Uri CONTENT_URIL = ContactsContract.Contacts.CONTENT_URI;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String _ID = ContactsContract.Contacts._ID;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        StringBuffer output;
        ContentResolver contentResolver = getActivity().getContentResolver();
        cursor = contentResolver.query(CONTENT_URIL, null, null, null, null);
        //iterate every phone in the contact
        if (cursor.getCount() > 0) {
            counter = 0;
            while (cursor.moveToNext()) {
                contact = new Contact();

                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                //     contact.setCName(name);
                //  contact.setCPhone(phone);
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                //   contact.setCName(name.toString());
                if (hasPhoneNumber > 0) {
                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (phoneCursor.moveToNext()) {
                        contact = new Contact();
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

   /* private ArrayList<Contact> requestContacts() {
        ArrayList<Contact> illegalList = new ArrayList<Contact>();
        ArrayList<Contact> filteredList = new ArrayList<Contact>();
        ArrayList<Contact> legalList = new ArrayList<Contact>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            legalList = getContacts();

*//*
            Now Filtering the ArrayList Of Contacts as that List may contain some Contacts
            which dont have Contact Number
*//*
*//*
          for(int j=0;j<illegalList.size();j++)
          {
              if(!(illegalList.get(j).getCPhone().equals("")))
              {
                  legalList.add(illegalList.get(j));
              }
          }
*//*
        }
        Collections.sort(legalList, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getCName().compareToIgnoreCase(o2.getCName());
            }
        });
        return legalList;
    }*/


}

