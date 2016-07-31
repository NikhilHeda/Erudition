package com.heda.crazyguy.sleepybone.quiz;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class GetTopics extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        String url = params[0] + "retrieve_topics.php";

        try {

            String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&"
                    + URLEncoder.encode("flag", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");

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