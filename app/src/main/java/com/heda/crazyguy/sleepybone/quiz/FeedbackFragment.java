package com.heda.crazyguy.sleepybone.quiz;

import android.app.Fragment;
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
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class FeedbackFragment extends Fragment implements View.OnClickListener {

    Spinner sRating;
    EditText etContent;
    String rating, content, user_id;
    Button send;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feedback_fragment, container, false);

        user_id = getActivity().getIntent().getExtras().getString("user_id");

        initialize(rootView);

        return rootView;
    }

    private void initialize(View rootView) {
        sRating = (Spinner) rootView.findViewById(R.id.sRating);
        etContent = (EditText) rootView.findViewById(R.id.etContent);
        send = (Button) rootView.findViewById(R.id.bSendFeedback);

        send.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        rating = sRating.getSelectedItem().toString();
        content = etContent.getText().toString();

        if (!content.equals("")) {
            String result = null;
            try {
                result = new InsertFeedBack().execute(rating, content, user_id).get();
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Fill all the Fields", Toast.LENGTH_SHORT).show();
        }
    }

    private class InsertFeedBack extends AsyncTask<String, Void, String> {

        String url = getResources().getString(R.string.root) + "insert_feedback.php";

        @Override
        protected String doInBackground(String... params) {
            String rating = params[0];
            String content = params[1];
            String user_id = params[2];

            try {

                String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("rating", "UTF-8") + "=" + URLEncoder.encode(rating, "UTF-8") + "&"
                        + URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8");

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
                }

                return sb.toString();

            } catch (Exception e) {
                return "Exception: " + e.getMessage();
            }
        }
    }

}
