package com.yajith.messaging.FirstTime.Swipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.yajith.messaging.R;

public class SwipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        setContentView(R.layout.activity_swipe);
        ViewPager viewPager=findViewById(R.id.page);
        TabLayout tabLayout=findViewById(R.id.tab);
        FragmentPageAdapter fragmentPageAdapter=new FragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPageAdapter);
        tabLayout.setupWithViewPager(viewPager,true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }
}