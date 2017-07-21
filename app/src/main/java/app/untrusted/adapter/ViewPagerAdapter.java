package app.untrusted.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import app.untrusted.R;
import app.untrusted.activity.LaunchScreenViewPagerActivity;
import app.untrusted.activity.SplashScreenActivity;
import app.untrusted.model.ViewPagerModel;

/**
 * Created by DarshanG on 7/17/2017.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;

    View mApplock;

    View mCalllock;

    public ViewPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View view = new View(collection.getContext());
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (position == 0) {
            view = inflater.inflate(R.layout.view_first, null, false);
            RelativeLayout linearLayout = (RelativeLayout) view.findViewById(R.id.linear_layout);
            mApplock = linearLayout.findViewById(R.id.rocket);
            startAnimation();
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAnimation();
                }
            });
            Button button = (Button) linearLayout.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SplashScreenActivity.class);
                    mContext.startActivity(intent);
                    ((LaunchScreenViewPagerActivity) mContext).finish();
                }
            });
        } else {
            view = inflater.inflate(R.layout.view_second, null, false);
            RelativeLayout linearLayoutThird = (RelativeLayout) view.findViewById(R.id.linear_layout2);
            mCalllock = linearLayoutThird.findViewById(R.id.rocket);
            startCallAnimation();
            linearLayoutThird.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCallAnimation();
                }
            });
            Button buttonEnterApp = (Button) linearLayoutThird.findViewById(R.id.buttonEnterApp);
            buttonEnterApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SplashScreenActivity.class);
                    mContext.startActivity(intent);
                    ((LaunchScreenViewPagerActivity) mContext).finish();
                }
            });
        }
        ((ViewPager) collection).addView(view, 0);
        return view;
    }

    private void startCallAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 330);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mCalllock.setTranslationX(value);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
               valueAnimator.setDuration(2500L);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    private void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 330);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mApplock.setTranslationX(value);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(2500L);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return ViewPagerModel.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ViewPagerModel customPagerEnum = ViewPagerModel.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }
}
