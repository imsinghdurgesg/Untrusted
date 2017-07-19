package app.untrusted.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import app.untrusted.R;
import app.untrusted.adapter.InstalledAppsAdapter;
import app.untrusted.source.AppsManager;


public class AppListActivity extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        mContext = getApplicationContext();

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecycle_app_List);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new InstalledAppsAdapter(mContext, new AppsManager(mContext).getInstalledPackages());
        mRecyclerView.setAdapter(mAdapter);
    }
}