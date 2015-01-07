package com.thing342.layoutbasedscouting;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class TeamViewerActivity extends SherlockFragmentActivity {
	
	private AerialAssaultApplication app;
	private FRCTeam mTeam;
	private MatchGroup mGroup;
	private FRCTeam teams[];
	private int teamPos;
	private boolean matchesFirst;
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_viewer);
		app = ((AerialAssaultApplication)getApplication());
		teamPos = getIntent().getIntExtra("AERIALASSAULT_TEST_FRCTEAM", 0);
		matchesFirst = getIntent().getBooleanExtra("AERIALASSAULT_TEST_MATCHESFIRST", true);
		
		if(matchesFirst) {
			mGroup = app.groups.get(teamPos);
			teams = app.getTeamsWithMatch(mGroup.num);
			if(teams.length == 1) editMatch(0);
			ArrayAdapter<FRCTeam> arrayAdapter = new ArrayAdapter<FRCTeam>(this, android.R.layout.simple_list_item_1, teams);
			listView = (ListView) findViewById(R.id.listView1);
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	        listView.setAdapter(arrayAdapter);
	        listView.setClickable(true);
	        setupActionBar();
	        
	        
		} else {
			mTeam = app.teamsList.get(teamPos);
			if(mTeam.matches.size() == 1) editMatch(0);
			ArrayAdapter<Match> arrayAdapter = new ArrayAdapter<Match>(this, android.R.layout.simple_list_item_1, mTeam.matches);
			listView = (ListView) findViewById(R.id.listView1);
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	        listView.setAdapter(arrayAdapter);
	        listView.setClickable(true);
	        setupActionBar();

	        for(int i = 0; i < mTeam.matches.size(); i++) {
	        	if (mTeam.matches.get(i).isEdited()) listView.setItemChecked(i, true);
	        	((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
	        }
		}
		
		
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      	  @Override
      	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
      		  	editMatch(position);	  
      	  }
      	});
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		
		if(matchesFirst) setTitle(mGroup.toString());
		else setTitle(mTeam.toString());
		
		//setTitle("Select a Team or Match: ");
		
		if(android.os.Build.VERSION.SDK_INT >= 5) {
			SharedPreferences prefs = getSharedPreferences(AerialAssaultApplication.PREFS, Context.MODE_PRIVATE); 
			DeviceId restoredId = DeviceId.getFromValue(prefs.getString(AerialAssaultApplication.DEVICEID_PREF, "0"));
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(restoredId.hexColor));
		}

	}
	
	private void editMatch(int pos) {
		Intent i = new Intent(this, MatchEditorActivity.class);
		//i.putExtra("AERIALASSAULT_TEST_FRCTEAMMATCH", mTeam.matches.get(pos));
		if(matchesFirst){
			
			FRCTeam thisTeam = teams[pos];
			Log.d("AerialAssault", Integer.toString(pos));
			
			i.putExtra("AERIALASSAULT_TEST_FRCMATCH", thisTeam.getMatchPos(mGroup.num));
			i.putExtra("AERIALASSAULT_TEST_FRCTEAMMATCH", app.getTeamPos(thisTeam.number));
		}
		else {
			i.putExtra("AERIALASSAULT_TEST_FRCMATCH", pos);
			i.putExtra("AERIALASSAULT_TEST_FRCTEAMMATCH", teamPos);
		}
		startActivity(i);
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_viewer, menu);
		return true;
	}*/
	
	@Override
	protected void onPause(){
		super.onPause();
		//app.onPause();
	}


}
