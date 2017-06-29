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

import static singh.durgesh.com.applocker.activity.HomeActivity.blockedNumbers;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_app_list, container, false);
        mContext = getContext();
        mContext.startService(new Intent(mContext, SecureMyAppsService.class));
        // Get the activity
        // mActivity = AppListActivity.this;

        // Get the widgets reference from XML layout
/*
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
*/
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
    public void showLoading(){
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();*/
        ProgressDialog dialog = ProgressDialog.show(mContext, "",
                "Loading. Please wait...", true);
    }
    public void dismissLoading(){
    }


}
