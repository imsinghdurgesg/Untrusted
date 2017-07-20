package app.untrusted.model;


import app.untrusted.R;

/**
 * Created by DarshanG on 7/17/2017.
 */

public enum ViewPagerModel {

    RED(R.string.red, R.layout.view_first),
    BLUE(R.string.blue, R.layout.view_second);

    private int mTitleResId;
    private int mLayoutResId;

    ViewPagerModel(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
