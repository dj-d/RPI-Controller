package com.e.rpi_controller.ui.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.e.rpi_controller.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class ControllerFragment extends Fragment {

    private static final int NUM_OF_TABS = 2;

    private static final String TAB_1 = "TV";
    private static final String TAB_2 = "SOUNDBAR";

    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_controller, container, false);

        View controllerContainer = (View) container.getParent();
        appBar = controllerContainer.findViewById(R.id.appbar);
        tabs = new TabLayout(getActivity());
        appBar.addView(tabs);

        viewPager = root.findViewById(R.id.controller_viewPager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.removeView(tabs);
    }

    /**
     * Manage the tabs of fragment
     */
    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        String[] titleTabs = {TAB_1, TAB_2};

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TVFragment();

                case 1:
                    return new SoundBarFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_OF_TABS;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleTabs[position];
        }
    }
}