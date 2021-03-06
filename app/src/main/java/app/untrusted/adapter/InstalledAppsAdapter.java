package app.untrusted.adapter;

/**
 * Created by DSingh on 6/5/2017.
 */

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
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

import app.untrusted.R;
import app.untrusted.model.CheckBoxState;
import app.untrusted.source.AppsManager;
import app.untrusted.utils.AppSharedPreference;
import app.untrusted.utils.CustomTypefaceSpan;

public class InstalledAppsAdapter extends RecyclerView.Adapter<InstalledAppsAdapter.ViewHolder> {

    private Context mContext;
    ArrayList<CheckBoxState> checkBoxStatesList;
    ArrayList<CheckBoxState> checkStateList;
    private String packageName = "";
    ArrayList<CheckBoxState> selectedPackagesList = new ArrayList<>();

    public InstalledAppsAdapter(Context context, ArrayList<CheckBoxState> list) {
        mContext = context;
        this.checkBoxStatesList = list;
        // this.checkStateList = blockAppList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewLabel;
        //    private TextView mTextViewPackage;
        private ImageView mImageViewIcon;
        private CheckBox cbBlockedApp;

        ViewHolder(View v) {
            super(v);
            // widgets reference from custom layout
            mTextViewLabel = (TextView) v.findViewById(R.id.app_label);
            //mTextViewPackage = (TextView) v.findViewById(R.id.app_package);
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

   /* public void setOnItemClickListener(MyClickListener clickListener) {
        myClickListener = clickListener;
    }*/

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //HERE SETTING THE fONT STYLE to TEXTVIEWS
        Typeface fontText = Typeface.createFromAsset(mContext.getAssets(), mContext.getResources().getString(R.string.font_roboto));
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
        String label = checkBoxStatesList.get(position).getAppLabel();
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

// on the click of the checkbox saving the package name for the persistence of the checksate
        holder.cbBlockedApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isUserHavingStatsPermission() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
                    } else {
                        builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
                    }
                    builder.setTitle("Permission")
                            .setMessage("In android 5.0 or above,you must permit user access permission in setting to use app protection feature of untrusted")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent intent = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                        intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                                        mContext.startActivity(intent);
                                        dialog.dismiss();
                                        Toast.makeText(mContext,"Please enable permission for Untrusted",Toast.LENGTH_LONG).show();
                                    }

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

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
                    checkBoxState.setPackageName(checkBoxStatesList.get(holder.getAdapterPosition()).getPackageName());
                    checkBoxState.setAppLabel(checkBoxStatesList.get(holder.getAdapterPosition()).getAppLabel());
                    selectedPackagesList.add(checkBoxState);
                } else {
                    selectedPackagesList.remove(getPosition(checkBoxStatesList.get(position)));
                }
                // Saving the values in shared preference
                AppSharedPreference mShared = new AppSharedPreference(mContext);
                Gson gson = new Gson();
                String json = gson.toJson(selectedPackagesList);
                mShared.putStringData("BlockApps", json);
                notifyDataSetChanged();
            }
        });

        //setting FontFamily to TextViews
        SpannableStringBuilder ssName = new SpannableStringBuilder(label);
        SpannableStringBuilder ssPkg = new SpannableStringBuilder(packageName);

        ssName.setSpan(new CustomTypefaceSpan("", fontText), 0, ssName.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ssPkg.setSpan(new CustomTypefaceSpan("", fontText), 0, ssPkg.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);


        // Setting the current app label
        holder.mTextViewLabel.setText(ssName);

        // Setting the current app package name
        // holder.mTextViewPackage.setText(ssPkg);

        // Setting the current app icon
        holder.mImageViewIcon.setImageDrawable(icon);


    }

    /**
     * checks whether method is checked previously or not
     *
     * @param packageName
     * @return true or false accordingly
     */
    public boolean isPackageBlock(String packageName) {
        for (int i = 0; i < checkStateList.size(); i++) {
            if (checkStateList.get(i).getPackageName().equals(packageName))
                return true;
        }
        return false;
    }

    /**
     * in this method userstatsmanager permission is check if it is enable or not
     *
     * @return boolean accordingly
     */

    public boolean isUserHavingStatsPermission() {
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

    /**
     * @param checkBoxState
     * @return position of checkstateList on which user has uncheked the checkbox.
     */
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

  /*  public interface MyClickListener {
        void onItemClick(List<CheckBoxState> packageName);

        void onItemDeselect(List<CheckBoxState> packageName);
    }*/
}

