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
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SignupFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private EditText etUsername, etPassword, etRPassword, etEmail, etPhone;
    private String username, password, r_password, email, gender, phone;
    private String user_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.signup_xml, container, false);

        initialize(rootView);

        return rootView;
    }

    private void initialize(View rootView) {
        Button signup = (Button) rootView.findViewById(R.id.bSignup);
        etUsername = (EditText) rootView.findViewById(R.id.etUsername);
        etPassword = (EditText) rootView.findViewById(R.id.etSignupPassword);
        etRPassword = (EditText) rootView.findViewById(R.id.etRPassword);
        etEmail = (EditText) rootView.findViewById(R.id.etSignupEmail);
        etPhone = (EditText) rootView.findViewById(R.id.etPhone);
        RadioGroup rgGender = (RadioGroup) rootView.findViewById(R.id.rgGender);

        signup.setOnClickListener(this);

        rgGender.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        r_password = etRPassword.getText().toString();
        email = etEmail.getText().toString();
        phone = etPhone.getText().toString();

        if (checkCredentials()) {
            Bundle b = new Bundle();
            b.putString("user_id", user_id);
            Intent i = new Intent(getActivity(), HomePageNav.class);
            i.putExtras(b);
            getActivity().startActivity(i);
        }

    }

    private boolean checkCredentials() {
        Boolean flag = false;
        if (!(username.equals("") || password.equals("") || r_password.equals("") || email.equals("") || gender.equals("") || phone.equals(""))) {

            String result = null;
            try {
                result = new InsertUser().execute(username, password, r_password, email, gender, phone).get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (result != null) {
                if (result.equalsIgnoreCase("Password Error")) {
                    Toast.makeText(getActivity(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                    etRPassword.setText("");
                } else if (result.equalsIgnoreCase("Email Error")) {
                    Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    etEmail.setText("");
                } else {
                    user_id = result;
                    flag = true;
                }
            }

        } else {
            Toast.makeText(getActivity(), "Invalid Entries", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbMale:
                gender = "M";
                break;
            case R.id.rbFemale:
                gender = "F";
                break;
        }
    }

    private class InsertUser extends AsyncTask<String, Void, String> {
        String url = getResources().getString(R.string.root) + "signup.php";

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String r_password = params[2];
            String email = params[3];
            String gender = params[4];
            String phone = params[5];

            try {

                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        + URLEncoder.encode("r_password", "UTF-8") + "=" + URLEncoder.encode(r_password, "UTF-8") + "&"
                        + URLEncoder.encode("email_id", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("ph_number", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&"
                        + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");

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