package com.thing342.layoutbasedscouting;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchAdapter extends BaseAdapter {
	
	Context context;
	MatchGroup groups[];
	private static LayoutInflater inflater = null;
	
	public MatchAdapter(Context context, MatchGroup groups[]) {
		this.context = context;
		this.groups = groups;
		inflater = ((Activity)context).getLayoutInflater();
	}
	
	public MatchAdapter(Context context, ArrayList<MatchGroup> groupList) {
		this.context = context;
		this.groups = groupList.toArray(new MatchGroup[groupList.size()]);
		inflater = ((Activity)context).getLayoutInflater();
	}

	@Override
	public int getCount() {
		return groups.length;
	}

	@Override
	public Object getItem(int position) {
		return groups[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi;
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.adapter_row, null);
        	Log.d("AerialAssault", "convertView is null");
        }
        else vi = convertView;
        
        MatchGroup thisMatch = groups[position];
        
        TextView header = (TextView) vi.findViewById(R.id.rowHeader);
        header.setText(thisMatch.toString());
        
        TextView footer = (TextView) vi.findViewById(R.id.rowDesc);
        String text = Arrays.toString(thisMatch.teams);
        footer.setText(text.substring(1, text.length()-1));
        
        ImageView dataCheck = (ImageView) vi.findViewById(R.id.rowDataCheck);
        dataCheck.setImageResource(R.drawable.data_bad); 
        if (thisMatch.isEdited()) dataCheck.setImageResource(R.drawable.data_partial); 
        if (thisMatch.isComplete()) dataCheck.setImageResource(R.drawable.data_ok);
        
        return vi;
	}

}
