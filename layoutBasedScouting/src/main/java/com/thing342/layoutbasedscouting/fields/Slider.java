package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.thing342.layoutbasedscouting.Field;
import com.thing342.layoutbasedscouting.Instantiable;
import com.thing342.layoutbasedscouting.R;
import com.thing342.layoutbasedscouting.ScoutingApplication;

import org.w3c.dom.Element;

/**
 * Created by wes on 1/10/15.
 */
public class Slider extends Field<Slider.InstantiableDouble>
{

    private final int resId = R.layout.slider;
    private double min = 0, max = 100, value = 50, step = 1;
    private SeekBar seekBar;
    private TextView fieldValue;
    private String name = "";

    static {
        ScoutingApplication.addField("slider", Slider.class);
    }

    public Slider()
    {
        super(new InstantiableDouble());
    }

    @Override
    public View getView(Context context, InstantiableDouble initValue)
    {
        value = initValue.value;

        View v = LayoutInflater.from(context).inflate(resId, null);

        ((TextView) v.findViewById(R.id.field_min_value)).setText(Double.toString(min));
        ((TextView) v.findViewById(R.id.field_max_value)).setText(Double.toString(max));

        fieldValue = (TextView) v.findViewById(R.id.field_value);
        fieldValue.setText(name + " " + Double.toString(value));

        seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        seekBar.setMax((int) ((max - min) / step));
        seekBar.setProgress((int) scaleValue(value));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                fieldValue.setText(name + " " + getValue().toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                //Do nothing!
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                //Do nothing!
            }
        });


        return v;
    }

    @Override
    public void setUp(Element e)
    {
        min = Double.parseDouble(e.getAttribute("min"));
        max = Double.parseDouble(e.getAttribute("max"));
        step = Double.parseDouble(e.getAttribute("step"));
        value = Double.parseDouble(e.getAttribute("initValue"));
    }

    @Override
    public InstantiableDouble getValue()
    {
        InstantiableDouble d = new InstantiableDouble();
        if (seekBar != null) d.value = (seekBar.getProgress() * step) + min;
        return d;
    }

    private double scaleValue(double val)
    {
        return (val - min) / step;
    }

    public static class InstantiableDouble implements Instantiable<Double>
    {
        public double value = 0.0;

        @Override
        public Double getEntry()
        {
            return value;
        }

        @Override
        public void setValue(Object value)
        {
            try {
                this.value = Double.parseDouble((String) value);
            } catch (ClassCastException r) {
                this.value = (Double) value;
            }
            //this.value = (Double) value;
        }

        @Override
        public String toString()
        {
            return Double.toString(value);
        }
    }
}
