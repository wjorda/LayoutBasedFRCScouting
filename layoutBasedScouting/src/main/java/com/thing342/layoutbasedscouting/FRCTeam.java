package com.thing342.layoutbasedscouting;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * An object representing an FRC Team at a competition event. This object contains references to all
 * of a team's matches and scouting data.
 */
public class FRCTeam implements Parcelable, Comparable<FRCTeam>
{
    public static final Parcelable.Creator<FRCTeam> CREATOR = new Parcelable.Creator<FRCTeam>()
    {
        public FRCTeam createFromParcel(Parcel in)
        {
            return new FRCTeam(in);
        }

        public FRCTeam[] newArray(int size)
        {
            return new FRCTeam[size];
        }
    };
    public int number;

	/*public Rating offensiveRating;
    public Rating defensiveRating;*/


    /*public FRCTeam(int number, Rating offensiveRating, Rating defensiveRating, String name) {
        this.number = number;
        this.offensiveRating = offensiveRating;
        this.defensiveRating = defensiveRating;
        this.name = name;
    }*/
    //public String name;
    public IterableHashMap<Integer, Match> matches = new IterableHashMap<Integer, Match>();

    public FRCTeam()
    {
        this.number = 0;
    }

    public FRCTeam(int number, String s)
    {
        this.number = number;
    }

    FRCTeam(Parcel in)
    {
        number = in.readInt();
        for (Object o : in.readArrayList(Match.class.getClassLoader())) {
            Match m = (Match) o;
            matches.put(m.matchNum, m);
        }
    }

    public String toString()
    {
        return "Team " + Integer.toString(number); //+ " - " + name;
    }

    /**
     * Creates a match under the team's record.
     *
     * @param num Number of the match to add.
     */
    public void createMatch(int num)
    {
        matches.put(num, new Match(num, this));
    }

    /**
     * Used to transfer between intents.
     *
     * @param out
     * @param flags
     */
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(number);
        out.writeMap(matches);
    }

    public int describeContents()
    {
        return 0;
    }

    /**
     * Gets a match from this team.
     *
     * @param matchNum The match number to return from this team.
     * @return The match, if it exists. <code>null</code> otherwise.
     */
    public Match getMatch(int matchNum)
    {
        return matches.get(matchNum);
    }

    /**
     * Check if this team has been scouted fully (i.e. all its matches have been opened)
     *
     * @return True if all matches have been loaded in the editor, false otherwise.
     */
    public boolean isComplete()
    {
        for (Map.Entry<Integer, Match> e : matches) {
            if (!e.getValue().isEdited()) return false;
        }

        return true;
    }

    public boolean isEdited()
    {
        for (Map.Entry<Integer, Match> e : matches) {
            if (e.getValue().isEdited()) return true;
        }

        return false;
    }

    @Override
    public int compareTo(FRCTeam another)
    {
        return this.number - another.number;
    }

}
