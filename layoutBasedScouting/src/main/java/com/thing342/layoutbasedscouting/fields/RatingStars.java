package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.thing342.layoutbasedscouting.R;

public class RatingStars extends Field<InstantiableTypes.InstantiableRating>
{

    private int resId = R.layout.ratings;
    private String name;
    private int stars;

    public RatingStars(String name, int stars)
    {
        super(new InstantiableTypes.InstantiableRating());
        this.name = name;
        this.stars = stars;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public View getView(Context context, InstantiableTypes.InstantiableRating initValue)
    {
        View v = LayoutInflater.from(context).inflate(resId, null);
        ((TextView) v.findViewById(R.id.field_name)).setText(name);
        ((RatingBar) v.findViewById(R.id.field_value)).setNumStars(stars);
        ((RatingBar) v.findViewById(R.id.field_value)).setRating(initValue.value.value);
        return v;
    }

}
