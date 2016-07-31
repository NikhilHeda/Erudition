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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    EditText etOldPassword, etNewPassword, etRNewPassword;
    String oldPassword, newPassword, rNewPassword, user_id;
    Button change;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.change_password_fragment, container, false);

        user_id = getActivity().getIntent().getExtras().getString("user_id");

        initialize(rootView);

        return rootView;
    }

    private void initialize(View rootView) {
        etOldPassword = (EditText) rootView.findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) rootView.findViewById(R.id.etNewPassword);
        etRNewPassword = (EditText) rootView.findViewById(R.id.etRNewPassword);
        change = (Button) rootView.findViewById(R.id.bChangePassword);

        change.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        oldPassword = etOldPassword.getText().toString();
        newPassword = etNewPassword.getText().toString();
        rNewPassword = etRNewPassword.getText().toString();

        if (!(oldPassword.equals("") || newPassword.equals("") || rNewPassword.equals(""))) {
            String result = null;
            try {
                result = new ChangePassword().execute(oldPassword, newPassword, rNewPassword, user_id).get();
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Fill all the Fields", Toast.LENGTH_SHORT).show();
        }

    }

    private class ChangePassword extends AsyncTask<String, Void, String> {

        String url = getResources().getString(R.string.root) + "chng_pwd.php";

        @Override
        protected String doInBackground(String... params) {
            String oldPass = params[0];
            String newPass = params[1];
            String rNewPass = params[2];
            String user_id = params[3];

            try {

                String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("old_password", "UTF-8") + "=" + URLEncoder.encode(oldPass, "UTF-8") + "&"
                        + URLEncoder.encode("new_password", "UTF-8") + "=" + URLEncoder.encode(newPass, "UTF-8") + "&"
                        + URLEncoder.encode("re_new_password", "UTF-8") + "=" + URLEncoder.encode(rNewPass, "UTF-8");

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
