package Adapter;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.xps.amdavadblog_app.IntroScreenFragment;

public class IntroScreenAdapter extends FragmentPagerAdapter {

    public IntroScreenAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return IntroScreenFragment.newInstance(Color.parseColor("#03A9F4"), position); // blue

            case 1:
                return IntroScreenFragment.newInstance(Color.parseColor("#c873f4"), position); // blue

            default:
                return IntroScreenFragment.newInstance(Color.parseColor("#4CAF50"), position); // green
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
