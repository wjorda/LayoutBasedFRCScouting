package com.thing342.layoutbasedscouting;

import android.util.Log;

import com.thing342.layoutbasedscouting.util.IterableHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Match implements Comparable<Match>
{

    public final int matchNum;
    private final FRCTeam team;

	/*
    public int autoTopGoal=0, autoBottomGoal=0, autoShotsMissed=0, autoHotGoal=0;
	public boolean autoMobility=false;
	public int teleTopGoal=0, teleBottomGoal=0, teleShotsMissed=0, teleAssists=0, teleTrussShots=0, teleTrussCatches=0;
	public Rating offensiveRating = Rating.NA;
	public Rating defensiveRating = Rating.NA;
	
	public String notes = "";
	public Rating driveTeamRating = Rating.NA;*/

    public ArrayList<String> data = new ArrayList<String>();
    private IterableHashMap<String, JSONObject> jsonData = new IterableHashMap<String, JSONObject>();

    public Match(int num, FRCTeam team)
    {
        this.matchNum = num;
        this.team = team;
        data = new ArrayList<String>();

    }

    public Match(FRCTeam team, String[] record)
    {
        this.team = team;
        data = new ArrayList<String>();
        matchNum = Integer.parseInt(record[0]);

        for (int i = 1; i < record.length; i++) {
            String datum = record[i];
            //if (datum == null) continue;
            data.add(datum);
        }
    }

    public Match(FRCTeam team, JSONObject json) throws JSONException
    {
        matchNum = json.optInt("match_number", 0);
        this.team = team;

        Iterator<String> jsonItr = json.keys();
        while (jsonItr.hasNext()) {
            String key = jsonItr.next();
            if (key.contains("match_number")) continue;
            if (key.contains("team_number")) continue;
            putData(key, json.getJSONObject(key));
        }
    }

    public static boolean isEdited(String[] record)
    {
        for (int i = 2; i < record.length; i++) {
            if (!record[i].isEmpty()) return true;
        }

        return false;
    }

    public void setData(Match m)
    {
        this.data = m.data;
        this.jsonData = m.jsonData;
    }

    public boolean isEdited()
    {
        return (!jsonData.isEmpty());

    }

    public JSONObject export()
    {
        JSONObject json = new JSONObject();
        //Log.d("JSON Data size", jsonData.size() + "");
        ScoutingApplication app = ScoutingApplication.getInstance();
        try {
            json.put("match_number", matchNum);
            json.put("team_number", team.number);

            for (Map.Entry<String, JSONObject> entry : jsonData) {
                //Log.d("JSON Object", entry.getValue().toString());
                json.put(entry.getKey(), entry.getValue());
            }

        } catch (JSONException e) {
            Log.e("Exception", e.getMessage(), e);
        }
        //Log.d("MATCH", json.toString());
        return json;
    }

    public void putData(String id, JSONObject data)
    {
        //Log.d("JSON Data size", jsonData.size() + "");
        jsonData.put(id, data);
    }

    public String[] getRecord(boolean export)
    {

        ArrayList<String> list = new ArrayList<String>();

        list.add(0, Integer.toString(matchNum));
        if (export) list.add(1, Integer.toString(team.number));

        for (Object o : data) {
            if (o instanceof Boolean)
                list.add(Integer.toString(((Boolean) o ? 1 : 0))); //You can thank Joseph for this. Apparently 'true' and 'false' are too hard for Tableau.
            else list.add(o.toString());
        }

        String record[] = new String[list.size()];
        return list.toArray(record);
    }

    public String toString()
    {
        return "Q" + Integer.toString(matchNum);
    }

    public JSONObject getData(String id)
    {
        return jsonData.get(id);
    }

    @Override
    public int compareTo(Match that)
    {
        return this.matchNum - that.matchNum;
    }


    private static final class Parser
    {
        public static boolean integer(String s)
        {
            boolean parsable = true;
            try {
                Integer.parseInt(s);
            } catch (NumberFormatException e) {
                parsable = false;
            }

            return parsable;
        }

        public static boolean bool(String s)
        {
            return (s.contains("alse") || s.contains("rue"));
        }
    }

}
