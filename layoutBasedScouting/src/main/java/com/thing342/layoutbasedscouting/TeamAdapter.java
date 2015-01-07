package com.thing342.layoutbasedscouting;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamAdapter extends BaseAdapter {
	
	Context context;
	FRCTeam teams[];
	private static LayoutInflater inflater = null;
	
	public TeamAdapter(Context context, FRCTeam teams[]) {
		this.context = context;
		this.teams = teams;
		inflater = ((Activity)context).getLayoutInflater();
	}
	
	public TeamAdapter(Context context, ArrayList<FRCTeam> teamList) {
		this.context = context;
		this.teams = teamList.toArray(new FRCTeam[teamList.size()]);
		inflater = ((Activity)context).getLayoutInflater();
	}

	@Override
	public int getCount() {
		return teams.length;
	}

	@Override
	public Object getItem(int position) {
		return teams[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
        if (vi == null) vi = inflater.inflate(R.layout.adapter_row, null);
        FRCTeam thisTeam = teams[position];
        
        TextView header = (TextView) vi.findViewById(R.id.rowHeader);
        header.setText(thisTeam.toString());
        
        TextView footer = (TextView) vi.findViewById(R.id.rowDesc);
        String text = thisTeam.matches.toString();
        footer.setText(text.substring(1, text.length()-1));
        
        ImageView dataCheck = (ImageView) vi.findViewById(R.id.rowDataCheck);
        dataCheck.setImageResource(R.drawable.data_bad); 
        if (thisTeam.isEdited()) dataCheck.setImageResource(R.drawable.data_partial); 
        if (thisTeam.isComplete()) dataCheck.setImageResource(R.drawable.data_ok);
        
        return vi;
	}

}
