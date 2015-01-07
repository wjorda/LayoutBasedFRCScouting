package com.thing342.layoutbasedscouting;

import com.thing342.layoutbasedscouting.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DeviceIdFragment extends DialogFragment {
	
	private View v;
	private DeviceId dID = DeviceId.OTHER;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		getDialog().setTitle("Set Device ID: ");
		v = inflater.inflate(R.layout.fragment_device_id, container, false);

		 try {
			SharedPreferences prefs = getActivity().getSharedPreferences("com.thing342.layoutbasedscouting", Context.MODE_PRIVATE); 
			DeviceId restoredId = DeviceId.getFromValue(prefs.getInt("com.thing342.layoutbasedscouting.deviceid", 0));
			 Log.d("AerialAssault", restoredId.filename);
			
			switch (restoredId) {
				case RED_1:
					((RadioButton) v.findViewById(R.id.red1)).setChecked(true);
					((RadioButton) v.findViewById(R.id.red2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.otherRadio)).setChecked(false);
					break;
				case RED_2:
					((RadioButton) v.findViewById(R.id.red1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red2)).setChecked(true);
					((RadioButton) v.findViewById(R.id.red3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.otherRadio)).setChecked(false);
					break;
				case RED_3:
					((RadioButton) v.findViewById(R.id.red1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red3)).setChecked(true);
					((RadioButton) v.findViewById(R.id.blue1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.otherRadio)).setChecked(false);
					break;
				case BLUE_1:
					((RadioButton) v.findViewById(R.id.red1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue1)).setChecked(true);
					((RadioButton) v.findViewById(R.id.blue2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.otherRadio)).setChecked(false);
					break;
				case BLUE_2:
					((RadioButton) v.findViewById(R.id.red1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue2)).setChecked(true);
					((RadioButton) v.findViewById(R.id.blue3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.otherRadio)).setChecked(false);
					break;
				case BLUE_3:
					((RadioButton) v.findViewById(R.id.red1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue3)).setChecked(true);
					((RadioButton) v.findViewById(R.id.otherRadio)).setChecked(false);
					break;
				default:
					((RadioButton) v.findViewById(R.id.red1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.red3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue1)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue2)).setChecked(false);
					((RadioButton) v.findViewById(R.id.blue3)).setChecked(false);
					((RadioButton) v.findViewById(R.id.otherRadio)).setChecked(true);
					break;
				}
			} catch (NullPointerException npe) {
				((RadioButton) v.findViewById(R.id.red1)).setChecked(false);
				((RadioButton) v.findViewById(R.id.red2)).setChecked(false);
				((RadioButton) v.findViewById(R.id.red3)).setChecked(false);
				((RadioButton) v.findViewById(R.id.blue1)).setChecked(false);
				((RadioButton) v.findViewById(R.id.blue2)).setChecked(false);
				((RadioButton) v.findViewById(R.id.blue3)).setChecked(false);
				((RadioButton) v.findViewById(R.id.otherRadio)).setChecked(true);
			}
		 
		 ((RadioGroup)v.findViewById(R.id.idgroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				onRadioButtonClicked(v.findViewById(checkedId));
			}
		});
		 
		 v.findViewById(R.id.buttonId).setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	getActivity().getSharedPreferences("com.thing342.layoutbasedscouting", Context.MODE_PRIVATE).edit()
			    		.putInt("com.thing342.layoutbasedscouting.deviceid", dID.value).commit();
	            	((MainActivity) getActivity()).updateTitle();
	            	dismiss();
	            }
			});   

		 return v;
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	   
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.blue1:
	            if (checked)
	                dID = DeviceId.BLUE_1;
	            break;
	        case R.id.blue2:
	            if (checked)
	                dID = DeviceId.BLUE_2;
	            break;
	        case R.id.blue3:
	            if (checked)
	                dID = DeviceId.BLUE_3;
	            break;
	        case R.id.red1:
	            if (checked)
	                dID = DeviceId.RED_1;
	            break;
	        case R.id.red2:
	            if (checked)
	                dID = DeviceId.RED_2;
	            break;
	        case R.id.red3:
	            if (checked)
	                dID = DeviceId.RED_3;
	            break;
	        case R.id.otherRadio:
	            if (checked)
	                dID = DeviceId.OTHER;
	            break;   
	    }
	    
	    getActivity().getSharedPreferences("com.thing342.layoutbasedscouting", Context.MODE_PRIVATE).edit()
		.putInt("com.thing342.layoutbasedscouting.deviceid", dID.value).commit();
	    
	    Toast.makeText(getActivity().getBaseContext(), "Device ID is " + Integer.toString(dID.value), Toast.LENGTH_SHORT).show();

	}
}
