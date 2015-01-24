package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.thing342.layoutbasedscouting.Field;
import com.thing342.layoutbasedscouting.R;
import com.thing342.layoutbasedscouting.ScoutingApplication;
import com.thing342.layoutbasedscouting.util.Instantiable;
import com.thing342.layoutbasedscouting.util.Instantiable.InstantiableInteger;

import org.w3c.dom.Element;

public class Counter extends Field<InstantiableInteger>
{

    static {
        ScoutingApplication.addField("counter", Counter.class);
    }

    private final int resId = R.layout.counter;
    private int value = 0;
    private String name;
    private TextView countText;

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

    public InstantiableInteger getValue()
    {
        InstantiableInteger i = new InstantiableInteger();
        i.value = this.value;
        return i;
    }

    @Override
    public InstantiableInteger parse(String value)
    {
        InstantiableInteger instantiableInteger = new InstantiableInteger();
        instantiableInteger.value = Integer.parseInt(value);
        return instantiableInteger;
    }

    private void add()
    {
        value++;
    }

    private void minus()
    {
        value--;
    }

    private TextView getCountText()
    {
        return countText;
    }

    @Override
    public View getView(Context context, Instantiable.InstantiableInteger initValue)
    {
        value = initValue.value;

        View v = LayoutInflater.from(context).inflate(resId, null);
        ((TextView) v.findViewById(R.id.field_name)).setText(name);

        countText = (TextView) v.findViewById(R.id.field_value);
        countText.setText(Integer.toString(getValue().value));

        Button plusButton = (Button) v.findViewById(R.id.counterbuttonplus);
        Button minusButton = (Button) v.findViewById(R.id.counterbuttonminus);

        plusButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                add();
                getCountText().setText(Integer.toString(getValue().value));
                Log.d("Button", "plus");
            }
        });
        minusButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                minus();
                getCountText().setText(Integer.toString(getValue().value));
                Log.d("Button", "minus");
            }
        });
        return v;
    }

    @Override
    public void setUp(Element e)
    {
        this.value = Integer.parseInt(e.getAttribute("initValue"));
        this.name = e.getAttribute("name");
    }


}
