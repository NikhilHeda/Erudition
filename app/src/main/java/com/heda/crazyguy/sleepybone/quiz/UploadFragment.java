package com.heda.crazyguy.sleepybone.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UploadFragment extends Fragment implements View.OnClickListener {

    Button upload;
    EditText q_statement, oA, oB, oC, oD;
    Spinner topic, weightage, correct;

    String q, opA, opB, opC, opD, t, w, c;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.upload_xml, container, false);

        initialize(rootView);

        return rootView;
    }

    private void initialize(View rootView) {
        q_statement = (EditText) rootView.findViewById(R.id.etQuestion);
        oA = (EditText) rootView.findViewById(R.id.etOptionA);
        oB = (EditText) rootView.findViewById(R.id.etOptionB);
        oC = (EditText) rootView.findViewById(R.id.etOptionC);
        oD = (EditText) rootView.findViewById(R.id.etOptionD);

        topic = (Spinner) rootView.findViewById(R.id.sTopic);
        weightage = (Spinner) rootView.findViewById(R.id.sWeightage);
        correct = (Spinner) rootView.findViewById(R.id.sAnswer);

        upload = (Button) rootView.findViewById(R.id.bUpload);

        upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        q = q_statement.getText().toString();
        opA = oA.getText().toString();
        opB = oB.getText().toString();
        opC = oC.getText().toString();
        opD = oD.getText().toString();

        t = topic.getSelectedItem().toString();
        w = weightage.getSelectedItem().toString();
        c = correct.getSelectedItem().toString();

        Toast.makeText(getActivity(), q + ", " + opA + ", " + opB + ", " + opC + ", " + opD + ", " + t + ", " + w + ", " + c, Toast.LENGTH_LONG).show();

    }
}
