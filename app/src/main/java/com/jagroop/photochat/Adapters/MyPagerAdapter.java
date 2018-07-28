package com.jagroop.photochat.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jagroop.photochat.Fragments.CameraFragment;
import com.jagroop.photochat.Fragments.ChatFragment;
import com.jagroop.photochat.Fragments.StoryFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {


    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ChatFragment.newInstance();
            case 1:
                return CameraFragment.newInstance();
            case 2:
                return StoryFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
