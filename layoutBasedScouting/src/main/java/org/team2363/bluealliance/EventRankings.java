package org.team2363.bluealliance;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EventRankings
{
    private final String[][] rankings;
    private final Map<Integer, String[]> finishes = new HashMap<Integer, String[]>();

    public EventRankings(JSONArray jsonArray) throws JSONException
    {
        int rows = jsonArray.length() - 1, cols = jsonArray.getJSONArray(1).length();
        rankings = new String[rows][cols];

        Log.d("Arrays", rows + " x " + cols);

        for (int i = 1; i <= rows; i++) {
            JSONArray rowJson = jsonArray.getJSONArray(i);
            for (int j = 0; j < cols; j++) rankings[i - 1][j] = rowJson.getString(j);
        }

        for (String[] row : rankings) {
            Log.d("Arrays", Arrays.toString(row));
            finishes.put(Integer.parseInt(row[1]), row);
        }

    }

    public String[][] getRankings()
    {
        return rankings;
    }

    public String[] getFinish(int team)
    {
        return finishes.get(team);
    }
}
