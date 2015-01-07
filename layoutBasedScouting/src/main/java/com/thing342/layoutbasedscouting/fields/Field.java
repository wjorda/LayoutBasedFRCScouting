package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

public abstract class Field<T> {
	
	private T t;
	private String name;
	
	public Class getType() {
		return t.getClass();
	}
	
	public Field(T t) {
		this.t = t;
	}
	
	public String getName() {return name;}

	public abstract View getView(Context context, T initValue);
	
}
