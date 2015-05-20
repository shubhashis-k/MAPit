package com.example.MAPit.MAPit;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.adapter.NavDrawerListAdapter;
import com.example.MAPit.model.NavDrawerItem;
import com.google.android.gms.maps.GoogleMap;
import com.mapit.backend.searchQueriesApi.model.Search;
import com.mapit.backend.userinfoModelApi.model.ResponseMessages;
import com.mapit.backend.userinfoModelApi.model.UserinfoModel;

import java.util.ArrayList;


public class SlidingDrawerActivity extends ActionBarActivity implements Edit_Profile_Endpoint_Communicator.manipulate_Edit_Profile {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawerLinear;
    private TextView profile_name;
    public ArrayList<Search> searchData;
    private ImageView profilePic;
    public int Count;
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
        profilePic = (ImageView) findViewById(R.id.profile_image);

        String profileName = getIntent().getExtras().getString(PropertyNames.Userinfo_Username.getProperty());
        profile_name.setText(profileName);

        String imageString = getIntent().getExtras().getString(PropertyNames.Userinfo_Profilepic.getProperty());
        if (imageString != null)
            profilePic.setImageBitmap(ImageConverter.stringToimageConverter(imageString));

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

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));


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
                R.drawable.ic_toggle, //nav menu toggle icon
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

    public Bundle getEmail() {
        Bundle mailInfo = getIntent().getExtras();
        return mailInfo;
    }


    @Override
    public void setInfo(UserinfoModel editedInfo) {
        String username = editedInfo.getName();
        String imageText = editedInfo.getImagedata();

        if (imageText == null)
            imageText = "";

        if (username == null)
            username = "";

        if (username.length() > 0)
            profile_name.setText(username);

        if (imageText.length() > 0)
            profilePic.setImageBitmap(ImageConverter.stringToimageConverter(imageText));

        Toast.makeText(this, "Profile Updated!", Toast.LENGTH_LONG).show();
    }


    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position,
                                long id) {
            mDrawerLayout.closeDrawer(mDrawerLinear);
            // display view for selected nav drawer item
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayView(position);
                }
            }, 250);


        }
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case 1:
                fragment = new Friend_Search_Fragment();
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case 2:
                fragment = new Groups_Fragment();
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case 3:
                fragment = new Friend_Request_Fragment();
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Bundle frienddata = new Bundle();
                frienddata.putString(Commands.Notification_job.getCommand(), Commands.Friends_Request.getCommand());
                fragment.setArguments(frienddata);
                break;
            case 4:
                fragment = new Friend_Request_Fragment();
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Bundle groupdata = new Bundle();
                groupdata.putString(Commands.Notification_job.getCommand(), Commands.Group_Join_Group.getCommand());
                fragment.setArguments(groupdata);
                break;
            case 5:
                fragment = new StatusFragment();
                Bundle myWallData = new Bundle();
                myWallData.putString(Commands.Fragment_Caller.getCommand(), Commands.Called_From_MyWall.getCommand());
                myWallData.putString(PropertyNames.Userinfo_Mail.getProperty(), getEmail().getString(PropertyNames.Userinfo_Mail.getProperty()));
                fragment.setArguments(myWallData);
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case 6:
                fragment = new OnlyGoogleMap();
                Bundle data = new Bundle();
                data.putString(Commands.SearchAndADD.getCommand(), Commands.ShowInMap.getCommand());
                fragment.setArguments(data);
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case 7:
                Intent intent = new Intent(SlidingDrawerActivity.this, SignIn.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        startFragment(fragment, position);
    }

    private void startFragment(Fragment fragment, int position) {
        //mDrawerLayout.closeDrawer(mDrawerLinear);
        FragmentManager fm = getFragmentManager();
        int count = fm.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            fm.popBackStackImmediate();
        }

        if (fragment != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();


            // update selected item and title, then close the drawer
            if (position != -1) {
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);

            }
            /*if(position == 0){
                if(mDrawerList.getChildAt(1)!=null)
                mDrawerList.getChildAt(1).setEnabled(false);
            }*/
            //setTitle(navMenuTitles[position]);
            //mDrawerLayout.closeDrawer(mDrawerLinear);
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
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            //int count = getFragmentManager().getBackStackEntryCount();
            getFragmentManager().popBackStack();
        } else {

            final Dialog exit = new Dialog(this);
            exit.setContentView(R.layout.exit_dialog);
            exit.setTitle("Exit");
            Button Yes = (Button) exit.findViewById(R.id.btexit);
            Button No = (Button) exit.findViewById(R.id.btremain);

            Yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Leaving..", Toast.LENGTH_SHORT).show();
                    Intent sigIn = new Intent(SlidingDrawerActivity.this, SignIn.class);
                    startActivity(sigIn);
                }
            });

            No.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exit.dismiss();
                }
            });

            exit.show();
        }
    }

}