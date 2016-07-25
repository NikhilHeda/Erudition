package com.heda.crazyguy.sleepybone.quiz;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ShowResults extends Activity {

    private JSONArray results;
    private String user_id;
    private int topic_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_results_xml);

        Bundle b = getIntent().getExtras();
        Bundle user_answers = b.getBundle("user_answers");
        user_id = b.getString("user_id");
        topic_id = b.getInt("topic_id");

        try {
            results = new JSONArray(new GetResults().execute(user_answers).get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        display(results);

    }

    private void display(JSONArray results) {

        TableLayout table = (TableLayout) findViewById(R.id.tlTable);
        TableRow tableRow;
        TextView text;

        JSONObject row;

        String[] fields = {"question_id", "q_statement", "user_answer", "answer", "points"};
        String[] options = {"option1", "option2", "option3", "option4"};

        String value;

        for (int i = 0; i < results.length(); i++) {

            tableRow = new TableRow(this);

            try {
                row = results.getJSONObject(i);
                for (int j = 0; j < 5; j++) {
                    value = row.getString(fields[j]);
                    text = new TextView(this);
                    text.setPadding(10, 10, 10, 10);

                    if (j == 1)
                        text.setGravity(Gravity.START);
                    else
                        text.setGravity(Gravity.CENTER);

                    if (j == 2 || j == 3) {
                        value = row.getString(options[Integer.parseInt(value) - 1]);
                    }

                    if ((!row.getString("correctness").equals("1")) && j == 4)
                        value = "0";

                    text.setText(value);
                    tableRow.addView(text);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            table.addView(tableRow);
        }

    }

/*
    private String display(Bundle user_answers) {
        String res = "", value;
        for (String key : user_answers.keySet()) {
            value = user_answers.getString(key);
            res += key + ":" + value + "\n";
        }
        return res;
    }
*/

    private class GetResults extends AsyncTask<Bundle, Void, String> {

        String url = getResources().getString(R.string.root) + "check_questions.php?user_id=" + user_id + "&topic_id=" + topic_id;

        @Override
        protected String doInBackground(Bundle... params) {

            try {

                String data = "";
                Bundle user_answers = params[0];

                for (String key : user_answers.keySet()) {
                    data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(user_answers.getString(key), "UTF-8") + "&";
                }


                data = data.substring(0, (data.length() - 1));
                Log.i("In Async Task", data);

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
