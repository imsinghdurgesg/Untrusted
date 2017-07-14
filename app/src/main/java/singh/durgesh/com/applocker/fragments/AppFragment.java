package singh.durgesh.com.applocker.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import singh.durgesh.com.applocker.adapter.InstalledAppsAdapter;
import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.services.SecureMyAppsService;
import singh.durgesh.com.applocker.source.AppsManager;


public class AppFragment extends BaseFragment {
    private Context mContext;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    InstalledAppsAdapter mAdapter;
    AlertDialog.Builder builder;
    public AppFragment() {
        // Required empty public constructor
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_app_list, container, false);
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

        // Initialize a new adapter for RecyclerView
        mAdapter = new InstalledAppsAdapter(mContext, new AppsManager(mContext).getInstalledPackages());

        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(mAdapter);
        hideProgressDialog();
        return view;
    }




}
