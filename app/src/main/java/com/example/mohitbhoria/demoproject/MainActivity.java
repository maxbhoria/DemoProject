package com.example.mohitbhoria.demoproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private String TAG = "hello";
    String stringEmail;
    String stringPassword;
    public static final String MyPREFERENCES2 = "MyPrefs";
    ProgressDialog pDialog;
    EditText email,password;
    Button login,signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ConnectivityCheck.getInstance(getApplicationContext()).isOnline()) {
            /**
             * Internet is available, Toast It!
             */
            Toast.makeText(getApplicationContext(), "WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
        } else {
            /**
             * Internet is NOT available, Toast It!
             */
            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
        }
       SharedPreferences sharedpreferences2=getSharedPreferences(MyPREFERENCES2, Context.MODE_PRIVATE);
      //  String defa="Default";
        String getEmail=sharedpreferences2.getString("email","");
        if(sharedpreferences2.getString("email",getEmail).length() ==0 )
        {

        }
        else
        {
            Intent intent=new Intent(MainActivity.this,NavigationDrawer.class);
            startActivity(intent);
            finish();
        }
        email= (EditText) findViewById(R.id.editTextEmail);
        password= (EditText) findViewById(R.id.editTextPassword);
        login= (Button) findViewById(R.id.buttonLogin);
        signUp= (Button) findViewById(R.id.buttonSignUp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityCheck.getInstance(getApplicationContext()).isOnline()) {
                    /**
                     * Internet is available, Toast It!
                     */
                    Intent intent=new Intent(MainActivity.this,SignUp.class);
                    startActivity(intent);
                } else {
                    /**
                     * Internet is NOT available, Toast It!
                     */
                    Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringEmail = email.getText().toString();
                stringPassword = password.getText().toString().trim();
                if (checkValidations(MainActivity.this, stringEmail, stringPassword)) {

                    HashMap<String, String> params = new HashMap<>();
                    params.put("email",stringEmail);
                    params.put("password",stringPassword);
                    postRequest(params);
                }


            }
        });

    }
    private void postRequest(final HashMap<String, String> postParams) {

        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(true);
                pDialog.show();

            }
            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                String responseString = "";
                try {
                    URL url = new URL("http://52.27.53.102/testapi/user/login");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setDoOutput(true);
                    con.setReadTimeout(30000);
                    con.setConnectTimeout(30000);
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    OutputStream os = con.getOutputStream();
                    os.write(getPostDataString(postParams).getBytes());
                    os.flush();
                    os.close();

                    int statusCode = con.getResponseCode();

                    if (statusCode == 200) {
                        is = con.getInputStream();
                        responseString = convertInputStreamToString(is);
                        return responseString;
                    } else {
                        return null;
                    }
                } catch (SocketTimeoutException e) {
                    return null;
                } catch (Exception ex) {
                    return null;
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        return null;
                    }
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    String response = result;

                    //Parse json from response here
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String error_string = jsonObject.getString("error_string");
                        int error_code = jsonObject.getInt("error_code");
                        String results = jsonObject.getString("result");
                        Log.i(TAG, "json " + "\t" + error_string + "\t" + error_code + "\t" + results);
                        JSONObject resultSet = jsonObject.getJSONObject("data");
                        String id=resultSet.getString("id");
                        String name=resultSet.getString("name");
                        String email=resultSet.getString("email");
                        String mobile=resultSet.getString("mobile");
                        String password=resultSet.getString("password");
                        String createdOn=resultSet.getString("createdOn");
                        String updatedOn=resultSet.getString("updatedOn");
                        //Log.e(TAG, "json paring :" + name + email+ mobile + password + createdOn + updatedOn +id);
                        if(error_string.equals("success.")) {
                            Toast.makeText(MainActivity.this,"SuccessFully Login",Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                            Intent intent=new Intent(MainActivity.this,NavigationDrawer.class);
                            SharedPreferences sharedPreferences2 = getSharedPreferences(MyPREFERENCES2, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                            editor2.putString("user", name);
                            editor2.putString("email", email);
                            editor2.putString("mobile", mobile);
                            editor2.putString("id", id);
                            editor2.commit();
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Login Failed ", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }

                    }

                    catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }
                }


                else {
                    Log.e(TAG, "Couldn't get json from server.");

                }

            }


        }.execute();


    }

    public String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder result = null;
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            result = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                result.append(line).append('\n');
            }
            return result.toString();
        } catch (Exception ex) {
            return "";
        }

    }
    private boolean checkValidations(MainActivity mainActivity, String email, String password){

       if(email==null||email.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Email Should be in Correct Form",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (password == null || password.length() < 8) {
            Toast.makeText(getApplicationContext(), "Please enter 6 digit password", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}