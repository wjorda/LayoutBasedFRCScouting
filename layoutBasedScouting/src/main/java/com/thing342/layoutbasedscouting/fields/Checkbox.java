package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.thing342.layoutbasedscouting.R;
import com.thing342.layoutbasedscouting.fields.Instantiable.InstantiableBoolean;

import org.w3c.dom.Element;

/**
 * A basic checkbox field for scouting boolean data.
 */
public class Checkbox extends Field<InstantiableBoolean>
{
    private String name;
    private int resId = R.layout.checkbox;
    private CheckBox checkBox;

    public Checkbox()
    {
        super(new InstantiableBoolean());
    }

    /**
     * Constructor for a <code>Checkbox</code> with text.
     *
     * @param name The text of label for this <code>Checkbox</code>.
     */
    public Checkbox(String name)
    {
        super(new InstantiableBoolean());
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
    public View getView(Context context, InstantiableBoolean initValue)
    {
        View v = LayoutInflater.from(context).inflate(resId, null);
        ((TextView) v.findViewById(R.id.field_name)).setText(name);
        checkBox = ((CheckBox) v.findViewById(R.id.field_value));
        checkBox.setChecked(initValue.value);
        return v;
    }

    @Override
    public void setUp(Element e)
    {
        name = new String();
        name = e.getAttribute("name");
    }

}
