package singh.durgesh.com.applocker.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.List;

import singh.durgesh.com.applocker.Utils.PatternUtils;
import singh.durgesh.com.applocker.Utils.PatternView;

public class PasswordActivity extends BasePatternActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Boolean isFirstTimeUser = prefs.getBoolean("FirstTimeEnter", false);
        if(isFirstTimeUser){
            Intent mIntent=new Intent(this,SignupActivity.class);
            startActivity(mIntent);
        }
        FirstTimeOnly();
    }

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    protected void onSetPattern(List<PatternView.Cell> pattern) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        String patternSha1 = PatternUtils.patternToSha1String(pattern);
        // TODO: Save patternSha1 in SharedPreferences.
        editor.putString("patternSha1", patternSha1);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startMain);
        finish();
    }
    public void FirstTimeOnly(){
        SharedPreferences prefs = getSharedPreferences("FirstTimeBoolean", MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean("FirstTimeEnter",true);
        editor.apply();
    }
}
