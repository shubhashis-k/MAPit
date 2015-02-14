package com.example.MAPit.MAPit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.MAPit.adapter.NavDrawerListAdapter;
import com.example.MAPit.model.NavDrawerItem;
import com.mapit.backend.searchQueriesApi.model.Search;
import com.mapit.backend.userinfoModelApi.model.ResponseMessages;
import java.util.ArrayList;
import java.util.List;


public class SlidingDrawerActivity extends ActionBarActivity implements Edit_Profile_Endpoint_Communicator.manipulate_Edit_Profile{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawerLinear;
    private TextView profile_name;
    public ArrayList <Search> searchData;
    // nav drawer title
    private CharSequence mDrawerTitle;

    Fragment fragment = null;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_drawer);

        //initializing profile_name
        profile_name = (TextView) findViewById(R.id.profile_name);
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinear = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));


        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_activities, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);

        }

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // For editing Profile
        profile_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fragment = new Edit_Profile();
                fragment.setArguments(getEmail());
                startFragment(fragment, -1);
                return false;


            }
        });
    }

    public Bundle getEmail()
    {
        Bundle mailInfo = getIntent().getExtras();
        return mailInfo;
    }

    @Override
    public void setResponseMessage(ResponseMessages response) {
        String res = response.getMessage();

        if(res.equals("Update OK"))
        {
            Toast.makeText(this, "update Successful!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
    }



    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        // Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
               // mDrawerList.getChildAt(0).setEnabled(false);
                break;
            case 1:
                fragment = new Friend_Search_Fragment();
                break;
            case 2:
                fragment = new Groups_Fragment();
                break;
            case 3:
                // fragment = new CommunityFragment();
                break;
            case 4:
                fragment = new MyWallFragment();
                break;
            case 5:
                // fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }
        startFragment(fragment, position);
    }

    private void startFragment(Fragment fragment, int position) {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null);
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();


            // update selected item and title, then close the drawer
            if (position != -1) {
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
            }
            //setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerLinear);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
           /* case R.id.action_settings:
                return true;
                */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLinear);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

   @Override
    public void onBackPressed() {

        if(getFragmentManager().getBackStackEntryCount() >0){
            getFragmentManager().popBackStack();
        }
        else{
            super.onBackPressed();
        }
       //Toast.makeText(this,"it worked",Toast.LENGTH_LONG).show();
    }


}