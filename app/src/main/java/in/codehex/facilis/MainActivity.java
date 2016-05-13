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

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import in.codehex.facilis.app.Config;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    SharedPreferences userPreferences;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initObjects();
        prepareObjects();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                showAlertLogout();
                return true;
            case R.id.action_profile:
                return true;
        }
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawers();
        return selectDrawerItem(item.getItemId(), item.getTitle().toString());
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawers();
        else super.onBackPressed();
    }

    /**
     * Initialize the objects.
     */
    private void initObjects() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationView = (NavigationView) findViewById(R.id.nav);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.layout_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        userPreferences = getSharedPreferences(Config.PREF_USER, MODE_PRIVATE);
    }

    /**
     * Implement and manipulate the objects.
     */
    private void prepareObjects() {
        setSupportActionBar(mToolbar);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mNavigationView.setNavigationItemSelectedListener(this);

        if (!userPreferences.contains(Config.KEY_PREF_IS_FIRST_LOGIN)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            userPreferences.edit().putBoolean(Config.KEY_PREF_IS_FIRST_LOGIN, false).commit();
        }

        selectDrawerItem(mNavigationView.getMenu().getItem(0).getItemId(),
                mNavigationView.getMenu().getItem(0).getTitle().toString());
    }

    /**
     * Display an alert when logout icon is clicked.
     */
    private void showAlertLogout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Logout");
        alertDialog.setMessage("Are you sure you want to logout?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO: send logout request to API
                userPreferences.edit().clear().commit();
                mIntent = new Intent(MainActivity.this, LoginActivity.class);
                mIntent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /**
     * Create a fragment for the selected item in the drawer and apply it to the container layout.
     *
     * @param id    the item id of the item selected in the nav menu.
     * @param title the title of the item which has to be set in the toolbar.
     * @return true always.
     */
    private boolean selectDrawerItem(int id, String title) {
        Fragment fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.view_orders:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(title);
                fragment = new ViewOrdersFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                return true;
            case R.id.active_bids:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(title);
                fragment = new ActiveBidsFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                return true;
            case R.id.previous_bids:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(title);
                fragment = new PreviousBidsFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                return true;
            case R.id.successful_bids:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(title);
                fragment = new SuccessfulBidsFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                return true;
            case R.id.about_us:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(title);
                fragment = new AboutUsFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                return true;
            case R.id.contact_us:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(title);
                fragment = new ContactUsFragment();
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.commit();
                return true;
            default:
                return true;
        }
    }

    /**
     * Show the view bid item list based on the fragment which calls it.
     *
     * @param name    it can be active or previous or successful bid fragment.
     * @param orderId the order id of the item clicked.
     * @param bidId   the bid id of the item clicked.
     */
    public void showBidItems(String name, int orderId, int bidId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Ordered Items");
        }
        Fragment fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        switch (name) {
            case Config.KEY_FRAGMENT_ACTIVE:
                fragment = new ViewBidItemsFragment();
                bundle.putString(Config.KEY_BUNDLE_FRAGMENT, name);
                bundle.putInt(Config.KEY_BUNDLE_ORDER_ID, orderId);
                bundle.putInt(Config.KEY_BUNDLE_BID_ID, bidId);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.addToBackStack(name);
                fragmentTransaction.commit();
                break;
            case Config.KEY_FRAGMENT_PREVIOUS:
                fragment = new ViewBidItemsFragment();
                bundle.putString(Config.KEY_BUNDLE_FRAGMENT, name);
                bundle.putInt(Config.KEY_BUNDLE_ORDER_ID, orderId);
                bundle.putInt(Config.KEY_BUNDLE_BID_ID, bidId);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.addToBackStack(name);
                fragmentTransaction.commit();
                break;
            case Config.KEY_FRAGMENT_SUCCESSFUL:
                fragment = new ViewBidItemsFragment();
                bundle.putString(Config.KEY_BUNDLE_FRAGMENT, name);
                bundle.putInt(Config.KEY_BUNDLE_ORDER_ID, orderId);
                bundle.putInt(Config.KEY_BUNDLE_BID_ID, bidId);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.layout_container, fragment);
                fragmentTransaction.addToBackStack(name);
                fragmentTransaction.commit();
                break;
        }
    }
}
