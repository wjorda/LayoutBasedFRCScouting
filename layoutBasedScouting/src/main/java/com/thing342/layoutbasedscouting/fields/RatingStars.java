package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.thing342.layoutbasedscouting.R;

import org.w3c.dom.Element;

public class RatingStars extends Field<Instantiable.InstantiableRating>
{

    private int resId = R.layout.ratings;
    private String name;
    private int stars;

    public RatingStars()
    {
        super(new Instantiable.InstantiableRating());
    }

    public RatingStars(String name, int stars)
    {
        super(new Instantiable.InstantiableRating());
        this.name = name;
        this.stars = stars;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public View getView(Context context, Instantiable.InstantiableRating initValue)
    {
        View v = LayoutInflater.from(context).inflate(resId, null);
        ((TextView) v.findViewById(R.id.field_name)).setText(name);
        ((RatingBar) v.findViewById(R.id.field_value)).setNumStars(stars);
        ((RatingBar) v.findViewById(R.id.field_value)).setRating(initValue.value.value);
        return v;
    }

    @Override
    public void setUp(Element e)
    {
        name = e.getAttribute("name");
        stars = Integer.parseInt(e.getAttribute("scale"));
    }

}
