package com.mooo.ziggypop.candconline;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by ziggypop on 4/17/15.
 * Contains settings
 */
public class SettingsActivity extends AppCompatActivity{

    public static final String TAG = "SettingsActivity";
    static public int current_game;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.settings_toolbar);
        mToolBar.setTitle(R.string.settings);
        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if (getIntent().getExtras() != null ) {
            // set the color of the toolbar based on the current game.
            current_game = getIntent().getExtras().getInt("current_game");
            switch (current_game) {
                case 1:
                    mToolBar.setBackgroundResource(R.color.kw_red);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        window.setStatusBarColor(getResources().getColor(R.color.kw_red));
                    break;
                case 2:
                    mToolBar.setBackgroundResource(R.color.cnc3_green);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        window.setStatusBarColor(getResources().getColor(R.color.cnc3_green));
                    break;
                case 3:
                    mToolBar.setBackgroundResource(R.color.generals_yellow);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        window.setStatusBarColor(getResources().getColor(R.color.generals_yellow));
                    break;
                case 4:
                    mToolBar.setBackgroundResource(R.color.zh_orange);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        window.setStatusBarColor(getResources().getColor(R.color.zh_orange));
                    break;
                case 5:
                    mToolBar.setBackgroundResource(R.color.ra3_red);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        window.setStatusBarColor(getResources().getColor(R.color.ra3_red));
                    break;
            }
        }


        //swap in the settings fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SettingsFragment()).commit();

    }


    public static class SettingsFragment extends PreferenceFragmentCompat{

        public void onCreate(Bundle savedInstanceState) {
            getActivity().setTheme(R.style.AppTheme);
            super.onCreate(savedInstanceState);


            // Show the Licence
            Preference licencePreference = getPreferenceScreen().findPreference("licence_preference");
            licencePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.licence);
                    builder.setMessage(R.string.BSD);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) { } // do nothing.
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });

            final Preference notifyIfOnline = getPreferenceScreen().findPreference("notify_if_online");

            // Receive notifications
            Preference receiveNotifications = getPreferenceScreen().findPreference(getString(R.string.receive_notifications));
            receiveNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isActive = (boolean) newValue;
                    if (isActive) {
                        notifyIfOnline.setEnabled(true);
                    } else {
                        notifyIfOnline.setEnabled(false);
                    }
                    return true;
                }
            });

            //Notification if you are online.
            notifyIfOnline.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    return true;
                }
            });


            //Notification interval




            // Manage DB friends
            Preference manageFriends = getPreferenceScreen().findPreference("manage_friends");
            manageFriends.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // launch an activity to show all players in the database (regardless of their online status)
                    Log.v(TAG, "clicked manageFriends");
                    Intent intent = new Intent(getContext().getApplicationContext(), PlayerDatabaseViewerActivity.class);
                    intent.putExtra("current_game", current_game);
                    getActivity().startActivity(intent);
                    return true;
                }
            });

            // Reset the DB.
            Preference resetDBPref = getPreferenceScreen().findPreference("reset_db_pref");
            resetDBPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Reset Player Database");
                    builder.setMessage(getActivity().getString(R.string.reset_db_disclaimer));
                    builder.setPositiveButton(getActivity().getText(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // delete the database.
                                    PlayerDatabaseHandler db = new PlayerDatabaseHandler(getActivity().getApplicationContext());
                                    db.resetDB(db.getReadableDatabase());
                                }
                            });
                    builder.setNegativeButton(getActivity().getText(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { } // do nothing, do not delete the DB.
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });





        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
