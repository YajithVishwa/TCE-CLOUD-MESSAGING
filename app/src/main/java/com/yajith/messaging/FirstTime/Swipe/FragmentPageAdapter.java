package com.yajith.messaging.FirstTime.Swipe;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentPageAdapter extends FragmentPagerAdapter {
    public FragmentPageAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {
            return new SwipeFirst();
        }
        else if(position==1)
        {
            return new SwipeSecond();
        }
        else
        {
            return new SwipeThird();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
