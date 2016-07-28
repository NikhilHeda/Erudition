package com.heda.crazyguy.sleepybone.quiz;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UploadFragment extends Fragment implements View.OnClickListener {

    Button upload;
    EditText q_statement, oA, oB, oC, oD;
    Spinner topic, weightage, correct;

    String q, opA, opB, opC, opD, t, w, c, user_id;

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

        Toast.makeText(getActivity(), q + ", " + opA + ", " + opB + ", " + opC + ", " + opD + ", " + t + ", " + w + ", " + c, Toast.LENGTH_SHORT).show();

        if (checkCredentials()) {
            Bundle b = new Bundle();
            b.putString("user_id", user_id);
            Intent i = new Intent("com.heda.crazyguy.sleepybone.quiz.TOPICS");
            i.putExtras(b);
            startActivity(i);
        }
    }

    private boolean checkCredentials() {
        boolean flag = true;
        if( !( q.equals("") || opA.equals("") || opB.equals("") || opC.equals("") || opD.equals("")) ) {
            String result = null;
            try {
                result = new InsertQuestion().execute(q, opA, opB, opC, opD, t, w, c).get();
                user_id = result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Invalid Entries", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    private class InsertQuestion extends AsyncTask<String, Void, String> {

        String url = getResources().getString(R.string.root) + "upload_questions.php";

        @Override
        protected String doInBackground(String... params) {
            String question = params[0];
            String optA = params[1];
            String optB = params[2];
            String optC = params[3];
            String optD = params[4];
            String topic = params[5];
            String weightage = params[6];
            String correct = params[7];

            try {

                String data = URLEncoder.encode("question", "UTF-8") + "=" + URLEncoder.encode(question, "UTF-8") + "&"
                        + URLEncoder.encode("optA", "UTF-8") + "=" + URLEncoder.encode(optA, "UTF-8") + "&"
                        + URLEncoder.encode("optB", "UTF-8") + "=" + URLEncoder.encode(optB, "UTF-8") + "&"
                        + URLEncoder.encode("optC", "UTF-8") + "=" + URLEncoder.encode(optC, "UTF-8") + "&"
                        + URLEncoder.encode("optD", "UTF-8") + "=" + URLEncoder.encode(optD, "UTF-8") + "&"
                        + URLEncoder.encode("topic", "UTF-8") + "=" + URLEncoder.encode(topic, "UTF-8") + "&"
                        + URLEncoder.encode("weightage", "UTF-8") + "=" + URLEncoder.encode(weightage, "UTF-8") + "&"
                        + URLEncoder.encode("correct", "UTF-8") + "=" + URLEncoder.encode(correct, "UTF-8");

                URL u = new URL(url);
                URLConnection conn = u.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
                ow.write(data);
                ow.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while( (line = br.readLine()) != null ) {
                    sb.append(line);
                }

                return sb.toString();

            }catch (Exception e) {
                return "Exception: " + e.getMessage();
            }
        }
    }

}
