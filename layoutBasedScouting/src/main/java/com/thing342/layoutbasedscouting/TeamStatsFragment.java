package com.thing342.layoutbasedscouting;

import android.support.v4.app.Fragment;

public class TeamStatsFragment extends Fragment
{
    private FRCTeam team;

    public static TeamStatsFragment getInstance(FRCTeam team)
    {

        TeamStatsFragment tsf = new TeamStatsFragment();
        tsf.team = team;

        return tsf;
    }
}
