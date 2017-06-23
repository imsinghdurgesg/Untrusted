package singh.durgesh.com.applocker.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import singh.durgesh.com.applocker.adapter.InstalledAppsAdapter;
import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.source.AppsManager;


public class AppListActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        //starting background service
        //
        // Get the application context
           mContext = getApplicationContext();

        // Get the activity
        mActivity = AppListActivity.this;

        // Get the widgets reference from XML layout
/*
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
*/
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecycle_app_List);

        // Define a layout for RecyclerView
/*
        mLayoutManager = new GridLayoutManager(mContext,2);
*/
        mLayoutManager = new LinearLayoutManager(mContext );
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Initialize a new adapter for RecyclerView
        mAdapter = new InstalledAppsAdapter(mContext, new AppsManager(mContext).getInstalledPackages());

        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(mAdapter);
    }
}