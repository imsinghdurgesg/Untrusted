package singh.durgesh.com.applocker.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import singh.durgesh.com.applocker.R;

public class BaseActivity extends AppCompatActivity {
    ProgressDialog customPD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showProgressDialog(){
        if(customPD != null && customPD.isShowing()){
            customPD.dismiss();
        }
        customPD = new ProgressDialog(this, R.style.MyTheme);
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
