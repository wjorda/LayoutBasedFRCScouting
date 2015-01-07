package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.thing342.layoutbasedscouting.R;

/**
 * A <code>Field</code> for writing un-formatted text.
 */
public class Notes extends Field<String>
{

    private String hint;
    private int resId = R.layout.note;

    /**
     * @param hint Text that will be displayed if the textbox is empty.
     */
    public Notes(String hint)
    {
        super("");
        this.hint = hint;
    }

    @Override
    public View getView(Context context, String initValue)
    {
        View v = LayoutInflater.from(context).inflate(resId, null);
        ((EditText) v.findViewById(R.id.value)).setHint(hint);
        ((EditText) v.findViewById(R.id.value)).setText(initValue);
        return v;
    }

    /**
     * @return The text that is displayed if the textbox is left empty.
     */
    public String getName()
    {
        return hint;
    }
}
