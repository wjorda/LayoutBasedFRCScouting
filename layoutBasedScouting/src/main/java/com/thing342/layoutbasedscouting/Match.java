package com.thing342.layoutbasedscouting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thing342.layoutbasedscouting.fields.Field;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Match {
	
	public int matchNum;
	public FRCTeam team;
	
	/*
	public int autoTopGoal=0, autoBottomGoal=0, autoShotsMissed=0, autoHotGoal=0;
	public boolean autoMobility=false;
	public int teleTopGoal=0, teleBottomGoal=0, teleShotsMissed=0, teleAssists=0, teleTrussShots=0, teleTrussCatches=0;
	public Rating offensiveRating = Rating.NA;
	public Rating defensiveRating = Rating.NA;
	
	public String notes = "";
	public Rating driveTeamRating = Rating.NA;*/
	
	public ArrayList<Object> data; 
	
	public Match(int num, FRCTeam team) {
		this.matchNum = num;
		this.team = team;
		data = new ArrayList<Object>(); 
	}
	
	public void setData(Match m) {
		this.data = m.data;
		
	}
    
    public static boolean isEdited(String[] record) {
    	for(int i = 2; i < record.length; i++) {
    		if (!record[i].isEmpty()) return true;
    	}
    	
    	return false;
    }
    
    public boolean isEdited() {
    	return (data.size() > 0);
    			
    }
    
	public Match (FRCTeam team, String[] record) {
		this.team = team;
		data = new ArrayList<Object>();
		matchNum = Integer.parseInt(record[0]);
		
		for(int i = 1; i < record.length; i++) {
			String datum = record[i];
			
			if (datum == null) continue;
			
			Object newEntry = Rating.parse(datum);
			if (newEntry != null) {
				data.add(newEntry);
				continue;
			}
			
			if (Parser.integer(datum)) {
				data.add(Integer.parseInt(datum));
				continue;
			}
			
			if (Parser.bool(datum)) {
				data.add(Boolean.parseBoolean(datum));
				continue;
			}
			
			data.add(datum);
			
			
		}
	}
    
    public String[] getRecord(boolean export) {
    	
    	ArrayList<String> list = new ArrayList<String>();
    	
    	list.add(0, Integer.toString(matchNum));
    	if(export) list.add(1, Integer.toString(team.number));
    	
    	for (Object o : data) {
    		if(o instanceof Boolean) list.add(Integer.toString(((Boolean) o ? 1:0))); //You can thank Joseph for this. Apparently 'true' and 'false' are too hard for Tableau.  
    		else list.add(o.toString());
    	}
    	
    	String record[] = new String[list.size()];
    	return list.toArray(record);
    }
	
    public String toString() {
    	return "Q" + Integer.toString(matchNum);
    }
    
    private static final class Parser {
    	public static boolean integer(String s) {
        	boolean parsable = true;
        	try{
        	Integer.parseInt(s);
        	}catch(NumberFormatException e){
        	parsable = false;
        	}
        	
        	return parsable;
        }
    	public static boolean bool(String s) {
        	return (s.contains("alse") || s.contains("rue"));
        }
    }
    
}
