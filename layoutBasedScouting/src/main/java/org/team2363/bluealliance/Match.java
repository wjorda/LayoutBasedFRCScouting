package org.team2363.bluealliance;

import org.json.JSONException;
import org.json.JSONObject;

public class Match
{
    private final String key;
    private final String set_number;
    private final String match_number;
    private final String event_key;
    private final String time_string;
    private final MatchCompetitionLevel level;
    private final Alliance redAlliance;
    private final Alliance blueAlliance;
    private final long timestamp;

    public Match(JSONObject json) throws JSONException
    {
        this.key = json.getString("key");
        this.set_number = json.get("set_number").toString();
        this.match_number = json.get("match_number").toString();
        this.event_key = json.getString("event_key");
        this.time_string = json.getString("time_string");
        this.level = MatchCompetitionLevel.getInstance(json.getString("comp_level"));
        this.timestamp = json.getLong("time");

        JSONObject alliances = json.getJSONObject("alliances");
        redAlliance = new Alliance(Alliance.RED, false, alliances.getJSONObject("red"));
        blueAlliance = new Alliance(Alliance.BLUE, false, alliances.getJSONObject("blue"));
    }

    public String getKey()
    {
        return key;
    }

    public String getSet_number()
    {
        return set_number;
    }

    public String getMatchKey()
    {
        return match_number;
    }

    public int getMatchNumber()
    {
        String[] split = match_number.split("m");
        return Integer.parseInt(split[split.length - 1]);
    }

    public String getEvent_key()
    {
        return event_key;
    }

    public String getTime_string()
    {
        return time_string;
    }

    public MatchCompetitionLevel getLevel()
    {
        return level;
    }

    public Alliance getTeamAlliance(int team)
    {
        if (redAlliance.hasTeam(team)) return redAlliance;
        if (blueAlliance.hasTeam(team)) return blueAlliance;
        else return null;
    }

    public Alliance getRedAlliance()
    {
        return redAlliance;
    }

    public Alliance getBlueAlliance()
    {
        return blueAlliance;
    }

    public String getTeam(Position position)
    {
        if (position.isRed) return getRedAlliance().getTeams()[position.pos];
        else return getBlueAlliance().getTeams()[position.pos];
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public enum MatchCompetitionLevel
    {
        QUALIFICATION("qm"),
        EINSTEIN("ef"),
        QUARTERFINALS("qf"),
        SEMIFINALS("sf"),
        FINALS("f");

        private final String tag;

        private MatchCompetitionLevel(String tag)
        {
            this.tag = tag;
        }

        public static MatchCompetitionLevel getInstance(String tag)
        {
            for (MatchCompetitionLevel l : values())
                if (l.tag.equals(tag)) return l;

            return QUALIFICATION;
        }

        public String getTag()
        {
            return tag;
        }
    }

    public enum Position
    {
        RED1("red1", 0, true),
        RED2("red2", 1, true),
        RED3("red3", 2, true),
        BLUE1("blue1", 0, false),
        BLUE2("blue2", 1, false),
        BLUE3("blue3", 2, false);

        private final String title;
        private final int pos;
        private final boolean isRed;

        private Position(String title, int pos, boolean isRed)
        {
            this.title = title;
            this.pos = pos;
            this.isRed = isRed;
        }

        public static Position getInstance(String parse)
        {
            for (Position p : Position.values()) if (p.title.equals(parse)) return p;
            return null;
        }
    }
}
