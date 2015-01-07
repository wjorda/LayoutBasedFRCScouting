package com.thing342.layoutbasedscouting;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

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
    public ArrayList<Match> matches = new ArrayList<Match>();

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
        matches = in.readArrayList(Match.class.getClassLoader());
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
        matches.add(new Match(num, this));
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
        out.writeList(matches);
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
        for (Match m : matches) {
            if (m.matchNum == matchNum) return m;
        }

        return null;
    }

    /**
     * Get the array index a match is stored in.
     *
     * @param matchNum The match number to search for.
     * @return
     */
    public int getMatchPos(int matchNum)
    {
        for (int i = 0; i < matches.size(); i++) if (matches.get(i).matchNum == matchNum) return i;
        return 0;
    }

    /**
     * Check if this team has been scouted fully (i.e. all its matches have been opened)
     *
     * @return True if all matches have been loaded in the editor, false otherwise.
     */
    public boolean isComplete()
    {
        for (Match m : matches) {
            if (!m.isEdited()) return false;
        }

        return true;
    }

    public boolean isEdited()
    {
        for (Match m : matches) {
            if (m.isEdited()) return true;
        }

        return false;
    }

    @Override
    public int compareTo(FRCTeam another)
    {
        return this.number - another.number;
    }

}
