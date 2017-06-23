package singh.durgesh.com.applocker.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import singh.durgesh.com.applocker.R;

public class BaseFragment extends Fragment{

    ProgressDialog customPD;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    protected void showProgressDialog(){
        if(customPD != null && customPD.isShowing()){
            customPD.dismiss();
        }
        customPD = new ProgressDialog(getActivity(), R.style.MyTheme);
        customPD.setCancelable(false);
        customPD.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        customPD.show();

    }

    protected void hideProgressDialog(){
        if(customPD != null){
            if(customPD.isShowing()){
                customPD.dismiss();
            }
        }
    }
}
