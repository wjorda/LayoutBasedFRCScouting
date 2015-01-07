package com.thing342.layoutbasedscouting.fields;

import com.thing342.layoutbasedscouting.R;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class Checkbox extends Field<Boolean> {
	
	private boolean value = false;
	private String name;
	private int resId = R.layout.checkbox;
	
	public Checkbox(String name) {
		super(false);
		this.name = name;
	}
	
	public String getName() {return name;}

	public View getView(Context context, Boolean initValue) {
		View v = LayoutInflater.from(context).inflate(resId, null);
		((TextView) v.findViewById(R.id.field_name)).setText(name);
		((CheckBox) v.findViewById(R.id.field_value)).setChecked(initValue);
		return v;
	}

	public void putValue(String key, Editor prefs) {
		prefs.putBoolean(key, value);
	}

}
