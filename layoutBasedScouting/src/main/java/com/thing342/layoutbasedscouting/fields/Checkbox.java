package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.thing342.layoutbasedscouting.R;

/**
 * A basic checkbox field for scouting boolean data.
 */
public class Checkbox extends Field<InstantiableTypes.InstantiableBoolean>
{

    private boolean value = false;
    private String name;
    private int resId = R.layout.checkbox;

    /**
     * Constructor for a <code>Checkbox</code> with text.
     *
     * @param name The text of label for this <code>Checkbox</code>.
     */
    public Checkbox(String name)
    {
        super(new InstantiableTypes.InstantiableBoolean());
        this.name = name;
    }

    /**
     * @return The text of label displayed next to this field.
     */
    public String getName()
    {
        return name;
    }

    @Override
    public View getView(Context context, InstantiableTypes.InstantiableBoolean initValue)
    {
        View v = LayoutInflater.from(context).inflate(resId, null);
        ((TextView) v.findViewById(R.id.field_name)).setText(name);
        ((CheckBox) v.findViewById(R.id.field_value)).setChecked(initValue.value);
        return v;
    }


    public void putValue(String key, Editor prefs)
    {
        prefs.putBoolean(key, value);
    }

}
