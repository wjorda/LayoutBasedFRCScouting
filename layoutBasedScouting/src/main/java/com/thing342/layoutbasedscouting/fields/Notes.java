package com.thing342.layoutbasedscouting.fields;

import com.thing342.layoutbasedscouting.R;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class Notes extends Field<String> {
	
	private String hint;
	private int resId = R.layout.note;
	
	public Notes(String hint) {
		super("");
		this.hint = hint;
	}

	public View getView(Context context, String initValue) {
		View v = LayoutInflater.from(context).inflate(resId, null);
		((EditText) v.findViewById(R.id.value)).setHint(hint);
		((EditText) v.findViewById(R.id.value)).setText(initValue);
		return v;
	}

	public String getName() { return hint; }
	
	public void putValue(String key, Editor prefs) {
		prefs.putString(key, "string");
	}
}
