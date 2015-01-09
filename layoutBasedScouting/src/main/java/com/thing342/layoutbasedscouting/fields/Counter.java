package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.thing342.layoutbasedscouting.R;

import org.w3c.dom.Element;

public class Counter extends Field<Instantiable.InstantiableInteger>
{

    private int value = 0;
    private int resId = R.layout.counter;
    private String name;

    public Counter()
    {
        super(new Instantiable.InstantiableInteger());
    }

    public Counter(int initValue, String name)
    {
        super(new Instantiable.InstantiableInteger());
        this.value = initValue;
        this.name = name;
    }

    public Integer getValue()
    {
        return new Integer(value);
    }

    @Override
    public View getView(Context context, Instantiable.InstantiableInteger initValue)
    {
        View v = LayoutInflater.from(context).inflate(resId, null);
        ((TextView) v.findViewById(R.id.field_name)).setText(name);
        ((TextView) v.findViewById(R.id.field_value)).setText(Integer.toString(initValue.value));
        return v;
    }

    @Override
    public void setUp(Element e)
    {
        this.value = Integer.parseInt(e.getAttribute("initValue"));
        this.name = e.getAttribute("name");
    }
}
