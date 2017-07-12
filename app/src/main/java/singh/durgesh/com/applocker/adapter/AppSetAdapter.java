package singh.durgesh.com.applocker.adapter;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.model.CheckBoxState;
import singh.durgesh.com.applocker.model.Contact;
import singh.durgesh.com.applocker.source.AppsManager;
import singh.durgesh.com.applocker.utils.AppSharedPreference;
import singh.durgesh.com.applocker.utils.CustomTypefaceSpan;

/**
 * Created by RSharma on 7/11/2017.
 */

public class AppSetAdapter extends RecyclerView.Adapter<AppSetAdapter.ViewHolder>
{
    private Context mContext;
    ArrayList<CheckBoxState> blockedAppList=new ArrayList<CheckBoxState>();
    private String packageName = "";
    ArrayList<CheckBoxState> selectedPackagesList = new ArrayList<>();

    ArrayList<CheckBoxState> protectListFinal1 = new ArrayList<CheckBoxState>();
    ArrayList<CheckBoxState> protectListTemp = new ArrayList<CheckBoxState>();

    public AppSetAdapter(Context context, ArrayList<CheckBoxState> list) {
        mContext = context;
        blockedAppList = list;
        // this.checkStateList = blockAppList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextViewLabel;
        private TextView mTextViewPackage;
        private ImageView mImageViewIcon;
        private CheckBox cbBlockedApp;
        Context context;
        ArrayList<CheckBoxState> blockedAppList;

       public ViewHolder(Context context, final View v,ArrayList<CheckBoxState> blockList) {
            super(v);
            this.context=context;
            this.blockedAppList=blockList;
            // widgets reference from custom layout
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextViewLabel = (TextView) v.findViewById(R.id.app_label);
            mTextViewPackage = (TextView) v.findViewById(R.id.app_package);
            mImageViewIcon = (ImageView) v.findViewById(R.id.iv_icon);
            cbBlockedApp = (CheckBox) v.findViewById(R.id.cb_blocked_app);
        }
    }

    @Override
    public AppSetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_installed_application, parent, false);
        AppSetAdapter.ViewHolder vh = new AppSetAdapter.ViewHolder(mContext,v,blockedAppList);
        return vh;
    }

   /* public void setOnItemClickListener(MyClickListener clickListener) {
        myClickListener = clickListener;
    }*/

    @Override
    public void onBindViewHolder(final AppSetAdapter.ViewHolder holder, final int position)
    {
        //preparing the Final List which has to be Updated in Preferences
        protectListFinal1=(ArrayList<CheckBoxState>)blockedAppList.clone();
        protectListTemp=(ArrayList<CheckBoxState>)blockedAppList.clone();


        //HERE SETTING THE fONT STYLE to TEXTVIEWS
        Typeface fontText = Typeface.createFromAsset(mContext.getAssets(), mContext.getResources().getString(R.string.font_roboto));
        //getting checked list from sharedPreference
        //  a new instance of AppManager class\
        AppsManager appsManager = new AppsManager(mContext);

        //  current package name
        packageName = blockedAppList.get(position).getPackageName();
        //     Log.d("Package Name", packageName);
        //  current app icon
        Drawable icon = appsManager.getAppIconByPackageName(packageName);
        //  current app label
        String label = blockedAppList.get(position).getAppLabel();
        //setting  state of checkbox
        holder.cbBlockedApp.setChecked(true);
        //Controlling the Functionality of Checking and UnChecking the Functionality of CheckBox\
        holder.cbBlockedApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CheckBoxState objCheck1=new CheckBoxState();
                CheckBoxState objCheck2=new CheckBoxState();

/*
                if (!blockList.isEmpty())
                {
                    //In order to make a ArrayList consisting of Unique Values only
                    Set<Contact> unique = new HashSet<Contact>();
                    unique.addAll(blockList);
                    blockListFinal.clear();
                    blockListFinal.addAll(unique);

                }

*/
                if(isChecked)
                {
                    int x=getPosition(blockedAppList.get(position));
                    objCheck1=protectListTemp.get(x);
                    protectListFinal1.add(objCheck1);
                    String name=objCheck1.getAppLabel();
                    Toast.makeText(mContext, name+" is Protected Again", Toast.LENGTH_SHORT).show();

                }
                else if(!isChecked)
                {
                    int x=getPosition(blockedAppList.get(position));
                    objCheck2=protectListTemp.get(x);
                    String name=objCheck2.getAppLabel();
                    Toast.makeText(mContext, name+" is UnProtected Now", Toast.LENGTH_SHORT).show();

                   protectListFinal1.remove(objCheck2);

                }
                AppSharedPreference appSharedPrefs = new AppSharedPreference(mContext);
                Gson gson = new Gson();
                //Removing the Duplicates
                Set<CheckBoxState> unique = new HashSet<CheckBoxState>();
                unique.addAll(protectListFinal1);
                protectListFinal1.clear();
                protectListFinal1.addAll(unique);

                String json = gson.toJson(protectListFinal1);
                appSharedPrefs.putStringData("BlockApps", json);


            }
        });


        //setting FontFamily to TextViews
        SpannableStringBuilder ssName = new SpannableStringBuilder(label.toString());
        SpannableStringBuilder ssPkg = new SpannableStringBuilder(packageName.toString());

        ssName.setSpan(new CustomTypefaceSpan("", fontText), 0, ssName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ssPkg.setSpan(new CustomTypefaceSpan("", fontText), 0, ssPkg.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        // Setting the current app label
        holder.mTextViewLabel.setText(ssName);

        // Setting the current app package name
        holder.mTextViewPackage.setText(ssPkg);

        // Setting the current app icon
        holder.mImageViewIcon.setImageDrawable(icon);


    }

    /**
     * checks whether method is checked previously or not
     * @param packageName
     * @return true or false accordingly
     */
//    public boolean isPackageBlock(String packageName)
//    {
//        for (int i = 0; i < checkStateList.size(); i++) {
//            if (checkStateList.get(i).getPackageName().equals(packageName))
//                return true;
//        }
//        return false;
//    }

    /**
     * in this method userstatsmanager permission is check if it is enable or not
     * @return boolean accordingly
     */

/*
    public boolean isuUsserStatspermission() {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                PackageManager packageManager = mContext.getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
                int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
                return (mode == AppOpsManager.MODE_ALLOWED);

            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        }
        return false;
    }
*/

    /**
     *
     * @param checkBoxState
     * @return position of checkstateList on which user has uncheked the checkbox.
     */
    public int getPosition(CheckBoxState checkBoxState) {
        int currentRemovePOstion = 0;
        for (int position = 0; position < blockedAppList.size(); position++) {
            if (blockedAppList.get(position).getPackageName().equals(checkBoxState.getPackageName())) {
                currentRemovePOstion = position;
            }
        }
        return currentRemovePOstion;

    }


    @Override
    public int getItemCount() {
        // Count the installed apps
        return blockedAppList.size();
    }

  /*  public interface MyClickListener {
        void onItemClick(List<CheckBoxState> packageName);

        void onItemDeselect(List<CheckBoxState> packageName);
    }*/
}
