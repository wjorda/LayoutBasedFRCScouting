package com.thing342.layoutbasedscouting;

import android.support.annotation.NonNull;

public class MatchGroup implements Comparable<MatchGroup>
{
    public final int num;
    public final int teams[];
    private final FRCTeam rTeams[];

    public MatchGroup(int num, FRCTeam teams[])
    {
        this.teams = new int[teams.length];
        this.rTeams = teams;

        for (int i = 0; i < teams.length; i++) {
            this.teams[i] = teams[i].number;
        }

        this.num = num;
    }

    @Override
    public String toString()
    {
        return "Match Q" + Integer.toString(num);
    }

    public boolean isEdited()
    {
        for (FRCTeam t : rTeams) {
            if (t.getMatch(num).isEdited()) return true;
        }

        return false;
    }

    public boolean isComplete()
    {
        for (FRCTeam t : rTeams) {
            if (!t.getMatch(num).isEdited()) return false;
        }

        return true;
    }

    @Override
    public int compareTo(@NonNull MatchGroup another)
    {
        return num - another.num;
    }
}