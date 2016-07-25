package com.heda.crazyguy.sleepybone.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    Button s;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_xml, container, false);

        initialize(rootView);

        return rootView;
    }

    private void initialize(View rootView) {
        s = (Button) rootView.findViewById(R.id.bSettings);

        s.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getActivity(), "Heloooo", Toast.LENGTH_LONG).show();
    }

}
