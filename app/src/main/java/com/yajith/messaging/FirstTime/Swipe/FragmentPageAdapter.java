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
        else if(position==2)
        {
            return new SwipeThird();
        }
        else if(position==3)
        {
            return new SwipeFour();
        }
        else
        {
            return new SwipeFive();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
