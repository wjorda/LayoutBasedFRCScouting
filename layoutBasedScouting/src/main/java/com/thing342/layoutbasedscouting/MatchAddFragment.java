package com.thing342.layoutbasedscouting;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

public class MatchAddFragment extends DialogFragment
{

    private int spinnerNum = 1;

    private Spinner spinners[] = new Spinner[spinnerNum];
    private OnFinishListener listener;

    public MatchAddFragment()
    {

    }

    public static MatchAddFragment newInstance(OnFinishListener onFinishListener)
    {

        MatchAddFragment maf = new MatchAddFragment();
        maf.listener = onFinishListener;
        return maf;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        getDialog().setTitle("Add New Match");
        //v = inflater.inflate(R.layout.fragment_match_add, container, false);
        //LinearLayout lv = (LinearLayout) v.findViewById(R.id.matchAddLinearLayout);

        LinearLayout lv = new LinearLayout(getActivity());

        for (int i = 0; i < spinnerNum; i++) {
            spinners[i] = new Spinner(getActivity());
            lv.addView(spinners[i]);
        }

        //ArrayAdapter<FRCTeam> arrayAdapter = new ArrayAdapter<FRCTeam>(getActivity(), android.R.layout.simple_selectable_list_item, app.teamsList);
        ArrayAdapter<FRCTeam> arrayAdapter = new ArrayAdapter<FRCTeam>(getActivity(), android.R.layout.simple_selectable_list_item, ((AerialAssaultApplication) getActivity().getApplication()).teamsList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Spinner s : spinners) s.setAdapter(arrayAdapter);

        Button button = new Button(getActivity());
        button.setText("Confirm");
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                confirmMatch();
            }
        });

        lv.addView(button);

        ScrollView v = new ScrollView(getActivity());
        v.addView(lv);

        return v;
    }

    private void confirmMatch()
    {
        /*Log.d("AerialAssault", "matches: " + (((AerialAssaultApplication) getActivity().getApplication()).matches));
        ((AerialAssaultApplication) getActivity().getApplication()).matches+=6;
		for(Spinner s : spinners) {
			((FRCTeam) s.getSelectedItem()).createMatch(((AerialAssaultApplication) getActivity().getApplication()).matches/6);
		}
		Log.d("AerialAssault", "matches: " + (((AerialAssaultApplication) getActivity().getApplication()).matches));
		((AerialAssaultApplication) getActivity().getApplication()).saveAll();
		dismiss();*/

        int teams[] = new int[spinners.length];
        for (int i = 0; i < spinners.length; i++)
            teams[i] = ((FRCTeam) spinners[i].getSelectedItem()).number;
        ((AerialAssaultApplication) getActivity().getApplication()).addExtraMatch(teams);
        listener.onFinish();

        dismiss();
    }

    public static interface OnFinishListener
    {

        public void onFinish();

    }
}
