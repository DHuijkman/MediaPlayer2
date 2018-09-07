package com.desktop.duco.mediaplayer2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

class MainPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{
    final int PAGE_COUNT = 3;
    private int tabIcons[] = {R.drawable.ic_library_music_black_24dp, R.drawable.ic_music_note_black_24dp};

    private List<Fragment> fragments = new ArrayList<>();
    private AppCompatActivity activity;

    public MainPagerAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        this.activity = activity;

        fragments.add(MusicListFragment.instantiate(activity, MusicListFragment.class.getName()));

        fragments.add(ControllerFragment.instantiate(activity,ControllerFragment.class.getName()));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }
}
