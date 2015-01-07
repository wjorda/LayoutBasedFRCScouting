package com.thing342.layoutbasedscouting;

import java.io.File;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.BaseAdapter;
import android.widget.Toast;
import ar.com.daidalos.afiledialog.FileChooserDialog;

public class PreferenceHelperActivity extends FragmentActivity {
	
	/*
	 * Used to handle intents sent by SettingsActivity
	 */
	
	AerialAssaultApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		app = ((AerialAssaultApplication)getApplication());
		Intent i = getIntent();
		Log.d("AerialAssault", "PHelper");
		setupActionBar();
		
		//dialog.setFilter("*.csv");
		if (i.getType().contains(MimeTypes.csv.contentType)) {
			FileChooserDialog dialog = new FileChooserDialog(this);
			dialog.setFilter(".*csv|.*3helix");
			dialog.setTitle("Select Match Schedule...");
		    dialog.show();
		    
		    
		    dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
		         public void onFileSelected(Dialog source, File file) {
		             source.hide();
		             Toast toast = Toast.makeText(source.getContext(), "Schedule Changed", Toast.LENGTH_LONG);
		             toast.show();
		             app.createFromFile(file);
		             forceFinish();
		             //((BaseAdapter) getListView().getAdapter()).notifyDataSetChanged();
		         }
		         public void onFileSelected(Dialog source, File folder, String name) {
		             source.hide();
		             Toast toast = Toast.makeText(source.getContext(), "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
		             toast.show();
		         }
		     });
			}
		else if (i.getType().contains(MimeTypes.xml.contentType)) {
			FileChooserDialog dialog = new FileChooserDialog(this);
			dialog.setFilter(".*xml|.*3helix");
			dialog.setTitle("Select Layout...");
			dialog.show();
			dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
		         public void onFileSelected(Dialog source, File file) {
		             source.hide();
		             Toast toast = Toast.makeText(source.getContext(), "Layout Changed!" + file.getName(), Toast.LENGTH_LONG);
		             toast.show();
		             app.changeLayout(file);
		             forceFinish();
		             //((BaseAdapter) getListView().getAdapter()).notifyDataSetChanged();
		         }
		         public void onFileSelected(Dialog source, File folder, String name) {
		             source.hide();
		             Toast toast = Toast.makeText(source.getContext(), "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
		             toast.show();
		         }
		     });
		}
		else if(i.getType().contains(MimeTypes.$323.contentType)) {
			checkClear();
		}
		
		else forceFinish();
	}
	
	public void checkClear() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
      	builder
      	.setTitle("Remove All Match Data??")
      	.setMessage("THIS CANNOT BE UNDONE.")
      	.setIcon(android.R.drawable.ic_dialog_alert)
      	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      	    public void onClick(DialogInterface dialog, int which) {			      	
      	    	//Yes button clicked, do something
                app.clearAll();
                forceFinish();
      	    }
      	})
      	.setNegativeButton("No", null)						//Do nothing on no
      	.show();
		
	}
	
	@Override
	public void onBackPressed() {
		forceFinish();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
			SharedPreferences prefs = getSharedPreferences("com.thing342.layoutbasedscouting", Context.MODE_PRIVATE); 
			DeviceId restoredId = DeviceId.getFromValue(prefs.getString("com.thing342.layoutbasedscouting.deviceid", "0"));
			setTitle("Scouting Settings");
			
			if(android.os.Build.VERSION.SDK_INT >= 11) {
				getActionBar().setBackgroundDrawable(new ColorDrawable(restoredId.hexColor));
			}
		}
	}
	
	protected void forceFinish() { finish(); }

}
