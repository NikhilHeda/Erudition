package com.heda.crazyguy.sleepybone.quiz;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

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

        /*
            Getting the type of topics selected.
            2 All
            3 Attempted
            4 Popular

            To pass in php (flag value)
            1 All
            2 Attempted
            3 Popular
        */

        try {
            String rootUrl = getResources().getString(R.string.root);
            String flag = Integer.toString(getArguments().getInt("position") - 1);
            topic_headings = new JSONArray(new GetTopics().execute(rootUrl, user_id, flag).get());
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

}
