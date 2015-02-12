package com.thing342.layoutbasedscouting.recyclerush;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wes on 1/12/15.
 */
public class ToteStack
{
    private int totes = 0;
    private boolean can, noodle;

    public ToteStack()
    {
        //Empty
    }

    ToteStack(int totes, boolean can, boolean noodle)
    {
        this.totes = totes;
        this.can = can;
        this.noodle = noodle;
    }

    public int getPoints()
    {
        int points = 0;
        if (can) points += 6 * totes;
        else points += 2 * totes;
        if (noodle) points += 6;
        return points;
    }

    public void addTote()
    {
        if (totes < 6) totes++;
    }

    public void removeTote()
    {
        if (totes > 0) totes--;
    }

    public int getTotes()
    {
        return totes;
    }

    public boolean hasNoodle()
    {
        return noodle;
    }

    public void setNoodle(boolean noodle)
    {
        this.noodle = noodle;
    }

    public boolean hasCan()
    {
        return can;
    }

    public void setCan(boolean can)
    {
        this.can = can;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < totes; i++) builder.append("T");
        if (noodle) builder.append("N");
        else if (can) builder.append("C");
        return builder.toString();
    }

    public JSONObject toJSON() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("height", totes);
        json.put("can", can);
        json.put("noodle", noodle);
        return json;
    }
}
