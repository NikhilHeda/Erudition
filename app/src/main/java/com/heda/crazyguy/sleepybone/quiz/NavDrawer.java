package com.heda.crazyguy.sleepybone.quiz;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NavDrawer extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;

    private CharSequence mTitle;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
    private NavDrawerListAdapter adapter;

    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_xml);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dlMainDrawer);
        mDrawerList = (ListView) findViewById(R.id.lvLeftDrawer);

        mTitle = mDrawerTitle = getTitle();

        navMenuTitles = getResources().getStringArray(R.array.drawer_items);

        navMenuIcons = getResources().obtainTypedArray(R.array.drawer_icons);

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1), true, "50+"));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));

        navMenuIcons.recycle();

        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Enabling the application icon as button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_select, menu);
            return true;
        }

        @Override
        public boolean onPrepareOptionsMenu(Menu menu) {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
            return super.onPrepareOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // toggle nav drawer on selecting action bar app icon/title
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            // Handle action bar actions click
            switch (item.getItemId()) {
                case R.id.action_settings:
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    */
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void displayView(int position) {

        Fragment fragment = new HomeFragment();

        switch (position) {
            case 1:
                fragment = new LoginFragment();
                break;
            case 2:
                fragment = new SignupFragment();
                break;
            case 3:
                fragment = new SettingsFragment();
                break;
        }

        Bundle args = new Bundle();
        args.putInt("position", position);

        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flMainContent, fragment).commit();


        mDrawerList.setItemChecked(position, true);
        setTitle(navMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }
    }

}
