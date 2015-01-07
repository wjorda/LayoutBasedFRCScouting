package com.thing342.layoutbasedscouting.fields;

import com.thing342.layoutbasedscouting.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Divider extends Field<String> {

	private final int resId = R.layout.divider;
	private String name;
	
	public Divider(String name) {
		super("null");
		this.name = name;
	}
	
	@Override
	public Class getType() { //This isn't used to store values, so return null type.
		return null;
	}

	@Override
	public View getView(Context context, String initValue) {
		View v = LayoutInflater.from(context).inflate(resId, null);
		TextView fieldname = (TextView) v.findViewById(R.id.field_name);
		fieldname.setText(name);
		return v;
	}


}
