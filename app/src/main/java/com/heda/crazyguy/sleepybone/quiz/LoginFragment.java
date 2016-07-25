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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private String user_id = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_xml, container, false);

        initialize(rootView);

        return rootView;
    }

    private void initialize(View rootView) {
        etEmail = (EditText) rootView.findViewById(R.id.etEmailId);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        Button login = (Button) rootView.findViewById(R.id.bLogin);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        Intent i;
        if (check_user(email, password)) {
            Bundle b = new Bundle();
            b.putString("user_id", user_id);
            i = new Intent("com.heda.crazyguy.sleepybone.quiz.TOPICS");
            i.putExtras(b);
            startActivity(i);
        } else {
            Toast.makeText(getActivity(), "Invalid Entries", Toast.LENGTH_SHORT).show();
            etPassword.setText("");
            etEmail.setText("");
        }
    }

    private boolean check_user(String email, String password) {
        Boolean flag = false;
        if (!(email.equals("") || password.equals(""))) {
            try {
                String result = new CheckUser().execute(email, password).get();
                if (!(result.equalsIgnoreCase("false"))) {
                    user_id = result;
                    flag = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    private class CheckUser extends AsyncTask<String, Void, String> {
        String url = getResources().getString(R.string.root) + "login.php";

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            try {

                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
