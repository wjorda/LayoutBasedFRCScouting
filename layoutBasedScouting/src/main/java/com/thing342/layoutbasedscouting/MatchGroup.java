package com.thing342.layoutbasedscouting;

import java.util.ArrayList;

public class MatchGroup {
	public final int num;
	public int teams[];
	private FRCTeam rTeams[];
	
	public MatchGroup(int num, FRCTeam teams[]) {
		this.teams = new int[teams.length];
		this.rTeams = teams;
		
		for(int i = 0; i < teams.length; i++) {
			this.teams[i] = teams[i].number;
		}
		
		this.num = num;
	}
	
	@Override
	public String toString() {
		return "Match Q" + Integer.toString(num);
	}
	
	public boolean isEdited() {
		for(FRCTeam t : rTeams) {
			if(t.getMatch(num).isEdited()) return true;
		}
		
		return false;
	}
	
	public boolean isComplete() {
		for(FRCTeam t : rTeams) {
			if(!t.getMatch(num).isEdited()) return false;
		}
		
		return true;
	}
}