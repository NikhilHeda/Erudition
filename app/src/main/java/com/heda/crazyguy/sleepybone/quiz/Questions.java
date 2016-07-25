package com.heda.crazyguy.sleepybone.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Questions extends Activity implements View.OnClickListener {

    private TextView question;
    private Button o1, o2, o3, o4;

    private JSONArray questions;

    private int current_question = 0;
    private int topic_id;
    private String user_id;

    private String question_id;

    private Bundle user_answers = new Bundle();
    private Bundle b = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_xml);

        Bundle b = getIntent().getExtras();
        topic_id = Integer.parseInt(b.getString("topic_id"));
        user_id = b.getString("user_id");

        initialize();

        // Getting all the questions...
        try {
            questions = new JSONArray(new GetQuestions().execute(topic_id).get()); // Passing the topic id...
        } catch (Exception e) {
            e.printStackTrace();
        }

        // setting the first question..
        setQuestion(questions, current_question++);

    }

    private void initialize() {
        question = (TextView) findViewById(R.id.tvQuestion);
        o1 = (Button) findViewById(R.id.bO1);
        o2 = (Button) findViewById(R.id.bO2);
        o3 = (Button) findViewById(R.id.bO3);
        o4 = (Button) findViewById(R.id.bO4);

        o1.setOnClickListener(this);
        o2.setOnClickListener(this);
        o3.setOnClickListener(this);
        o4.setOnClickListener(this);

    }

    private void setQuestion(JSONArray questions, int q_number) {
        int length = questions.length();
        if (q_number < length) {
            try {
                JSONObject q = questions.getJSONObject(q_number);
                question_id = q.getString("question_id");
                question.setText(question_id + ". " + q.getString("q_statement"));
                o1.setText(q.getString("option1"));
                o2.setText(q.getString("option2"));
                o3.setText(q.getString("option3"));
                o4.setText(q.getString("option4"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No more questions", Toast.LENGTH_SHORT).show();
            Intent i = new Intent("com.heda.crazyguy.sleepybone.quiz.SHOWRESULTS");
            b.putString("user_id", user_id);
            b.putInt("topic_id", topic_id);
            b.putBundle("user_answers", user_answers);
            i.putExtras(b);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {

        String answer = "";
        switch (v.getId()) {
            case R.id.bO1:
                answer = "1";
                break;
            case R.id.bO2:
                answer = "2";
                break;
            case R.id.bO3:
                answer = "3";
                break;
            case R.id.bO4:
                answer = "4";
                break;
        }

        user_answers.putString(question_id, answer);

        setQuestion(questions, current_question++);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    private class GetQuestions extends AsyncTask<Integer, Void, String> {

        String url = getResources().getString(R.string.root) + "retrieve_questions.php";

        @Override
        protected String doInBackground(Integer... params) {
            try {

                String data = URLEncoder.encode("topic_id", "UTF-8") + "=" + URLEncoder.encode(params[0].toString(), "UTF-8");

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
