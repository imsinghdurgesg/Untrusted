package singh.durgesh.com.applocker.Adapter;

/**
 * Created by DSingh on 6/5/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.Source.AppsManager;

public class InstalledAppsAdapter extends RecyclerView.Adapter<InstalledAppsAdapter.ViewHolder>{

    private Context mContext;
    private List<String> mDataSet;

    public InstalledAppsAdapter(Context context, List<String> list){
        mContext = context;
        mDataSet = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView mTextViewLabel;
        public TextView mTextViewPackage;
        public ImageView mImageViewIcon;

        public ViewHolder (View v){
            super(v);
            //  widgets reference from custom layout
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextViewLabel = (TextView) v.findViewById(R.id.app_label);
            mTextViewPackage = (TextView) v.findViewById(R.id.app_package);
            mImageViewIcon = (ImageView) v.findViewById(R.id.iv_icon);
        }
    }

    @Override
    public InstalledAppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_installed_application,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        //  a new instance of AppManager class
        AppsManager appsManager = new AppsManager(mContext);

        //   current package name
        final String packageName = (String) mDataSet.get(position);

        //  current app icon
        Drawable icon = appsManager.getAppIconByPackageName(packageName);

        //  current app label
        String label = appsManager.getApplicationLabelByPackageName(packageName);

        // Setting the current app label
        holder.mTextViewLabel.setText(label);

        // Setting the current app package name
        holder.mTextViewPackage.setText(packageName);

        // Setting the current app icon
        holder.mImageViewIcon.setImageDrawable(icon);

        // Setting a click listener for CardView
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the intent to launch the specified application
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
                if(intent != null){
                    mContext.startActivity(intent);
                }else {
                    Toast.makeText(mContext,packageName + " Launch Error.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        // Count the installed apps
        return mDataSet.size();
    }

}
