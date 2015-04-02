/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.fjp.rightrack.SlidingTabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import at.fjp.rightrack.MotivationFragment;
import at.fjp.rightrack.R;
import at.fjp.rightrack.TodoFragment;

/**
 * A basic sample
 * to display a custom {@link android.support.v4.view.ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class SlidingTabsTodoFragment extends Fragment {

    static final String LOG_TAG = "SlidingTabsBasicFragment";
    private static final String ARG_PARAM1 = "param1";
    private static Context mContext;

    /**
     * A custom {@link android.support.v4.view.ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link android.support.v4.view.ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;


    public static SlidingTabsTodoFragment newInstance(int sectionNumber, Context context) {
        SlidingTabsTodoFragment fragment = new SlidingTabsTodoFragment();
        mContext = context;
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    /**
     * Inflates the {@link android.view.View} which will be displayed by this {@link android.support.v4.app.Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sliding_tabs_todo, container, false);


    }

    // BEGIN_INCLUDE (fragment_onviewcreated)
    /**
     * This is called after the {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)} has finished.
     * Here we can pick out the {@link android.view.View}s we need to configure from the content view.
     *
     * We set the {@link android.support.v4.view.ViewPager}'s adapter to be an instance of {@link MyAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link android.support.v4.view.ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        // Center the tabs in the layout
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
    }
    // END_INCLUDE (fragment_onviewcreated)


    public class MyAdapter extends FragmentStatePagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = TodoFragment.newInstance(position, mContext);
                    break;
                case 1:
                    fragment = TodoFragment.newInstance(position, mContext);
                    break;
                case 2:
                    fragment = TodoFragment.newInstance(position, mContext);
                    break;
                case 3:
                    fragment = MotivationFragment.newInstance(position);
                    break;
            }
            return fragment;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence pageTitle = "NOT SET";
            switch (position) {
                case 0:
                    pageTitle = "Daily";
                    break;
                case 1:
                    pageTitle = "Others";
                    break;
                case 2:
                    pageTitle = "Monthly";
                    break;
                case 3:
                    pageTitle = "Yearly";
                    break;
            }
            return pageTitle;
            //return "Item " + (position + 1);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
