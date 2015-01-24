package com.thing342.layoutbasedscouting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thing342.layoutbasedscouting.util.Instantiable;
import com.thing342.layoutbasedscouting.util.IterableHashMap;

import java.util.ArrayList;
//import android.app.ActionBar;
//import android.view.Menu;
//import android.view.MenuItem;

public class MatchEditorActivity extends ActionBarActivity
{

    private final IterableHashMap<Integer, Field> fieldLookup = new IterableHashMap<Integer, Field>();
    private ArrayList<Object> data;
    private LinearLayout layout;
    private ScoutingApplication app;
    private Match mMatch;
    private FRCTeam mTeam;
    /*private int autoTopGoal=0, autoBottomGoal=0, autoShotsMissed=0, autoHotGoal=0;
    private boolean autoMobility=false;
    private int teleTopGoal=0, teleBottomGoal=0, teleShotsMissed=0, teleAssists=0, teleTrussShots=0, teleTrussCatches=0;
    private Rating offensiveRating=Rating.NA, defensiveRating=Rating.NA, driveTeamRating=Rating.NA;*/
    private int team = 0;
    private int match = 0;

    @SuppressWarnings("rawtypes")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        app = ((ScoutingApplication) getApplication());
        setTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_editor);
        //setupSlidingMenu();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        //mMatch = b.getParcelable("AERIALASSAULT_TEST_FRCTEAMMATCH");
        team = b.getInt("AERIALASSAULT_TEST_FRCTEAMMATCH");
        match = b.getInt("AERIALASSAULT_TEST_FRCMATCH");

        mTeam = app.teamsList.get(team);
        mMatch = mTeam.matches.get(match);
        data = new ArrayList<Object>();
        //fillSavedData();

        setTitle(mTeam.toString() + " - Match " + mMatch.toString());

        layout = (LinearLayout) findViewById(R.id.matchEditorLayout);
        //this.data = mMatch.data;

        Field f;
        int pos = 0;
        ArrayList<ArrayList<Field>> datas = app.groupedData.getValues();
        ArrayList<String> groupNames = app.groupedData.getKeys();

        Log.d("datas size", datas.size() + "");

        for (int j = 0; j < datas.size(); j++) {

            View group = getLayoutInflater().inflate(R.layout.group, null);
            LinearLayout content = (LinearLayout) group.findViewById(R.id.group_content);
            TextView groupTitle = (TextView) group.findViewById(R.id.group_title);
            groupTitle.setText(groupNames.get(j));
            ArrayList<Field> groupData = datas.get(j);
            Log.d("group datas size", groupData.size() + "");

            for (int i = 0; i < groupData.size(); i++) {

                f = groupData.get(i);

                Object newEntry;
                View thisView = null;

                if (f.getType() == null) {
                    thisView = f.getView(this, null);
                } else {
                    if (mMatch.data != null && mMatch.data.size() > 0)
                        newEntry = f.parse(mMatch.data.get(pos));

                    else try {
                        newEntry = f.getType().newInstance();

                    } catch (InstantiationException e) { //if default constructor DNE

                        String classname = f.getType().toString();
                        Log.d("AerialAssault", classname);

                        if (classname.contains("class java.lang.Integer")) { //Counter
                            newEntry = 0;
                            Log.d("AerialAssault", "New Int");
                        } else if (classname.contains("class java.lang.Boolean")) { //Checkbox
                            newEntry = false;
                            Log.d("AerialAssault", "New Bool");
                        } else if (classname.contains("Rating")) { //RatingBar
                            newEntry = Rating.NA;
                            Log.d("AerialAssault", "New Rating");
                        } else {
                            newEntry = 0;
                        }
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        newEntry = 0;
                    }

                    Log.d("newEntry Class", newEntry.getClass().getCanonicalName());

                    if (newEntry instanceof Instantiable)
                        newEntry = ((Instantiable) newEntry).getEntry();

                    data.add(newEntry);


                    try {
                        if (f.getType().newInstance() instanceof Instantiable) {
                            Log.d("Scouting", "Instantiable type " + i);
                            Instantiable instance = (Instantiable) f.getType().newInstance();
                            instance.setValue(newEntry);
                            thisView = f.getView(this, instance);
                        } else thisView = f.getView(this, f.getType().cast(newEntry));
                    } /*catch (ClassCastException e) {
                        Log.e("Scouting", "Error generating field " + i);
                        Log.e("Scouting", "Stack: " + Arrays.toString(e.getStackTrace()));
    
                    } */ catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (thisView != null) {
                        thisView.setTag(pos);
                        fieldLookup.put(pos, f);
                    }
                    pos++;
                    Log.d("pos", pos + "");
                }

                if (thisView != null) content.addView(thisView);
                else Log.d("Scouting", "Field" + i + " is null");
                //Log.d("AerialAssault", "I SEE YOU VIEWER");
            }

            layout.addView(group);
        }

        /*if (android.os.Build.VERSION.SDK_INT >= 5) {
            SharedPreferences prefs = getSharedPreferences(ScoutingApplication.PREFS, Context.MODE_PRIVATE);
            DeviceId restoredId = DeviceId.getFromValue(prefs.getString(ScoutingApplication.DEVICEID_PREF, "0"));
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(restoredId.styleId));
        }*/
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        saveMatch();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.match_editor, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                saveMatch();
                return true;
            /*case R.id.action_settings:
                openSettings();
                return true;*/
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setTheme()
    {
        SharedPreferences prefs = getSharedPreferences(ScoutingApplication.PREFS, Context.MODE_PRIVATE);
        DeviceId restoredId = DeviceId.getFromValue(prefs.getString(ScoutingApplication.DEVICEID_PREF, "0"));

        Log.d(getLocalClassName(), restoredId.name);
        setTheme(restoredId.styleId);
    }

    private void saveMatch()
    {

        for (int j = 0; j < layout.getChildCount(); j++) {
            LinearLayout cardContent = (LinearLayout) layout.getChildAt(j).findViewById(R.id.group_content);

            for (int i = 0; i < cardContent.getChildCount(); i++) {
                View v = cardContent.getChildAt(i);
                if (v.getTag() == null) {
                    Log.d("AerialAssault", v.getClass().toString());
                    continue;
                }

                int pos = (Integer) v.getTag();
                data.set(pos, fieldLookup.get(pos).getValue());
            }
        }

        ArrayList<String> strings = new ArrayList<>();
        for (Object o : data) strings.add(o.toString());

        ((ScoutingApplication) getApplication()).teamsList.get(team).matches.get(match).data = strings; //direct Reference to mMatch
        ((ScoutingApplication) getApplication()).saveAll();

        Toast.makeText(getBaseContext(), "Match Saved!", Toast.LENGTH_SHORT).show();

    }

}

	
