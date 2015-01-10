package com.thing342.layoutbasedscouting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import java.io.IOException;
import java.util.Map;

//import android.view.Menu;
//import android.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity
{

    private ScoutingApplication app;
    private ListView listView;
    private int currentPos = 0;

    /////////////////////////--CONSTRUCTORS--/////////////////////////////////

    /////////////////////////--OVERRIDDEN METHODS--///////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        app = ((ScoutingApplication) getApplication());
        app.resumeAll();

        setContentView(R.layout.activity_main);
        updateTitle();
        setArrayAdapter();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        updateTitle();
        setArrayAdapter();
        getListView().setSelection(currentPos);
    }

    private void setArrayAdapter()
    {
        SharedPreferences prefs = getSharedPreferences(app.PREFS, Context.MODE_PRIVATE);
        boolean matchesFirst = prefs.getBoolean(app.MATCHESFIST_PREF, true);
        Log.d("AerialAssault", Boolean.toString(matchesFirst));

        if (matchesFirst) {
            ArrayAdapter<MatchGroup> arrayAdapter = new ArrayAdapter<MatchGroup>(
                    this, android.R.layout.simple_list_item_1, app.groups.getValues());
            //MatchAdapter arrayAdapter = new MatchAdapter (this, app.groups);
            listView = (ListView) findViewById(R.id.teamListView);
            TextView emptyText = new TextView(getBaseContext());
            emptyText.setText("No matches set.");
            emptyText.setTextAppearance(getBaseContext(),
                    android.R.style.TextAppearance_Large);
            listView.setAdapter(arrayAdapter);
            listView.setClickable(true);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setLongClickable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3)
                {

                    viewTeam(position, true);

                }
            });
        } else {
            ArrayAdapter<FRCTeam> arrayAdapter = new ArrayAdapter<FRCTeam>(
                    this, android.R.layout.simple_list_item_1,
                    app.teamsList.getValues());
            //TeamAdapter arrayAdapter = new TeamAdapter(this, app.teamsList);
            listView = (ListView) findViewById(R.id.teamListView);
            TextView emptyText = new TextView(getBaseContext());
            emptyText.setText("No matches set.");
            emptyText.setTextAppearance(getBaseContext(),
                    android.R.style.TextAppearance_Large);
            listView.setAdapter(arrayAdapter);
            listView.setClickable(true);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setLongClickable(true);

            int i = 0;
            for (Map.Entry<Integer, FRCTeam> entry : app.teamsList) {
                if (entry.getValue().isComplete())
                    listView.setItemChecked(i, true);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

                i++;
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3)
                {

                    viewTeam(position, false);

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_schedule_add:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                MatchAddFragment fragment = MatchAddFragment.newInstance(new MatchAddFragment.OnFinishListener()
                {

                    @Override
                    public void onFinish()
                    {
                        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                    }
                });

                fragment.show(ft, "MatchAddFragment");
                return true;

            case R.id.action_team_add:
                addTeamDialog();
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                return true;

            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;

            case R.id.action_export:
                SharedPreferences prefs = getSharedPreferences(app.PREFS, Context.MODE_PRIVATE);
                DeviceId restoredId = DeviceId.getFromValue(prefs.getString(app.DEVICEID_PREF, "0"));
                try {
                    app.exportAll(restoredId.filename + ".txt");
                    Toast.makeText(getApplicationContext(), "Match Data exported to /sdcard/Scouting/" + restoredId.filename + ".txt", Toast.LENGTH_SHORT).show();
                } catch (IOException ioe) {
                    Toast.makeText(getApplicationContext(), "Unable to export matches: " + ioe.getMessage(), Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    ////////////////////////--PUBLIC METHODS--////////////////////////////////

    public ListView getListView()
    {
        return listView;
    }

    public void updateTitle()
    {
        SharedPreferences prefs = getSharedPreferences(app.PREFS, Context.MODE_PRIVATE);
        DeviceId restoredId = DeviceId.getFromValue(prefs.getString(app.DEVICEID_PREF, "0"));

        String title = getString(R.string.app_name) + "(" + getString(R.string.version_name) +
                ") - Device ID: " + restoredId.toString();

        setTitle(title);

        if (android.os.Build.VERSION.SDK_INT >= 5) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(restoredId.hexColor));
        }

    }

    ///////////////////////--PRIVATE METHODS--/////////////////////////////////

    private void addTeamDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final EditText field = new EditText(getBaseContext());
        field.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        field.setHint("Team Number");
        field.setTextColor(Color.BLACK);

        builder
                .setTitle("Add Team:")
                .setView(field)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getBaseContext(), Integer.toString(Integer.parseInt(field.getText().toString())), Toast.LENGTH_SHORT).show();
                        app.addTeam(Integer.parseInt(field.getText().toString()));
                        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                        app.saveAll();
                        //app.resumeAll();
                    }
                })
                .show();

    }


    private void viewTeam(int pos, boolean matchesFirst)
    {

        currentPos = pos;

        if (matchesFirst) {
            MatchGroup mGroup = app.groups.getValues().get(pos);
            FRCTeam[] teams = app.getTeamsWithMatch(mGroup.num);

            if (teams.length == 1) {
                FRCTeam thisTeam = teams[0];
                Log.d("AerialAssault", Integer.toString(pos));

                Intent i = new Intent(this, MatchEditorActivity.class);
                i.putExtra("AERIALASSAULT_TEST_FRCMATCH", mGroup.num);
                i.putExtra("AERIALASSAULT_TEST_FRCTEAMMATCH", thisTeam.number);
                startActivity(i);
                return;
            }

        }

        return;
    }

}
