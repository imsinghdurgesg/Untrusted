package singh.durgesh.com.applocker.adapter;

/**
 * Created by DSingh on 6/5/2017.
 */

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.model.CheckBoxState;
import singh.durgesh.com.applocker.source.AppsManager;
import singh.durgesh.com.applocker.utils.AppSharedPreference;
import singh.durgesh.com.applocker.utils.CustomTypefaceSpan;

public class InstalledAppsAdapter extends RecyclerView.Adapter<InstalledAppsAdapter.ViewHolder> {

    private Context mContext;
    ArrayList<CheckBoxState> checkBoxStatesList;
    ArrayList<CheckBoxState> checkStateList;
    private String packageName = "";
    String selectedPackgemane = "";
    ArrayList<CheckBoxState> selectedPackagesList = new ArrayList<>();
    public MyClickListener myClickListener;

    public InstalledAppsAdapter(Context context, ArrayList<CheckBoxState> list) {
        mContext = context;
        this.checkBoxStatesList = list;
        // this.checkStateList = blockAppList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextViewLabel;
        private TextView mTextViewPackage;
        private ImageView mImageViewIcon;
        private CheckBox cbBlockedApp;

        ViewHolder(View v) {
            super(v);
            // widgets reference from custom layout
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextViewLabel = (TextView) v.findViewById(R.id.app_label);
            mTextViewPackage = (TextView) v.findViewById(R.id.app_package);
            mImageViewIcon = (ImageView) v.findViewById(R.id.iv_icon);
            cbBlockedApp = (CheckBox) v.findViewById(R.id.cb_blocked_app);
            /*cbBlockedApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    if (cb.isChecked()) {
                        Log.d("checkList---", "" + checkBoxStatesList.size());
                        checkState.setPackageName(selectedPackgemane);
                        checkState.setPosition(getAdapterPosition());
                        selectedPackagesList.add(checkState);

                      //  myClickListener.onItemClick(selectedPackagesList);
                    } else {
                        //checkState.setPackageName(selectedPackgemane);
                        checkState.setSelected(false);
                        selectedPackagesList.remove(getAdapterPosition());
                        myClickListener.onItemDeselect(selectedPackagesList);
                    }
                   *//* {
                        *//**//*CheckBoxState state = (CheckBoxState) cb.getTag();
                        state.setSelected(cb.isChecked());
                        checkBoxBlockStatesList.get(getAdapterPosition()).setSelected(cb.isChecked());*//**//*
                    }*//*

                    Gson gson = new Gson();
                    String json = gson.toJson(selectedPackagesList);
                    mSharedPref.putStringData("BlockApps", json);
                }
            });*/
        }


       /* public void setItenCheckListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemCheck(view, getAdapterPosition());
        }*/

    }

    @Override
    public InstalledAppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_installed_application, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void setOnItemClickListener(MyClickListener clickListener) {
        myClickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        //HERE SETTING THE fONT STYLE to TEXTVIEWS
        Typeface fontText = Typeface.createFromAsset(mContext.getAssets(),mContext.getResources().getString(R.string.font_roboto));
        //getting checked list from sharedPreference
        AppSharedPreference mSharedPref = new AppSharedPreference(mContext);
        String packages = mSharedPref.getStringData("BlockApps");
//        Log.e("reading here", packages);
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        Type type = new TypeToken<List<CheckBoxState>>() {
        }.getType();
        checkStateList = gson.fromJson(packages, type);
        //  a new instance of AppManager class\
        AppsManager appsManager = new AppsManager(mContext);

        //  current package name
        packageName = checkBoxStatesList.get(position).getPackageName();
        //     Log.d("Package Name", packageName);
        //  current app icon
        Drawable icon = appsManager.getAppIconByPackageName(packageName);
        //  current app label
        String label = appsManager.getApplicationLabelByPackageName(packageName);
        //
        //  holder.cbBlockedApp.setOnClickListener(null);
        //setting  state of checkbox
        holder.cbBlockedApp.setOnCheckedChangeListener(null);
        if (checkStateList != null) {
            if (checkStateList.isEmpty()) {
                //   Snackbar.make(holder.mCardView, "No App is bloked yet",Snackbar.LENGTH_SHORT).show();

            } else {
                if (isPackageBlock(packageName)) {

                    holder.cbBlockedApp.setChecked(true);
                } else {
                    holder.cbBlockedApp.setChecked(false);
                }
            }
        } else {
            holder.cbBlockedApp.setChecked(false);
        }


        holder.cbBlockedApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isuUsserStatspermission()) {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    mContext.startActivity(intent);
                }
                if (checkStateList != null) {
                    if (!checkStateList.isEmpty()) {
                        selectedPackagesList.clear();
                        //In order to make a ArrayList consisting of Unique Values only
                        HashSet<CheckBoxState> unique = new HashSet<CheckBoxState>();
                        unique.clear();
                        unique.addAll(checkStateList);
                        selectedPackagesList.addAll(unique);
                    }
                }


                if (isChecked) {
                    CheckBoxState checkBoxState = new CheckBoxState();
                    //selectedPackgemane = checkBoxStatesList.get(holder.getAdapterPosition()).getPackageName();
                    checkBoxState.setPackageName(checkBoxStatesList.get(holder.getAdapterPosition()).getPackageName());
                    selectedPackagesList.add(checkBoxState);
                } else {
                    selectedPackagesList.remove(getPosition(checkBoxStatesList.get(position)));
                }

               /* SharedPreferences appSharedPrefs = mContext.getSharedPreferences("BlockedContacts",Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(selectedPackagesList);
                prefsEditor.putString("BlockApps", json);
                prefsEditor.commit();*/
                AppSharedPreference mShared = new AppSharedPreference(mContext);
                Gson gson = new Gson();
                String json = gson.toJson(selectedPackagesList);
                mShared.putStringData("BlockApps", json);
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


       /* if (stts.equals("1")) {
            holder.cbBlockedApp.setChecked(true);
        }
        if (stts.equals("0")) {
            holder.cbBlockedApp.setChecked(false);
        }*/

        // Setting a click listener for CardView
       /* holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the intent to launch the specified application
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
                if (intent != null) {
                    mContext.startActivity(intent);

                } else {
                    Toast.makeText(mContext, packageName + " Launch Error.", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
       /* holder.cbBlockedApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockAppsList.add(packageName);
                Log.d("List Size", "" + blockAppsList.size());
                blockedAppHashList = new HashSet(blockAppsList);
                Log.d("here list block app", "" + blockedAppHashList.size());
               *//* if (Build.VERSION.SDK_INT >= 21) {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    mContext.startActivity(intent);
                }*//*
            }
        });*/
        /*holder.cbBlockedApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = mContext.getSharedPreferences("checkedState", 0);
                if (isChecked) {
                    status = "1";
                    int pos = new ViewHolder(buttonView).getAdapterPosition();
                    Log.d("position", "" + pos);
                } else {
                    status = "0";
                }
                preferences.edit().putString("status", status).commit();
            }
        });*/

    }

    public boolean isPackageBlock(String packageName) {
        for (int i = 0; i < checkStateList.size(); i++) {
            if (checkStateList.get(i).getPackageName().equals(packageName))
                return true;
        }
        return false;
    }

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

    /*protected int getPosition(CheckBoxState checkBoxState) {
        int position = 0;
        if (checkStateList != null) {
            for (int blockPosition = 0; blockPosition < checkStateList.size(); blockPosition++) {
                if (checkStateList.get(blockPosition).getPackageName().equals(checkBoxState.getPackageName())) {
                    position = blockPosition;
                }
            }

        }
        return position;

    }*/
    public int getPosition(CheckBoxState checkBoxState) {
        int currentRemovePOstion = 0;
        for (int position = 0; position < selectedPackagesList.size(); position++) {
            if (selectedPackagesList.get(position).getPackageName().equals(checkBoxState.getPackageName())) {
                currentRemovePOstion = position;
            }
        }
        return currentRemovePOstion;

    }


    @Override
    public int getItemCount() {
        // Count the installed apps
        return checkBoxStatesList.size();
    }

    public interface MyClickListener {
        void onItemClick(List<CheckBoxState> packageName);

        void onItemDeselect(List<CheckBoxState> packageName);
    }

   /* public interface ItemClickListener {
        void onItemCheck(View v, int pos);
    }*/

   /* public void saveBlockAppList(int pos) {
        SharedPreferences preferences = mContext.getSharedPreferences("db", 0);
        // Set<String> hs = ss.getStringSet("set", blockedAppHashList);
        HashSet<String> in = new HashSet<>(blockedAppHashList);
        in.add(String.valueOf(in.size() + 1));
        preferences.edit().putStringSet("set", in).commit(); // brevity
        // SharedPreferences sss = getSharedPreferences("db", 0); // not needed
        Log.d("chauster", "2.set = " + preferences.getStringSet("set", in));
    }*/

}

/*
package singh.durgesh.com.applocker.adapter;

*/
/**
 * Created by DSingh on 6/5/2017.
 *//*


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.source.AppsManager;

public class InstalledAppsAdapter extends RecyclerView.Adapter<InstalledAppsAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mDataSet;
    ArrayList<String> blockAppsList = new ArrayList<>();
    HashSet<String> blockedAppHashList;
    public InstalledAppsAdapter(Context context, List<String> list) {
        mContext = context;
        mDataSet = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextViewLabel;
        public TextView mTextViewPackage;
        public ImageView mImageViewIcon;
        public CheckBox cbBlockedApp;

        public ViewHolder(View v) {
            super(v);
            //  widgets reference from custom layout
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextViewLabel = (TextView) v.findViewById(R.id.app_label);
            mTextViewPackage = (TextView) v.findViewById(R.id.app_package);
            mImageViewIcon = (ImageView) v.findViewById(R.id.iv_icon);
            cbBlockedApp = (CheckBox) v.findViewById(R.id.cb_blocked_app);
        }
    }

    @Override
    public InstalledAppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_installed_application, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

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
                if (intent != null) {
                    mContext.startActivity(intent);

                } else {
                    Toast.makeText(mContext, packageName + " Launch Error.", Toast.LENGTH_SHORT).show();
                }
            }
        });
          holder.cbBlockedApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockAppsList.add(packageName);
                Log.d("List Size",""+blockAppsList.size());
                blockedAppHashList=new HashSet(blockAppsList);
                Log.d("here list block app",""+blockedAppHashList.size());
                saveBlockAppList();
                if (Build.VERSION.SDK_INT >= 21) {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // Count the installed apps
        return mDataSet.size();
    }
    public void saveBlockAppList(){
        SharedPreferences preferences = mContext.getSharedPreferences("db", 0);
       // Set<String> hs = ss.getStringSet("set", blockedAppHashList);
        HashSet<String> in = new HashSet<>(blockedAppHashList);
        in.add(String.valueOf(in.size()+1));
        preferences.edit().putStringSet("set", in).commit(); // brevity
      // SharedPreferences sss = getSharedPreferences("db", 0); // not needed
        Log.d("chauster", "2.set = "+ preferences.getStringSet("set", in));
    }

}
*/
