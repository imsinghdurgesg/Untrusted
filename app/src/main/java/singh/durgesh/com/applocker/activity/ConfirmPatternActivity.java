package singh.durgesh.com.applocker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.utils.AppSharedPreference;
import singh.durgesh.com.applocker.utils.PatternUtils;
import singh.durgesh.com.applocker.utils.PatternView;
import singh.durgesh.com.applocker.utils.ViewAccessibilityCompat;

// For AOSP implementations, see:
// https://android.googlesource.com/platform/packages/apps/Settings/+/master/src/com/android/settings/ConfirmLockPattern.java
// https://android.googlesource.com/platform/frameworks/base/+/43d8451/policy/src/com/android/internal/policy/impl/keyguard/KeyguardPatternView.java
// https://android.googlesource.com/platform/frameworks/base/+/master/packages/Keyguard/src/com/android/keyguard/KeyguardPatternView.java
public class ConfirmPatternActivity extends BasePatternActivity
        implements PatternView.OnPatternListener {


    private static final String KEY_NUM_FAILED_ATTEMPTS = "num_failed_attempts";
    public static final int RESULT_FORGOT_PASSWORD = RESULT_FIRST_USER;
    AppSharedPreference mSharedPref;
    protected int mNumFailedAttempts;
    LinearLayout pl_button_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferencesLock = PreferenceManager.getDefaultSharedPreferences(this);
        String lockName = sharedPreferencesLock.getString("Lock", null);
        if (lockName != null) {
            if (lockName.equals("PhoneLock")) {
                Intent mintent = getIntent();
                boolean isFromService = mintent.getBooleanExtra("isFromService", false);
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("isFromService",isFromService);
                startActivity(intent);
                finish();
            }
        }

        //SharedPreference to change Theme dynamically...
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String themeName = sharedPreferences.getString("Theme", null);
        if (themeName != null) {
            if (themeName.equals("Redtheme")) {
                setTheme(R.style.MyMaterialTheme);
            } else {
                setTheme(R.style.MyMaterialThemeGreen);
            }
        }

        super.onCreate(savedInstanceState);
        pl_button_container= (LinearLayout) findViewById(R.id.pl_button_container);
        mSharedPref = new AppSharedPreference(this);
        mMessageText.setText(R.string.pl_draw_pattern_to_unlock);
        mMessageText.setTextColor(getResources().getColor(R.color.pattern_text));
        mPatternView.setInStealthMode(isStealthModeEnabled());
        mPatternView.setOnPatternListener(this);
        pl_button_container.setVisibility(View.GONE);
//        mLeftButton.setText(R.string.pl_cancel);
//        mLeftButton.setVisibility(View.INVISIBLE);
        //  isFirstTimeUserComplete = "isFirstTimeUserComplete";
       /* mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });*/
//        pl_button_container.setVisibility(View.INVISIBLE);
     //   mRightButton.setText(R.string.pl_forgot_pattern);
    //    mRightButton.setVisibility(View.INVISIBLE);
       /* mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onForgotPassword();
            }
        });*/
        ViewAccessibilityCompat.announceForAccessibility(mMessageText, mMessageText.getText());

        if (savedInstanceState == null) {
            mNumFailedAttempts = 0;
        } else {
            mNumFailedAttempts = savedInstanceState.getInt(KEY_NUM_FAILED_ATTEMPTS);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_NUM_FAILED_ATTEMPTS, mNumFailedAttempts);
    }

    @Override
    public void onPatternStart() {
        removeClearPatternRunnable();

        // Set display mode to correct to ensure that pattern can be in stealth mode.
        mPatternView.setDisplayMode(singh.durgesh.com.applocker.utils.PatternView.DisplayMode.Correct);
    }

    @Override
    public void onPatternCellAdded(List<PatternView.Cell> pattern) {
    }

    @Override
    public void onPatternDetected(List<PatternView.Cell> pattern) {
        if (isPatternCorrect(pattern)) {
            onConfirmed();
        } else {
            mMessageText.setText(R.string.pl_wrong_pattern);
            mPatternView.setDisplayMode(singh.durgesh.com.applocker.utils.PatternView.DisplayMode.Wrong);
            postClearPatternRunnable();
            ViewAccessibilityCompat.announceForAccessibility(mMessageText, mMessageText.getText());
            onWrongPattern();
        }
    }

    @Override
    public void onPatternCleared() {
        removeClearPatternRunnable();
    }

    protected boolean isStealthModeEnabled() {
        return false;
    }

    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        String patternSha1 = mSharedPref.getStringData("patternSha");

        return TextUtils.equals(PatternUtils.patternToSha1String(pattern), patternSha1);
    }

    protected void onConfirmed() {
        setResult(RESULT_OK);
        Intent intent = getIntent();
        boolean isFromService = intent.getBooleanExtra("isFromService", false);
        if (isFromService) {
            finish();
        } else {
            Intent mIntent = new Intent(this, HomeActivity.class);
            startActivity(mIntent);
            finish();
        }
    }


    protected void onWrongPattern() {
        ++mNumFailedAttempts;
    }

    protected void onCancel() {
        setResult(RESULT_CANCELED);
        // finish();
    }

    protected void onForgotPassword() {
        setResult(RESULT_FORGOT_PASSWORD);
        //finish();
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

}
