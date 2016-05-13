/*
 * Copyright 2016 Bobby
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.codehex.facilis;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.codehex.facilis.app.Config;


/**
 * A fragment that is used to display orders list in the {@link MainActivity} class.
 */
public class ViewOrdersFragment extends Fragment {

    TabLayout mTabLayout;

    public ViewOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_orders, container, false);

        initObjects(view);
        prepareObjects();

        return view;
    }

    /**
     * Initialize the objects.
     *
     * @param view the root view of the layout.
     */
    private void initObjects(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.layout_tab);
    }

    /**
     * Implement and manipulate the objects.
     */
    private void prepareObjects() {
        for (int i = 0; i < Config.TAB_TITLE.length; i++)
            mTabLayout.addTab(mTabLayout.newTab().setText(Config.TAB_TITLE[i]));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setCurrentTab(0);
    }

    /**
     * Set the layout to the selected tab.
     *
     * @param position the position of the tab.
     */
    private void setCurrentTab(int position) {
        Fragment fragment;
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                fragment = new AllDealsFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragment = new HotDealsFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragment = new LastMinuteFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                break;
        }
    }
}
