package com.thing342.layoutbasedscouting;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
//import android.app.ActionBar;
//import android.view.Menu;
//import android.view.MenuItem;

public class MatchEditorActivity extends ActionBarActivity
{

    private static final transient int vibLength = 500; //Time to vibrate when field is changed.
    public ArrayList<Field> dataset;
    public ArrayList<Object> data;
    LinearLayout layout;
    private ScoutingApplication app;
    private Match mMatch;
    private FRCTeam mTeam;

    /*private int autoTopGoal=0, autoBottomGoal=0, autoShotsMissed=0, autoHotGoal=0;
    private boolean autoMobility=false;
    private int teleTopGoal=0, teleBottomGoal=0, teleShotsMissed=0, teleAssists=0, teleTrussShots=0, teleTrussCatches=0;
    private Rating offensiveRating=Rating.NA, defensiveRating=Rating.NA, driveTeamRating=Rating.NA;*/
    private int team = 0;
    private int match = 0;

    private IterableHashMap<Integer, Field> fieldLookup = new IterableHashMap<Integer, Field>();

    @SuppressWarnings("rawtypes")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        app = ((ScoutingApplication) getApplication());
        setContentView(R.layout.activity_match_editor);
        //setupSlidingMenu();

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
        for (int i = 0; i < app.data.size(); i++) {
            f = app.data.get(i);

            Object newEntry;
            View thisView = null;

            if (f.getType() == null) {
                thisView = f.getView(this, null);
            } else {
                if (!(mMatch.data == null || mMatch.data.size() == 0))
                    newEntry = mMatch.data.get(pos);

                else try {
                    newEntry = f.getType().newInstance();

                } catch (InstantiationException e) { //if default constructor DNE

                    String classname = f.getType().toString();
                    Log.d("AerialAssault", classname);

                    if (classname.contains("class java.lang.Integer")) { //Counter
                        newEntry = new Integer(0);
                        Log.d("AerialAssault", "New Int");
                    } else if (classname.contains("class java.lang.Boolean")) { //Checkbox
                        newEntry = new Boolean(false);
                        Log.d("AerialAssault", "New Bool");
                    } else if (classname.contains("Rating")) { //RatingBar
                        newEntry = Rating.NA;
                        Log.d("AerialAssault", "New Rating");
                    } else {
                        newEntry = new Integer(0);
                    }
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    newEntry = new Integer(0);
                }

                if (newEntry instanceof Instantiable)
                    newEntry = ((Instantiable) newEntry).getEntry();

                data.add(newEntry);
                Log.d("newEntry Class", newEntry.getClass().getCanonicalName());


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

                } */ catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (thisView != null) {
                    thisView.setTag(pos);
                    fieldLookup.put(pos, f);
                }
                pos++;
            }

            if (thisView != null) layout.addView(thisView);
            else Log.d("Scouting", "Field" + i + " is null");
            //Log.d("AerialAssault", "I SEE YOU VIEWER");
        }

        if (android.os.Build.VERSION.SDK_INT >= 5) {
            SharedPreferences prefs = getSharedPreferences(ScoutingApplication.PREFS, Context.MODE_PRIVATE);
            DeviceId restoredId = DeviceId.getFromValue(prefs.getString(ScoutingApplication.DEVICEID_PREF, "0"));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(restoredId.hexColor));
        }
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveMatch()
    {

        checkDataFields:
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v.getTag() == null) {
                Log.d("AerialAssault", "Field is null!!!!!");
                continue checkDataFields;
            }

            int pos = (Integer) v.getTag();
            data.set(pos, fieldLookup.get(pos).getValue());

            /*try {
                EditText et = (EditText) v.findViewById(R.id.value);
                Log.d("AerialAssault", "EditText found at pos " + i);


                if (et != null) {
                    String text = et.getText().toString();
                    text = text.replace(",", "_");
                    text = text.replace(";", "_");
                    text = text.replace("\"", "_");
                    text = text.replace("\n", "_");
                    data.set((Integer) v.getTag(), text.replaceAll("[-+.^:,] \n", "_"));
                    Log.d("AerialAssault", et.getText().toString());
                    Log.d("Scouting", text.replaceAll("[-+.^:,] \n", "_"));
                    continue checkDataFields;
                }

            } catch (ClassCastException cce) {
                Log.d("AerialAssault", "CCE at pos " + i);
            } catch (NullPointerException cce) {
                Log.d("AerialAssault", "NPE at pos " + i);
            }

            try {
                RatingBar rb = (RatingBar) v.findViewById(R.id.field_value);

                if (rb != null) {
                    data.set((Integer) v.getTag(), Rating.parseValue(rb.getRating()));
                    continue checkDataFields;
                }

            } catch (ClassCastException cce) {
            }*/
        }

        ((ScoutingApplication) getApplication()).teamsList.get(team).matches.get(match).data = data; //direct Reference to mMatch
        ((ScoutingApplication) getApplication()).saveAll();

        Toast.makeText(getBaseContext(), "Match Saved!", Toast.LENGTH_SHORT).show();

    }

	/*private void setupSlidingMenu() { //Add a mini version of MainActivity to a SlidingMen
        SharedPreferences prefs = getSharedPreferences(app.PREFS, Context.MODE_PRIVATE);
		boolean matchesFirst = prefs.getBoolean(app.MATCHESFIST_PREF, true);
		Log.d("AerialAssault", Boolean.toString(matchesFirst));
		View v = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
		ListView listView;
		
		if (matchesFirst) {
			ArrayAdapter<MatchGroup> arrayAdapter = new ArrayAdapter<MatchGroup>(
					this, android.R.layout.simple_list_item_1, app.groups);
			//MatchAdapter arrayAdapter = new MatchAdapter (this, app.groups);
			listView = (ListView) v.findViewById(R.id.teamListView);
			TextView emptyText = new TextView(getBaseContext());
			emptyText.setText("No matches set.");
			emptyText.setTextAppearance(getBaseContext(),
					android.R.attr.textAppearanceLarge);
			listView.setAdapter(arrayAdapter);
			listView.setClickable(true);
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			listView.setLongClickable(true);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					viewTeam(position, true);

				}
			});
		} else {
			ArrayAdapter<FRCTeam> arrayAdapter = new ArrayAdapter<FRCTeam>(
					this, android.R.layout.simple_list_item_1,
					app.teamsList);
			//TeamAdapter arrayAdapter = new TeamAdapter(this, app.teamsList);
			listView = (ListView) v.findViewById(R.id.teamListView);
			TextView emptyText = new TextView(getBaseContext());
			emptyText.setText("No matches set.");
			emptyText.setTextAppearance(getBaseContext(),
					android.R.attr.textAppearanceLarge);
			listView.setAdapter(arrayAdapter);
			listView.setClickable(true);
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			listView.setLongClickable(true);
			for (int i = 0; i < app.teamsList.size(); i++) {
				if (app.teamsList.get(i).isComplete())
					listView.setItemChecked(i, true);
				((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
			}
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					viewTeam(position, false);

				}
			});
		}
		
		SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        //menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.addView(v);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	}
	
		private final void viewTeam(int pos, boolean matchesFirst) {
				Intent i = new Intent(this, TeamViewerActivity.class);
				i.putExtra("AERIALASSAULT_TEST_FRCTEAM", pos);
				i.putExtra("AERIALASSAULT_TEST_MATCHESFIRST", matchesFirst);
				i.putExtra("AERIALASSAULT_TEST_MAINACTIVITY", true);
				startActivity(i);
		}*/


}

	
