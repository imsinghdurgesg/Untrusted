package app.untrusted.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import app.untrusted.R;
import app.untrusted.utils.AppSharedPreference;
import app.untrusted.utils.PatternUtils;
import app.untrusted.utils.PatternView;
import app.untrusted.utils.ViewAccessibilityCompat;
/*
 * Part of the source is from platform_packages_apps/Settings
 * android.settings.ChooseLockPattern.java
 */
public class SetPatternActivity extends BasePatternActivity
        implements PatternView.OnPatternListener {
    private enum LeftButtonState {

        Cancel(R.string.pl_cancel, true),
        CancelDisabled(R.string.pl_cancel, false),
        Redraw(R.string.pl_redraw, true),
        RedrawDisabled(R.string.pl_redraw, false);

        public final int textId;
        public final boolean enabled;

        LeftButtonState(int textId, boolean enabled) {
            this.textId = textId;
            this.enabled = enabled;
        }
    }

    private enum RightButtonState {

        Continue(R.string.pl_continue, true),
        ContinueDisabled(R.string.pl_continue, false),
        Confirm(R.string.pl_confirm, true),
        ConfirmDisabled(R.string.pl_confirm, false);

        public final int textId;
        public final boolean enabled;

        RightButtonState(int textId, boolean enabled) {
            this.textId = textId;
            this.enabled = enabled;
        }
    }

    private enum Stage {

        Draw(R.string.pl_draw_pattern, LeftButtonState.Cancel, RightButtonState.ContinueDisabled,
                true),
        DrawTooShort(R.string.pl_pattern_too_short, LeftButtonState.Redraw,
                RightButtonState.ContinueDisabled, true),
        DrawValid(R.string.pl_pattern_recorded, LeftButtonState.Redraw, RightButtonState.Continue,
                false),
        Confirm(R.string.pl_confirm_pattern, LeftButtonState.Cancel,
                RightButtonState.ConfirmDisabled, true),
        ConfirmWrong(R.string.pl_wrong_pattern, LeftButtonState.Cancel,
                RightButtonState.ConfirmDisabled, true),
        ConfirmCorrect(R.string.pl_pattern_confirmed, LeftButtonState.Cancel,
                RightButtonState.Confirm, false);

        public final int messageId;
        public final LeftButtonState leftButtonState;
        public final RightButtonState rightButtonState;
        public final boolean patternEnabled;

        Stage(int messageId, LeftButtonState leftButtonState, RightButtonState rightButtonState,
              boolean patternEnabled) {
            this.messageId = messageId;
            this.leftButtonState = leftButtonState;
            this.rightButtonState = rightButtonState;
            this.patternEnabled = patternEnabled;
        }
    }

    private static final String KEY_STAGE = "stage";
    private static final String KEY_PATTERN = "pattern";

    private int mMinPatternSize;
    private List<PatternView.Cell> mPattern;
    private Stage mStage;
    AppSharedPreference msharedPref;
    int lockPatternValue=5;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msharedPref = new AppSharedPreference(this);
        Boolean isFirstTimeUser = msharedPref.getBooleanData("FirstTimeUser", false);
        Boolean userCompleteProcess = msharedPref.getBooleanData("userCompleteProcess", false);
        // checking whether user is visitin firsttime or not and if second time the onConfirmPatternActivity will open
        Intent intent = getIntent();
        //regaqrding changing the status color
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.pattern_dot));

        Boolean valuefromConfirmActivity = intent.getBooleanExtra("fromConfirmactivity",false);
        if(valuefromConfirmActivity )
        {
            lockPatternValue++;

        }
        else {
            if (isFirstTimeUser && userCompleteProcess) {
                Intent mIntent = new Intent(this, ConfirmPatternActivity.class);
                startActivity(mIntent);
                finish();
            }
        }
        FirstTimeOnly();
        mMinPatternSize = getMinPatternSize();

        mPatternView.setOnPatternListener(this);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftButtonClicked();
            }
        });
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightButtonClicked();
            }
        });

        if (savedInstanceState == null) {
            updateStage(Stage.Draw);
        } else {
            String patternString = savedInstanceState.getString(KEY_PATTERN);
            if (patternString != null) {
                mPattern = PatternUtils.stringToPattern(patternString);
            }
            updateStage(Stage.values()[savedInstanceState.getInt(KEY_STAGE)]);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_STAGE, mStage.ordinal());
        if (mPattern != null) {
            outState.putString(KEY_PATTERN, PatternUtils.patternToString(mPattern));
        }
    }

    @Override
    public void onPatternStart() {

        removeClearPatternRunnable();

        mMessageText.setText(R.string.pl_recording_pattern);
        mPatternView.setDisplayMode(PatternView.DisplayMode.Correct);
        mLeftButton.setEnabled(false);
        mRightButton.setEnabled(false);
    }

    @Override
    public void onPatternCellAdded(List<PatternView.Cell> pattern) {
    }

    @Override
    public void onPatternDetected(List<PatternView.Cell> newPattern) {
        switch (mStage) {
            case Draw:
            case DrawTooShort:
                if (newPattern.size() < mMinPatternSize) {
                    updateStage(Stage.DrawTooShort);
                } else {
                    mPattern = new ArrayList<>(newPattern);
                    updateStage(Stage.DrawValid);
                }
                break;
            case Confirm:
            case ConfirmWrong:
                if (newPattern.equals(mPattern)) {
                    updateStage(Stage.ConfirmCorrect);
                } else {
                    updateStage(Stage.ConfirmWrong);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected stage " + mStage + " when "
                        + "entering the pattern.");
        }
    }

    @Override
    public void onPatternCleared() {
        removeClearPatternRunnable();
    }

    private void onLeftButtonClicked() {
        if (mStage.leftButtonState == LeftButtonState.Redraw) {
            mPattern = null;
            updateStage(Stage.Draw);
        } else if (mStage.leftButtonState == LeftButtonState.Cancel) {
            onCanceled();
        } else {
            throw new IllegalStateException("left footer button pressed, but stage of " + mStage
                    + " doesn't make sense");
        }
    }

    protected void onCanceled() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void onRightButtonClicked() {
        if (mStage.rightButtonState == RightButtonState.Continue) {
            if (mStage != Stage.DrawValid) {
                throw new IllegalStateException("expected ui stage " + Stage.DrawValid
                        + " when button is " + RightButtonState.Continue);
            }
            updateStage(Stage.Confirm);
        } else if (mStage.rightButtonState == RightButtonState.Confirm) {
            if (mStage != Stage.ConfirmCorrect) {
                throw new IllegalStateException("expected ui stage " + Stage.ConfirmCorrect
                        + " when button is " + RightButtonState.Confirm);
            }
            onSetPattern(mPattern);
            onConfirmed();
        }
    }

    protected void onConfirmed() {
        setResult(RESULT_OK);
        if(lockPatternValue==6)
        {
            Intent mIntent = new Intent(this, PrefsActivity.class);
            startActivity(mIntent);
            finish();
        }
        else
        {
            Intent mIntent = new Intent(this, HomeActivity.class);
            startActivity(mIntent);
            finish();

        }
    }

    private void updateStage(Stage newStage) {

        Stage previousStage = mStage;
        mStage = newStage;

        if (mStage == Stage.DrawTooShort) {
            mMessageText.setText(getString(mStage.messageId, mMinPatternSize));
        } else {
            mMessageText.setText(mStage.messageId);
        }

        mLeftButton.setText(mStage.leftButtonState.textId);
        mLeftButton.setEnabled(mStage.leftButtonState.enabled);

        mRightButton.setText(mStage.rightButtonState.textId);
        mRightButton.setEnabled(mStage.rightButtonState.enabled);
        //mRightButton.setEnabled(false);
        mPatternView.setInputEnabled(mStage.patternEnabled);

        switch (mStage) {
            case Draw:
                // clearPattern() resets display mode to DisplayMode.Correct.
                mPatternView.clearPattern();
                break;
            case DrawTooShort:
                mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case DrawValid:
                break;
            case Confirm:
                mPatternView.clearPattern();
                break;
            case ConfirmWrong:
                mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case ConfirmCorrect:
                break;
        }

        // If the stage changed, announce the header for accessibility. This
        // is a no-op when accessibility is disabled.
        if (previousStage != mStage) {
            ViewAccessibilityCompat.announceForAccessibility(mMessageText, mMessageText.getText());
        }
    }

    protected int getMinPatternSize() {
        return 4;
    }

    protected void onSetPattern(List<PatternView.Cell> pattern) {
        String patternSha = PatternUtils.patternToSha1String(pattern);
        // TODO: Save patternSha1 in SharedPreferences.
        msharedPref.putStringData("patternSha", patternSha);
    }

    //Saving the boolean while first time user visited in the app
    public void FirstTimeOnly() {
        msharedPref.putBooleanData("FirstTimeUser", true);
    }
}