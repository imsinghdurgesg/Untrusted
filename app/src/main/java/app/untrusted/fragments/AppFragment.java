package app.untrusted.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import app.untrusted.R;
import app.untrusted.adapter.InstalledAppsAdapter;
import app.untrusted.model.CheckBoxState;
import app.untrusted.model.Contact;
import app.untrusted.services.SecureMyAppsService;
import app.untrusted.utils.FetchData;


public class AppFragment extends BaseFragment implements FetchData.GetList {
    private Context mContext;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ArrayList<CheckBoxState> tempPackage = new ArrayList<>();
    InstalledAppsAdapter mAdapter;
   // ProgressBar pgBar;
    ArrayList<CheckBoxState> appList;
    AlertDialog.Builder builder;
    public AppFragment() {
        // Required empty public constructor
    }

    @Override
    public void getList(ArrayList<?> list)
    {
        if(list!=null)
        {
            tempPackage= (ArrayList<CheckBoxState>) ((ArrayList<CheckBoxState>)list).clone();
            appList.addAll(tempPackage);
            mAdapter.notifyDataSetChanged();
            hideProgressDialog();

            Log.e("Hello","GetList");
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showProgressDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        new FetchData(2, getActivity(), null, this).execute();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_app_list, container, false);
     //   pgBar=(ProgressBar) view.findViewById(R.id.pg_bar);
        mContext = getContext();
        mContext.startService(new Intent(mContext, SecureMyAppsService.class));
        // Get the activity
        // mActivity = AppListActivity.this;

        // Get the widgets reference from XML layout
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecycle_app_List);

        // Define a layout for RecyclerView
/*
        mLayoutManager = new GridLayoutManager(mContext,2);
*/
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        appList=(ArrayList<CheckBoxState>)tempPackage.clone();

        // Initialize a new adapter for RecyclerView
        mAdapter = new InstalledAppsAdapter(mContext, appList);

        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }




}
