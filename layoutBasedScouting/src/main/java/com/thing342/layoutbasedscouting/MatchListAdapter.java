package com.thing342.layoutbasedscouting;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MatchListAdapter extends BaseAdapter
{
    private final MatchGroup groups[];

    public MatchListAdapter(MatchGroup groups[])
    {
        this.groups = groups;
    }

    public MatchListAdapter(ArrayList<MatchGroup> groupList)
    {
        this.groups = groupList.toArray(new MatchGroup[groupList.size()]);
    }

    @Override
    public int getCount()
    {
        return groups.length;
    }

    @Override
    public Object getItem(int position)
    {
        return groups[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi;
        if (convertView == null) {
            vi = View.inflate(parent.getContext(), R.layout.adapter_row, null);
            Log.d("AerialAssault", "convertView is null");
        } else vi = convertView;

        MatchGroup thisMatch = groups[position];

        TextView header = (TextView) vi.findViewById(R.id.rowHeader);
        header.setText(thisMatch.toString());

        TextView footer = (TextView) vi.findViewById(R.id.rowSubtitle);
        footer.setText("Team " + thisMatch.teams);

        //CheckBox checkBox = (CheckBox) vi.findViewById(R.id.is_complete_box);
        //checkBox.setChecked(thisMatch.isEdited());

        return vi;
    }

}
