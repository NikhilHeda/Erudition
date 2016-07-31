package com.heda.crazyguy.sleepybone.quiz;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Nisha on 31/07/16.
 */

public class TotalScoreFragment extends Fragment implements View.OnClickListener {

    TableLayout table;
    TextView total_score;
    String user_id;
    JSONArray result;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.total_score_fragment, container, false);

        user_id = getActivity().getIntent().getExtras().getString("user_id");

        intialize(rootview);

        return rootview;
    }

    private void intialize(View rootview) {

        table = (TableLayout) rootview.findViewById(R.id.tlTableTS);
        total_score = (TextView) rootview.findViewById(R.id.tvTotalScore);

    }

    @Override
    public void onClick(View view) {

        try {
            result = new JSONArray(new GetTotalScore().execute(user_id).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), (CharSequence) result, Toast.LENGTH_SHORT).show();
        display(result);
    }

    private void display(JSONArray results) {

    }

    private class GetTotalScore extends AsyncTask<String, Void, String> {

        String url = getResources().getString(R.string.root) + "retrieve_scores.php";

        @Override
        protected String doInBackground(String... params) {
            String user_id = params[0];

            try {

                String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
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

            } catch (Exception e) {
                return "Exception " + e.getMessage();
            }
        }
    }
}
