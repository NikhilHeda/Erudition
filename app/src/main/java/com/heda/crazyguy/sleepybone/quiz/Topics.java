package com.heda.crazyguy.sleepybone.quiz;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Topics extends ListActivity {

    private JSONArray topic_headings;

    private Bundle b1 = new Bundle();
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b2 = getIntent().getExtras();
        user_id = b2.getString("user_id");

        try {
            topic_headings = new JSONArray(new GetTopics().execute(1).get()); // Passing the flag as an integer 1 - all topics
        } catch (Exception e) {
            e.printStackTrace();
        }

        setListAdapter(new ArrayAdapter<String>(Topics.this, android.R.layout.simple_list_item_1, getArray(topic_headings)));

    }

    private String[] getArray(JSONArray topic_headings) {
        String[] topics = new String[topic_headings.length()];
        try {
            for (int i = 0; i < topic_headings.length(); i++)
                topics[i] = topic_headings.getJSONObject(i).getString("topic_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topics;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try {
            String topic_id = topic_headings.getJSONObject(position).getString("topic_id");
            b1.putString("topic_id", topic_id);
            b1.putString("user_id", user_id);
            Intent i = new Intent("com.heda.crazyguy.sleepybone.quiz.QUESTIONS");
            i.putExtras(b1);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class GetTopics extends AsyncTask<Integer, Void, String> {

        String url = getResources().getString(R.string.root) + "retrieve_topics.php";

        @Override
        protected String doInBackground(Integer... params) {

            try {

                String data = URLEncoder.encode("flag", "UTF-8") + "=" + URLEncoder.encode(params[0].toString(), "UTF-8");

                URL u = new URL(url);
                URLConnection conn = u.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
                ow.write(data);
                ow.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }

                return sb.toString();

            } catch (Exception e) {
                return "Exception: " + e.getMessage();
            }
        }
    }

}
