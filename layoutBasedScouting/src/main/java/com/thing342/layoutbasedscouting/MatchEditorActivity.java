package com.thing342.layoutbasedscouting;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.thing342.layoutbasedscouting.fields.Field;
//import android.app.ActionBar;
//import android.view.Menu;
//import android.view.MenuItem;

public class MatchEditorActivity extends SherlockActivity {
	
	private AerialAssaultApplication app;
	private Match mMatch;
	private FRCTeam mTeam;
	
	private int teamPos=0;
	private int matchPos=0;
	private static final transient int vibLength = 500; //Time to vibrate when field is changed.
	
	LinearLayout layout;
	
	/*private int autoTopGoal=0, autoBottomGoal=0, autoShotsMissed=0, autoHotGoal=0;
	private boolean autoMobility=false;
	private int teleTopGoal=0, teleBottomGoal=0, teleShotsMissed=0, teleAssists=0, teleTrussShots=0, teleTrussCatches=0;
	private Rating offensiveRating=Rating.NA, defensiveRating=Rating.NA, driveTeamRating=Rating.NA;*/
	
	public ArrayList<Field> dataset;
	public ArrayList<Object> data;
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = ((AerialAssaultApplication)getApplication());
		setContentView(R.layout.activity_match_editor);
		//setupSlidingMenu();
		
		Bundle b = getIntent().getExtras();
		//mMatch = b.getParcelable("AERIALASSAULT_TEST_FRCTEAMMATCH");
		teamPos = b.getInt("AERIALASSAULT_TEST_FRCTEAMMATCH");
		matchPos = b.getInt("AERIALASSAULT_TEST_FRCMATCH");
		
		mTeam = app.teamsList.get(teamPos);
		mMatch = mTeam.matches.get(matchPos);
		data = new ArrayList<Object>();
		//fillSavedData();
		
		setTitle(mTeam.toString() + " - Match " + mMatch.toString());
		
		layout = (LinearLayout) findViewById(R.id.matchEditorLayout);
		//this.data = mMatch.data;
		
		Field f;
		int pos = 0;
		for(int i = 0; i < app.data.size(); i++) {
			f = app.data.get(i);
			
			Object newEntry;
			View thisView = null;
			
			if(f.getType() == null) {
				thisView = f.getView(getBaseContext(), null);
			} 
			
			else {
				if(!(mMatch.data == null || mMatch.data.size()==0)) newEntry = mMatch.data.get(pos);
				
				else try {
						newEntry = f.getType().newInstance();
					} catch (InstantiationException e) { //if default constructor DNE
		
						String classname = f.getType().toString();
						Log.d("AerialAssault", classname);
						
						if(classname.contains("class java.lang.Integer")) { //Counter
							newEntry = new Integer(0);
							Log.d("AerialAssault", "New Int");
						}
						else if(classname.contains("class java.lang.Boolean")) { //Checkbox
							newEntry = new Boolean(false);
							Log.d("AerialAssault", "New Bool");
						}
						else if (classname.contains("Rating")) { //RatingBar
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
				
					data.add((Object) newEntry);
					try {thisView = f.getView(getBaseContext(), f.getType().cast(newEntry));}
					catch (ClassCastException e) {
						Log.e("Scouting", "Error generating field " + i );
						Log.e("Scouting", "Stack: " + e.getLocalizedMessage());
						
						if(newEntry instanceof Integer) thisView = f.getView(getBaseContext(), ((Integer) newEntry > 0)); //Manual boolean casting
					}
					if(thisView != null) thisView.setTag(pos);
					pos++;
			}

			if(thisView!=null)layout.addView(thisView);
			else Log.d("Scouting", "Field" + i + " is null");
			//Log.d("AerialAssault", "I SEE YOU VIEWER");
		}
		
		if(android.os.Build.VERSION.SDK_INT >= 5) {
			SharedPreferences prefs = getSharedPreferences(AerialAssaultApplication.PREFS, Context.MODE_PRIVATE); 
			DeviceId restoredId = DeviceId.getFromValue(prefs.getString(AerialAssaultApplication.DEVICEID_PREF, "0"));
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(restoredId.hexColor));
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		saveMatch();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.match_editor, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                saveMatch();
                return true;
            /*case R.id.action_settings:
                openSettings();
                return true;*/
            case android.R.id.home:
            	Intent i = new Intent(this, TeamViewerActivity.class);
        		
            	if(getSharedPreferences(AerialAssaultApplication.PREFS, Context.MODE_PRIVATE).getBoolean(AerialAssaultApplication.MATCHESFIST_PREF, true)) {
        			i.putExtra("AERIALASSAULT_TEST_FRCTEAM", mMatch.matchNum);
        			i.putExtra("AERIALASSAULT_TEST_MATCHESFIRST", true);
        		} else {
        			i.putExtra("AERIALASSAULT_TEST_FRCTEAM", app.getTeamPos(mTeam.number));
        			i.putExtra("AERIALASSAULT_TEST_MATCHESFIRST", false);
        		}
            	
    			NavUtils.navigateUpTo(this, i);
    			return false;
    			
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void saveMatch() {
    	
    	checkDataFields:
    	for(int i = 0; i < layout.getChildCount(); i++) {
    		View v = layout.getChildAt(i);
    		
    		try {
    			EditText et = (EditText) v.findViewById(R.id.value);
    			Log.d("AerialAssault", "EditText found at pos " + i);
    			
    			
    			if (et != null) {
    				String text = et.getText().toString();
    				text = text.replace(",", "_");
    				text = text.replace(";", "_");
    				text = text.replace("\"", "_");
    				text = text.replace("\n", "_");
    				data.set((Integer) v.getTag(), text.replaceAll("[-+.^:,] \n","_"));
    				Log.d("AerialAssault", et.getText().toString());
    				Log.d("Scouting", text.replaceAll("[-+.^:,] \n","_"));
    				continue checkDataFields;
    			}
    			
    		} catch (ClassCastException cce) {Log.d("AerialAssault", "CCE at pos " + i);}
    		catch (NullPointerException cce) {Log.d("AerialAssault", "NPE at pos " + i);}
    		
    		try {
    			RatingBar rb = (RatingBar) v.findViewById(R.id.field_value);
    			
    			if (rb != null) {
    				data.set((Integer) v.getTag(), Rating.parseValue(rb.getRating()));
    				continue checkDataFields;
    			}
    			
    		} catch (ClassCastException cce) {}
    	}
	
		((AerialAssaultApplication)getApplication()).teamsList.get(teamPos).matches.get(matchPos).data = data; //direct Reference to mMatch
		((AerialAssaultApplication)getApplication()).saveAll();
		Toast.makeText(getBaseContext(), "Match Saved!", Toast.LENGTH_SHORT).show();
	   
    }
    
	public void incrementCounter(View v) {
		ViewGroup row = (ViewGroup) v.getParent();
		int pos = (Integer) row.getTag();
		if ((Integer) data.get(pos) < Integer.MAX_VALUE) data.set(pos, (Integer) data.get(pos) + 1);
		((TextView) row.getChildAt(2)).setText((data.get(pos)).toString());
		((Vibrator) this.getBaseContext().getSystemService(VIBRATOR_SERVICE)).vibrate(vibLength);
		
		Log.d("AerialAssault", Build.MANUFACTURER + " - " + Build.MODEL);
		
		if (Build.MODEL.contains("Kindle")) {
			MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.data_select);
			mediaPlayer.start();
		}		
	}
	
	public void decrementCounter(View v) {
		ViewGroup row = (ViewGroup) v.getParent();
		int pos = (Integer) row.getTag();
		if ((Integer) data.get(pos) > 0) data.set(pos, (Integer) data.get(pos) - 1);
		((TextView) row.getChildAt(2)).setText((data.get(pos)).toString());
		((Vibrator) this.getBaseContext().getSystemService(VIBRATOR_SERVICE)).vibrate(vibLength);
		
		if (Build.MODEL.contains("Kindle")) {
			MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.data_select);
			mediaPlayer.start();
		}
	}
	
	public void getStars(View v) {
		ViewGroup row = (ViewGroup) v.getParent();
		int pos = (Integer) row.getTag();
		data.set(pos, Rating.parseValue(((RatingBar) v).getRating()));
		Log.d("AerialAssault", Double.toString(((RatingBar) v).getRating()));
		((Vibrator) this.getBaseContext().getSystemService(VIBRATOR_SERVICE)).vibrate(vibLength);
		
		if (Build.MODEL.contains("Kindle")) {
			MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.data_select);
			mediaPlayer.start();
		}
	}
	
	public void toggle(View v) {
		ViewGroup row = (ViewGroup) v.getParent();
		int pos = (Integer) row.getTag();
		boolean checked = ((CheckBox) row.getChildAt(1)).isChecked();
		Log.d("AerialAssault", Boolean.toString(((CheckBox) row.getChildAt(1)).isChecked()));
		data.set(pos, checked);
		((Vibrator) this.getBaseContext().getSystemService(VIBRATOR_SERVICE)).vibrate(vibLength);
		
		if (Build.MODEL.contains("Kindle")) {
			MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.data_select);
			mediaPlayer.start();
		}
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

	