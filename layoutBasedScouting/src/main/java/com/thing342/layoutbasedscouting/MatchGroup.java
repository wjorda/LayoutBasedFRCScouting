package com.thing342.layoutbasedscouting;

import android.support.annotation.NonNull;

public class MatchGroup implements Comparable<MatchGroup>
{
    public static final int NULL = Byte.MIN_VALUE;

    public final int num;
    public final int teams;
    private final FRCTeam rTeams;

    public MatchGroup(int num, FRCTeam... teams)
    {
        if (teams.length > 1)
            throw new IllegalArgumentException("MatchGroup no longer supports multiple teams. Please limit to one parameter team.");
        else if (teams.length == 1) {
            this.rTeams = teams[0];
            this.teams = rTeams.number;
            this.num = num;
        } else {
            this.num = NULL;
            this.teams = NULL;
            rTeams = null;
        }
    }

    @Override
    public String toString()
    {
        return "Match Q" + Integer.toString(num);
    }

    public boolean isEdited()
    {
        return rTeams.isEdited();
    }

    public boolean isComplete()
    {
        return rTeams.isEdited();
    }

    public Match getMatch(int index)
    {
        return rTeams.getMatch(num);
    }

    @Override
    public int compareTo(@NonNull MatchGroup another)
    {
        return num - another.num;
    }
}