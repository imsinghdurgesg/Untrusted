package app.untrusted.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import app.untrusted.R;
import app.untrusted.utils.AppSharedPreference;
import app.untrusted.utils.PatternUtils;
import app.untrusted.utils.PatternView;
import app.untrusted.utils.ViewAccessibilityCompat;

import static app.untrusted.services.SecureMyAppsService.blockedPackage;
import static app.untrusted.services.SecureMyAppsService.foregroundPackage;


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

        Intent intent = getIntent();
        String currentPackage = intent.getStringExtra("Package");


        mSharedPref = new AppSharedPreference(this);

        super.onCreate(savedInstanceState);
        if (currentPackage != null) {
            Log.d("Confirm Pattern", currentPackage);
            top_image.setImageDrawable(getAppIconByPackageName(currentPackage));

        }
        pl_button_container = (LinearLayout) findViewById(R.id.pl_button_container);

        Intent intentLock = getIntent();
        Boolean lockvalue = intentLock.getBooleanExtra("PatternLock", false);
        if (lockvalue) {
            mMessageText.setText(R.string.pl_draw_old_pattern_to_unlock);
        } else {
            mMessageText.setText(R.string.pl_draw_pattern_to_unlock);
        }
        mMessageText.setTextColor(getResources().getColor(R.color.pattern_text));
        mPatternView.setInStealthMode(isStealthModeEnabled());
        mPatternView.setOnPatternListener(this);
        //  mLeftButton.setText(R.string.pl_cancel);
        pl_button_container.setVisibility(View.GONE);
        // isFirstTimeUserComplete = "isFirstTimeUserComplete";
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
        mRightButton.setText(R.string.pl_forgot_pattern);
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForgotPassword();
            }
        });
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
        mPatternView.setDisplayMode(app.untrusted.utils.PatternView.DisplayMode.Correct);
    }

    @Override
    public void onPatternCellAdded(List<PatternView.Cell> pattern) {
    }

    @Override
    public void onPatternDetected(List<PatternView.Cell> pattern) {
        if (isPatternCorrect(pattern)) {
            mMessageText.setText(R.string.pattern_Success);
            mMessageText.setTextColor(getResources().getColor(R.color.black));
            mPatternView.setDisplayMode(PatternView.DisplayMode.Correct);
            onConfirmed();
        } else {
            mMessageText.setText(R.string.pl_wrong_pattern);
            mMessageText.setTextColor(getResources().getColor(R.color.red));
            mPatternView.setDisplayMode(app.untrusted.utils.PatternView.DisplayMode.Wrong);
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

    public Drawable getAppIconByPackageName(String packageName) {
        Drawable icon;
        try {
            icon = this.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // Get a default icon
            icon = ContextCompat.getDrawable(this, R.drawable.icon_avatat);
        }
        return icon;
    }

    protected void onConfirmed() {
        blockedPackage = foregroundPackage;
        setResult(RESULT_OK);
        Intent intentLock = getIntent();
        Boolean lockvalue = intentLock.getBooleanExtra("PatternLock", false);
        if (lockvalue) {
            Intent setPatternIntent = new Intent(this, SetPatternActivity.class);
            setPatternIntent.putExtra("fromConfirmactivity", true);
            startActivity(setPatternIntent);
            finish();
        } else {
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

        Intent intentLock = getIntent();
        Boolean lockvalue = intentLock.getBooleanExtra("PatternLock", false);
        if (lockvalue) {
            Intent intent = new Intent(this, PrefsActivity.class);
            startActivity(intent);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
            finish();
        }
    }

}




