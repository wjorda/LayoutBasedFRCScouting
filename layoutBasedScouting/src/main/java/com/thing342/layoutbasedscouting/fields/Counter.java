package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.thing342.layoutbasedscouting.R;

public class Counter extends Field<Integer> {
	
	private int value = 0;
	private int resId = R.layout.counter;
	private String name;
	
	public Integer getValue() {
		return new Integer(value);
	}
	
	public Counter(int initValue, String name) {
		super(0);
		this.value = initValue;
		this.name = name;
	}
	
	public View getView(Context context, Integer initValue) {
		View v = LayoutInflater.from(context).inflate(resId, null);
		((TextView) v.findViewById(R.id.field_name)).setText(name);
		((TextView) v.findViewById(R.id.field_value)).setText(Integer.toString(initValue));
		return v;
	}

}
