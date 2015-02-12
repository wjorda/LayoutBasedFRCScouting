package org.team2363.bluealliance;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BlueAllianceAPIClient
{
    private final String appId;

    public BlueAllianceAPIClient(String appId)
    {
        this.appId = appId;
    }

    public Team teamRequest(int number)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/team/frc" + number,
                    code = "team_" + number;

            JSONObject jsonTeam = new JSONObject(getHTML(url));
            return new Team(jsonTeam);
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public Event[] teamEventRequest(int team, int year)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/team/frc" + team + "/" + year + "/events";

            JSONArray jsonEvents = new JSONArray(getHTML(url));
            Event[] events = new Event[jsonEvents.length()];
            for (int i = 0; i < events.length; i++)
                events[i] = new Event(jsonEvents.getJSONObject(i), false);

            return events;
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public Award[] teamEventAwardRequest(int team, int year, String eventCode)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/team/frc" + team + "/event/" + year + eventCode + "/awards";
            JSONArray jsonAwards = new JSONArray(getHTML(url));
            Award[] awards = new Award[jsonAwards.length()];

            for (int i = 0; i < awards.length; i++)
                awards[i] = new Award(jsonAwards.getJSONObject(i));

            return awards;
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public Match[] teamEventMatchRequest(int team, int year, String eventCode)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/team/frc" + team + "/event/" + year + eventCode + "/matches";
            JSONArray jsonMatches = new JSONArray(getHTML(url));
            Match[] matches = new Match[jsonMatches.length()];

            for (int i = 0; i < matches.length; i++)
                matches[i] = new Match(jsonMatches.getJSONObject(i));

            return matches;
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public int[] teamYearsParticipatedRequest(int team)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/team/frc" + team + "/years_participated";
            JSONArray jsonYears = new JSONArray(getHTML(url));
            int[] years = new int[jsonYears.length()];
            for (int i = 0; i < years.length; i++) years[i] = jsonYears.getInt(i);
            return years;
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public TeamMedia[] teamMediaRequest(int team, int year)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/team/frc" + team + "/" + year + "/media";
            JSONArray jsonMedia = new JSONArray(getHTML(url));
            TeamMedia[] media = new TeamMedia[jsonMedia.length()];
            for (int i = 0; i < media.length; i++)
                media[i] = new TeamMedia(jsonMedia.getJSONObject(i));
            return media;
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public Event[] eventListRequest(int year)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/events/" + year;
            JSONArray jsonEvents = new JSONArray(getHTML(url));
            Event[] events = new Event[jsonEvents.length()];
            for (int i = 0; i < events.length; i++)
                events[i] = new Event(jsonEvents.getJSONObject(i), true);
            return events;
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public Event eventRequest(int year, String code)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/event/" + year + code;
            return new Event(new JSONObject(getHTML(url)), true);
        } catch (JSONException e) {
            Log.e("JSON Error", e.getMessage(), e);
            return null;
        }
    }

    public Team[] eventTeamRequest(int year, String code)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/event/" + year + code + "/teams";
            String html = getHTML(url);
            Log.d("", html);

            JSONArray jsonTeams = new JSONArray(html);
            Team[] teams = new Team[jsonTeams.length()];
            for (int i = 0; i < teams.length; i++) teams[i] = new Team(jsonTeams.getJSONObject(i));
            return teams;
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public Match[] eventMatchRequest(int year, String code)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/event/" + year + code + "/matches";
            JSONArray jsonMatches = new JSONArray(getHTML(url));
            Match[] matches = new Match[jsonMatches.length()];
            for (int i = 0; i < matches.length; i++)
                matches[i] = new Match(jsonMatches.getJSONObject(i));
            return matches;
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public EventStats eventStatsRequest(int year, String code)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/event/" + year + code + "/stats";
            return new EventStats(new JSONObject(getHTML(url)));
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public EventRankings eventRankingsRequest(int year, String code)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/event/" + year + code + "/rankings";
            String html = getHTML(url);
            Log.d("Rankings", html);
            return new EventRankings(new JSONArray(html));
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public Award[] eventRewardRequest(int year, String code)
    {
        try {
            String url = "http://www.thebluealliance.com/api/v2/event/" + year + code + "/awards";
            JSONArray jsonAwards = new JSONArray(getHTML(url));
            Award[] awards = new Award[jsonAwards.length()];
            for (int i = 0; i < awards.length; i++)
                awards[i] = new Award(jsonAwards.getJSONObject(i));
            return awards;

        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public Match singleMatchRequest(int year, String code, Match.MatchCompetitionLevel level, int levelNum, int setNumber)
    {
        try {
            String set = year + code + "_" + level.getTag() + levelNum + "m" + setNumber;
            String url = "http://www.thebluealliance.com/api/v2/match/" + set;
            return new Match(new JSONObject(getHTML(url)));
        } catch (JSONException e) {
            Log.e("JSON Error", "JSON Exception)", e);
            return null;
        }
    }

    public String getHTML(String url)
    {
        URL oracle;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        StringBuilder result = new StringBuilder();
        try {
            oracle = new URL(url);
            conn = (HttpURLConnection) oracle.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("X-TBA-App-Id", appId);
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result.append(line).append("\n");
            }
            rd.close();
        } catch (IOException e) {
            Log.e("IOEXCEPTION", "IOEXCEPTION", e);
        }
        return result.toString();
    }

    public String getAppId()
    {
        return appId;
    }
}
