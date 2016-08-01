package com.heda.crazyguy.sleepybone.quiz;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    String user_id;
    JSONArray results;

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
        try {
            results = new JSONArray(new GetTotalScore().execute(user_id).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        display(results);
    }

    @Override
    public void onClick(View view) {

    }

    private void display(JSONArray results) {
        TableRow tablerow;
        TextView text, sl_no;
        JSONObject row;
        String value;

        String[] fields = {"topic_name", "score"};

        for(int i = 0; i < results.length(); i++) {
            tablerow = new TableRow(getActivity());

            try {
//              For Serial Number:
                sl_no = new TextView(getActivity());
                sl_no.setPadding(10, 10, 10, 10);
                sl_no.setText(String.valueOf(i + 1));
                tablerow.addView(sl_no);

                row = results.getJSONObject(i);
                for(int j = 0; j < 2; j++) {
                    value = row.getString(fields[j]);
                    text = new TextView(getActivity());
                    text.setPadding(10, 10, 10, 10);
                    text.setText(value);
                    tablerow.addView(text);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            table.addView(tablerow);
        }
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
                return "Exception: " + e.getMessage();
            }
        }
    }
}
