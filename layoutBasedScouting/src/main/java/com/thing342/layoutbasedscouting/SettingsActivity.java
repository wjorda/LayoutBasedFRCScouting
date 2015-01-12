package com.thing342.layoutbasedscouting;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

//import android.view.MenuItem;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity
{

    private Toolbar mToolBar;

    //////////////--CONSTRUCTORS--////////////////////////

    //////////////--OVERRIDDEN METHODS--//////////////////

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setTheme();
        Log.d("AerialAssault", "setupActionBar()");
        //addPreferencesFromResource(R.xml.preferences);
        setPreferenceScreen(makePreferences());
        Log.d("AerialAssault", "addPreferencesFromResources()");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setupActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                // TODO: If Settings has multiple levels, Up should navigate up
                // that hierarchy.
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(int layoutResID)
    {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.activity_settings, new LinearLayout(this), false);

        mToolBar = (Toolbar) contentView.findViewById(R.id.action_bar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        ViewGroup contentWrapper = (ViewGroup) contentView.findViewById(R.id.content_wrapper);
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);

        getWindow().setContentView(contentView);
    }

    /////////////////--PRIVATE METHODS--//////////////////

    private PreferenceScreen makePreferences()
    {
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);

        String entries[][] = {
                {"Other/No ID", "Red 1", "Red 2", "Red 3", "Blue 1", "Blue 2", "Blue 3"},
                {"0", "1", "2", "3", "4", "5", "6"}
        };

        ListPreference deviceID = new ListPreference(this);
        deviceID.setTitle("Select Device ID");
        deviceID.setSummary("Select device ID.");
        //deviceID.setIcon(R.drawable.ic_action_phone);
        deviceID.setKey("deviceid");
        deviceID.setEntries(entries[0]);
        deviceID.setEntryValues(entries[1]);
        deviceID.setDefaultValue(entries[1][0]);
        deviceID.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                Log.d("SettinActivity", preference.toString() + " : " + newValue.toString());
                getPreferenceManager().getSharedPreferences().edit().putString("deviceid", (String) newValue).commit();
                setTheme((String) newValue);
                restartOnTheme();
                return false;
            }
        });
        root.addPreference(deviceID);

        FileDialogPreference csvImport = new FileDialogPreference(this);
        csvImport.setTitle("Import Schedule From CSV");
        csvImport.setSummary("Import matches schedule from a local CSV file.");
        //csvImport.setIcon(R.drawable.ic_action_add_to_queue);
        csvImport.setDialogTitle("Select Match Schedule...");
        csvImport.setToastText("Matches Imported Successfully!");
        csvImport.setFilter(".*csv|.*3helix");
        csvImport.setOnFileSelectedListener(new FileDialogPreference.OnFileSelectedListener()
        {
            @Override
            public void onFileSelected(File file)
            {
                ((ScoutingApplication) getApplication()).createFromFile(file);

            }
        });
        root.addPreference(csvImport);

        FileDialogPreference xmlImport = new FileDialogPreference(this);
        xmlImport.setTitle("Import Scoresheet from XML");
        xmlImport.setSummary("Import scoring widgets from a local XML file.");

        SharedPreferences prefs = getSharedPreferences(ScoutingApplication.PREFS, Context.MODE_PRIVATE);
        String xmlFilePath = prefs.getString(ScoutingApplication.XMLPATH_PREF, null);
        if (xmlFilePath != null) xmlImport.setDialogTitle("Currently using " + xmlFilePath);
        else xmlImport.setDialogTitle("Select Scoresheet...");

        xmlImport.setToastText("Scoresheet Imported Successfully!");
        xmlImport.setFilter(".*xml|.*3helix");
        xmlImport.setOnFileSelectedListener(new FileDialogPreference.OnFileSelectedListener()
        {

            @Override
            public void onFileSelected(File file)
            {
                ((ScoutingApplication) getApplication()).changeLayout(file);

            }
        });
        root.addPreference(xmlImport);

        DialogConfirmPreference resetAll = new DialogConfirmPreference(this, "Clear All Data", "Delete all teams and matches.", R.drawable.ic_action_refresh,
                "Really Clear All Data?", "This cannot be undone.", "OK", "Cancel", "All data cleared.", Toast.LENGTH_LONG, android.R.drawable.ic_dialog_alert);
        resetAll.setListener(new DialogConfirmPreference.OnDialogListener()
        {

            @Override
            public void onDecline()
            {
                // Do Nothing, Cancel.
            }

            @Override
            public void onConfirm()
            {
                ((ScoutingApplication) getApplication()).clearAll();

            }
        });
        root.addPreference(resetAll);

        CheckBoxPreference matchesFirst = new CheckBoxPreference(this);
        matchesFirst.setTitle("Display Matches First");
        matchesFirst.setSummaryOn("Display a list of matches, each containing teams");
        matchesFirst.setSummaryOff("Display a list of teams, each containing matches");
        //matchesFirst.setIcon(R.drawable.ic_action_collection);
        matchesFirst.setKey("matchesFirst");
        matchesFirst.setDefaultValue(true);
        root.addPreference(matchesFirst);

        return root;
    }

    private void restartOnTheme()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog));
        builder.setTitle("Relaunch App")
                .setMessage("This change requires reloading the application.")
                .setCancelable(true)
                .setPositiveButton("Close", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent data = new Intent();
                        //---set the data to pass back---
                        data.putExtra(MainActivity.DEVICE_ID_CHANGED, true);
                        setResult(RESULT_OK, data);
                        //---close the activity---
                        dialog.dismiss();
                        finish();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    private void setupActionBar()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            // Show the Up button in the action bar.
            mToolBar.setTitle("Settings");
        }
    }

    private void setTheme()
    {
        SharedPreferences prefs = getSharedPreferences(ScoutingApplication.PREFS, Context.MODE_PRIVATE);
        DeviceId restoredId = DeviceId.getFromValue(prefs.getString("deviceid", "0"));
        setTheme(restoredId.styleId);
    }

    private void setTheme(String newValue)
    {
        DeviceId restoredId = DeviceId.getFromValue(newValue);
        setTheme(restoredId.settingsStyleId);

        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        } else setPreferenceScreen(makePreferences());
    }

}
