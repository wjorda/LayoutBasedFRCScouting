package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.thing342.layoutbasedscouting.R;

public class Counter extends Field<InstantiableTypes.InstantiableInteger>
{

    private int value = 0;
    private int resId = R.layout.counter;
    private String name;

    public Counter(int initValue, String name)
    {
        super(new InstantiableTypes.InstantiableInteger());
        this.value = initValue;
        this.name = name;
    }

    public Integer getValue()
    {
        return new Integer(value);
    }

    @Override
    public View getView(Context context, InstantiableTypes.InstantiableInteger initValue)
    {
        View v = LayoutInflater.from(context).inflate(resId, null);
        ((TextView) v.findViewById(R.id.field_name)).setText(name);
        ((TextView) v.findViewById(R.id.field_value)).setText(Integer.toString(initValue.value));
        return v;
    }
}
