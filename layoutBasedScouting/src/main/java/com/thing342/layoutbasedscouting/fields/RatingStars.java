package com.thing342.layoutbasedscouting.fields;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.thing342.layoutbasedscouting.Field;
import com.thing342.layoutbasedscouting.R;
import com.thing342.layoutbasedscouting.ScoutingApplication;
import com.thing342.layoutbasedscouting.util.Instantiable.InstantiableRating;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;

public class RatingStars extends Field<InstantiableRating>
{

    private final int resId = R.layout.ratings;
    private String name;
    private int stars;
    private RatingBar ratingBar;
    private String id = "";

    public RatingStars()
    {
        super(new InstantiableRating());
    }

    public RatingStars(String name, int stars)
    {
        super(new InstantiableRating());
        this.name = name;
        this.stars = stars;
    }

    static {
        ScoutingApplication.addField("rating", RatingStars.class);
    }

    public String getName()
    {
        return name;
    }

    @Override
    public View getView(Context context, InstantiableRating initValue)
    {
        View v = LayoutInflater.from(context).inflate(resId, null);
        ((TextView) v.findViewById(R.id.field_name)).setText(name);

        ratingBar = (RatingBar) v.findViewById(R.id.field_value);
        ratingBar.setNumStars(stars);
        ratingBar.setRating(initValue.value.value);
        return v;
    }

    @Override
    public void setUp(Element e)
    {
        name = e.getAttribute("name");
        stars = Integer.parseInt(e.getAttribute("scale"));
        id = e.getAttribute("id");
    }

    @Override
    public InstantiableRating getValue()
    {
        InstantiableRating instantiableRating = new InstantiableRating();
        instantiableRating.value = Rating.parseValue(ratingBar.getRating());
        return instantiableRating;
    }

    @Override
    public InstantiableRating parse(String value)
    {
        InstantiableRating instantiableRating = new InstantiableRating();
        instantiableRating.value = Rating.parse(value);
        return instantiableRating;
    }

    @Override
    public InstantiableRating parse(JSONObject value)
    {
        InstantiableRating instantiableRating = new InstantiableRating();
        instantiableRating.value = Rating.parse(value.optString("rating", "s_1"));
        return instantiableRating;
    }

    @Override
    protected JSONObject getJSON(boolean flag) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("rating", getValue().value);
        return json;
    }

    @Override
    public String getId()
    {
        return id;
    }

}
