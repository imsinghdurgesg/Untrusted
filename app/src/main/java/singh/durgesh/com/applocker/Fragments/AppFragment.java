package singh.durgesh.com.applocker.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import singh.durgesh.com.applocker.Adapter.InstalledAppsAdapter;
import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.Source.AppsManager;

public class AppFragment extends Fragment {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_app_list, container, false);
        mContext = getContext();
        ProgressDialog dialog = ProgressDialog.show(mContext, "",
                "Loading. Please wait...", true);
        dialog.show();
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
        dialog.cancel();
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
