package com.yajith.messaging;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yajith.messaging.Fragment.RecentChat.RecentChatFragment;
import com.yajith.messaging.Fragment.VideoCall.VideoCallFragment;

public class ViewPager extends FragmentPagerAdapter {
    public ViewPager(FragmentManager context)
    {
        super(context);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {
            return new RecentChatFragment();
        }
        else
        {
            return new VideoCallFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
