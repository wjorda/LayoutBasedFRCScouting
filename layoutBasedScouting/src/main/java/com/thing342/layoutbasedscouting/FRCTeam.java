package com.thing342.layoutbasedscouting;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.RatingBar;

public class FRCTeam implements Parcelable, Comparable<FRCTeam>{
	
	public int number;
	//public String name;
	public ArrayList<Match> matches = new ArrayList<Match>();
	
	/*public Rating offensiveRating;
	public Rating defensiveRating;*/
	
	
	
	/*public FRCTeam(int number, Rating offensiveRating, Rating defensiveRating, String name) {
		this.number = number;
		this.offensiveRating = offensiveRating;
		this.defensiveRating = defensiveRating;
		this.name = name;
	}*/
	
	public FRCTeam(int number, String name) {
		this.number = number;
		//this.name = name;
	}
	
	public FRCTeam() {
		this.number = 2363;
		//this.name = "Triple Helix";
	}
	
	public String toString() {
		return "Team " + Integer.toString(number); //+ " - " + name;
	}
	
	public void createMatch(int num) {
		matches.add(new Match(num, this));
	}
	
	
	private FRCTeam(Parcel in) {
        // This order must match the order in writeToParcel()
        number = in.readInt();
        //name = in.readString();
        matches = in.readArrayList(Match.class.getClassLoader());
        /*offensiveRating = (Rating) in.readSerializable();
        defensiveRating = (Rating) in.readSerializable();
        autoTopGoal = in.readInt();
        autoBottomGoal = in.readInt();
        autoShotsMissed = in.readInt();
        autoHotGoal = in.readInt();
        autoMobility = in.readInt() == 1;
        teleTopGoal = in.readInt();
        teleBottomGoal = in.readInt();
        teleShotsMissed = in.readInt();
        teleAssists = in.readInt();
        teleTrussShots = in.readInt();
        teleTrussCatches = in.readInt();*/
        
        // Continue doing this for the rest of your member data
    }

    public void writeToParcel(Parcel out, int flags) {
        // Again this order must match the Question(Parcel) constructor
        out.writeInt(number);
        //.writeString(name);
        out.writeList(matches);
        /*out.writeSerializable(offensiveRating);
        out.writeSerializable(defensiveRating);
        out.writeInt(autoTopGoal);
        out.writeInt(autoBottomGoal);
        out.writeInt(autoShotsMissed);
        out.writeInt(autoHotGoal);
        out.writeInt(autoMobility ? 1 : 0);
        out.writeInt(teleTopGoal);
        out.writeInt(teleBottomGoal);
        out.writeInt(teleShotsMissed);
        out.writeInt(teleAssists);
        out.writeInt(teleTrussShots);
        out.writeInt(teleTrussCatches);*/
        // Again continue doing this for the rest of your member data
    }

    // Just cut and paste this for now
    public int describeContents() {
        return 0;
    }
    
    public Match getMatch(int matchNum) {
    	for (Match m : matches) {
    		if (m.matchNum == matchNum) return m; 
    	}
    	
    	return null;
    }
    
    public int getMatchPos(int matchNum) {
    	for(int i = 0; i < matches.size(); i++) if (matches.get(i).matchNum == matchNum) return i;
    	return 0;
    }

    // Just cut and paste this for now
    public static final Parcelable.Creator<FRCTeam> CREATOR = new Parcelable.Creator<FRCTeam>() {
        public FRCTeam createFromParcel(Parcel in) {
            return new FRCTeam(in);
        }

        public FRCTeam[] newArray(int size) {
            return new FRCTeam[size];
        }
    };
    
    public boolean isComplete() {
    	for (Match m : matches) {
    		if (!m.isEdited()) return false;
    	}
    	
    	return true;
    }
    
    public boolean isEdited() {
    	for (Match m : matches) {
    		if(m.isEdited()) return true;
    	}
    	
    	return false;
    }

	@Override
	public int compareTo(FRCTeam another) {
		return this.number - another.number;
	}

}
