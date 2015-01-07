package com.thing342.layoutbasedscouting;

import java.io.IOException;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

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
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockFragmentActivity {
	
	private AerialAssaultApplication app;
	private ListView listView;
	private int currentPos = 0;
	
	/////////////////////////--CONSTRUCTORS--/////////////////////////////////
	
	/////////////////////////--OVERRIDDEN METHODS--///////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		app = ((AerialAssaultApplication)getApplication());
		app.resumeAll();
		app.sort();
		
		setContentView(R.layout.activity_main);
		updateTitle();
		setArrayAdapter();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
		updateTitle();
		setArrayAdapter();
		getListView().setSelection(currentPos);
	}
	
	private void setArrayAdapter() {
		SharedPreferences prefs = getSharedPreferences(app.PREFS, Context.MODE_PRIVATE);
		boolean matchesFirst = prefs.getBoolean(app.MATCHESFIST_PREF, true);
		Log.d("AerialAssault", Boolean.toString(matchesFirst));
		
		if (matchesFirst) {
			ArrayAdapter<MatchGroup> arrayAdapter = new ArrayAdapter<MatchGroup>(
					this, android.R.layout.simple_list_item_1, app.groups);
			//MatchAdapter arrayAdapter = new MatchAdapter (this, app.groups);
			listView = (ListView) findViewById(R.id.teamListView);
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
			listView = (ListView) findViewById(R.id.teamListView);
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
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {           
            
            case R.id.action_schedule_add:
            	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            	MatchAddFragment fragment = MatchAddFragment.newInstance(new MatchAddFragment.OnFinishListener() {
					
					@Override
					public void onFinish() {
						((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
					}
				});
            	
            	fragment.show(ft, "MatchAddFragment");
            	return true;
            
            case R.id.action_team_add:
            	addTeamDialog();
            	((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            	return true;
            	
            case R.id.search:
            	AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        		final EditText field = new EditText(getBaseContext());
        		field.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        		field.setHint("Team Number");
        		field.setTextColor(Color.BLACK);
        		//field.setBackgroundColor(Color.BLACK);
        		
        		alertBuilder
        		.setTitle("Search Team:")
        		.setView(field)
        		.setPositiveButton("Search", new DialogInterface.OnClickListener() {
        			
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
        				searchDialog(field.getText().toString());
        			}
        		})
        		.show();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		return true;
	}
	
	////////////////////////--PUBLIC METHODS--////////////////////////////////
	
	public ListView getListView() {
		return listView;
	}

	public void updateTitle() {
		SharedPreferences prefs = getSharedPreferences(app.PREFS, Context.MODE_PRIVATE); 
		DeviceId restoredId = DeviceId.getFromValue(prefs.getString(app.DEVICEID_PREF, "0"));
		setTitle("Scouting (v1.0.2) - Device ID: " + restoredId.toString());
		
		if(android.os.Build.VERSION.SDK_INT >= 5) {
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(restoredId.hexColor));
		}
		
	}
	
	///////////////////////--PRIVATE METHODS--/////////////////////////////////
	
	private void searchDialog(String query) {
		Intent searchIntent = new Intent(this, TeamSearchActivity.class);
		searchIntent.putExtra("AERIALASSAULT_QUERY", query);
		startActivity(searchIntent);
	}
	
	private void addTeamDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		final EditText field = new EditText(getBaseContext());
		field.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		field.setHint("Team Number");
		field.setTextColor(Color.BLACK);
		
		builder
		.setTitle("Add Team:")
		.setView(field)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getBaseContext(), Integer.toString(Integer.parseInt(field.getText().toString())), Toast.LENGTH_SHORT).show();
				app.addTeam(Integer.parseInt(field.getText().toString()));
				((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
				app.saveAll();
				//app.resumeAll();
			}
		})
		.show();

	}

	
	private void viewTeam(int pos, boolean matchesFirst) {
		
		currentPos = pos;
		
		if(matchesFirst) {
			MatchGroup mGroup = app.groups.get(pos);
			FRCTeam[] teams = app.getTeamsWithMatch(mGroup.num);
			
			if(teams.length == 1) {
				FRCTeam thisTeam = teams[0];
				Log.d("AerialAssault", Integer.toString(pos));
				
				Intent i = new Intent(this, MatchEditorActivity.class);
				i.putExtra("AERIALASSAULT_TEST_FRCMATCH", thisTeam.getMatchPos(mGroup.num));
				i.putExtra("AERIALASSAULT_TEST_FRCTEAMMATCH", app.getTeamPos(thisTeam.number));
				startActivity(i);
				return;
			}
			
	    }
	    	
		Intent i = new Intent(this, TeamViewerActivity.class);
		i.putExtra("AERIALASSAULT_TEST_FRCTEAM", pos);
		i.putExtra("AERIALASSAULT_TEST_MATCHESFIRST", matchesFirst);
		i.putExtra("AERIALASSAULT_TEST_MAINACTIVITY", true);
		startActivity(i);
		return;
	}

}
