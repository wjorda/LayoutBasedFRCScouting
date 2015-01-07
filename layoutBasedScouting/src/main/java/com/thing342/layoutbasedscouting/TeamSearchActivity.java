package com.thing342.layoutbasedscouting;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

public class TeamSearchActivity extends SherlockActivity {

	private AerialAssaultApplication app;
	private ArrayList<FRCTeam> results = new ArrayList<FRCTeam>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		app = ((AerialAssaultApplication)getApplication());
		// Show the Up button in the action bar.
		//setupActionBar();
		handleIntent(getIntent());
		
		if(Build.VERSION.SDK_INT >= 5) {
			SharedPreferences prefs = getSharedPreferences(AerialAssaultApplication.PREFS, Context.MODE_PRIVATE); 
			DeviceId restoredId = DeviceId.getFromValue(prefs.getString(AerialAssaultApplication.DEVICEID_PREF, "0"));
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(restoredId.hexColor));
		}
	}
	
	@Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	/*@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void handleIntent(Intent intent) {
		
		ListView listView = (ListView) findViewById(R.id.searchListView);

		ArrayAdapter<FRCTeam> arrayAdapter = new ArrayAdapter<FRCTeam>(this, android.R.layout.simple_selectable_list_item, results);
		TextView emptyText = new TextView(getBaseContext());
        emptyText.setText("No matches Found.");
        emptyText.setTextAppearance(getBaseContext(), android.R.attr.textAppearanceLarge);
        listView.setEmptyView(emptyText);
        listView.setAdapter(arrayAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      	  @Override
      	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
      		viewTeam(position);	  
      	  }
      	});
		
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("Search Query", query);
            setTitle("Search: " + query);
            //use the query to search your data somehow
            results.clear();
            for(FRCTeam team : app.teamsList) if (Integer.toString(team.number).startsWith(query)) results.add(team);
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        } else if (intent.getStringExtra("AERIALASSAULT_QUERY") != null) {
        	String query = intent.getStringExtra("AERIALASSAULT_QUERY");
            Log.d("Workaround Search Query", query);
            setTitle("Search: " + query);
            //use the query to search your data somehow
            results.clear();
            for(FRCTeam team : app.teamsList) if (Integer.toString(team.number).startsWith(query)) results.add(team);
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void viewTeam(int position) {
		FRCTeam thisTeam = results.get(position);
		int pos = app.getTeamPos(thisTeam.number);
		
		Intent i = new Intent(this, TeamViewerActivity.class);
		i.putExtra("AERIALASSAULT_TEST_FRCTEAM", pos);
		startActivity(i);
	}

}
