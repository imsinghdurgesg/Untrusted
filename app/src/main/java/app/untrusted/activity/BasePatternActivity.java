package app.untrusted.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.untrusted.R;
import app.untrusted.utils.PatternView;

public class BasePatternActivity extends AppCompatActivity {

    private static final int CLEAR_PATTERN_DELAY_MILLI = 1000;

    protected TextView mMessageText;
    protected PatternView mPatternView;
    protected LinearLayout mButtonContainer;
    protected Button mLeftButton;
    protected Button mRightButton;

    private final Runnable clearPatternRunnable = new Runnable() {
        public void run() {
            // clearPattern() resets display mode to DisplayMode.Correct.
            mPatternView.clearPattern();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_pattern);
        mMessageText = (TextView)findViewById(R.id.pl_message_text);
        mPatternView = (PatternView)findViewById(R.id.pl_pattern);
        mButtonContainer = (LinearLayout)findViewById(R.id.pl_button_container);
        mLeftButton = (Button)findViewById(R.id.pl_left_button);
        mRightButton = (Button)findViewById(R.id.pl_right_button);
    }

    protected void removeClearPatternRunnable() {
        mPatternView.removeCallbacks(clearPatternRunnable);
    }

    protected void postClearPatternRunnable() {
        removeClearPatternRunnable();
        mPatternView.postDelayed(clearPatternRunnable, CLEAR_PATTERN_DELAY_MILLI);
    }
}
