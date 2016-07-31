package com.heda.crazyguy.sleepybone.quiz;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class TopicsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private JSONArray topic_headings;

    private Bundle b1 = new Bundle();
    private String user_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.topics_fragment, container, false);

        ListView allTopics = (ListView) rootView.findViewById(R.id.lvAllTopics);

        Bundle b2 = getActivity().getIntent().getExtras();
        user_id = b2.getString("user_id");

        try {
            topic_headings = new JSONArray(new GetTopics().execute(1).get()); // Passing the flag as an integer 1 - all topics
        } catch (Exception e) {
            e.printStackTrace();
        }

        allTopics.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getArray(topic_headings)));
        allTopics.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            String topic_id = topic_headings.getJSONObject(i).getString("topic_id");
            b1.putString("topic_id", topic_id);
            b1.putString("user_id", user_id);
            Intent intent = new Intent("com.heda.crazyguy.sleepybone.quiz.QUESTIONS");
            intent.putExtras(b1);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
