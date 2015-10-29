package com.example.adamhurwitz.hackingenvironment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * TabsAdapter manages the different fragments corresponding to each of the tabs in the application.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    // The list of fragments that the adapter holds.
    private final List<Fragment> mFragments = new ArrayList<>();

    // The list of titles corresponding to each of the fragments. Each fragment is a new tab.
    private final List<String> mFragmentTitles = new ArrayList<>();

    /**
     * The default constructor for the TabsAdapter.
     */
    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Adds a new fragment to the list of fragments for the adapter.
     *
     * @param fragment An android application fragment.
     * @param title    A string of the fragment name to use for the title of the tab.
     */
    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
