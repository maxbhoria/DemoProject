package com.example.mohitbhoria.demoproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SignUp extends AppCompatActivity {
    ImageView imageView;
    Double latitude,longitude;
    Bitmap bmp;
    Button signUp;
    String ImageDecode;
    int IMG_RESULT=1;
    Intent intent;
    int CAMERA=1;
    private int STORAGE_PERMISSION_CODE = 23;
    EditText userName,email,phone,password,reTypePassword;
    String stringName;
    String stringEmail;
    String stringMobile;
    String stringPassword;
    String stringreTypePassword;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String TAG ="hello";
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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
        userName= (EditText) findViewById(R.id.editTextUserName);
        email= (EditText) findViewById(R.id.editTextEmail2);
        phone= (EditText) findViewById(R.id.editTextPhone);
        password= (EditText) findViewById(R.id.editTextPassword2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        reTypePassword= (EditText) findViewById(R.id.editTextRetypePassword);
        imageView= (ImageView) findViewById(R.id.imageView);
       /* imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,CAMERA);
            }
        });*/
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReadStorageAllowed()) {
                    //If permission is already having then showing the toast
                    intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra("crop","true");
                    intent.putExtra("aspectX",1);
                    intent.putExtra("aspectY",1);
                    intent.putExtra("outputX",200);
                    intent.putExtra("outputY",200);
                    intent.putExtra("return-data",true);
                    startActivityForResult(intent, IMG_RESULT);
                    //Existing the method with return
                    return;
                }

                //If the app has not the permission then asking for the permission
                requestStoragePermission();
            }
        });
        signUp= (Button) findViewById(R.id.buttonSignUp2);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringName = userName.getText().toString();
                stringEmail = email.getText().toString();
                stringPassword = password.getText().toString().trim();
                stringreTypePassword = reTypePassword.getText().toString().trim();
                stringMobile = phone.getText().toString().trim();
                if (checkValidations(SignUp.this, stringName, stringEmail, stringPassword, stringreTypePassword,stringMobile)) {


                    HashMap<String, String> params = new HashMap<>();
                    params.put("name",stringName);
                    params.put("mobileNumber",stringMobile);
                    params.put("email",stringEmail);
                    params.put("password", stringPassword);
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
                pDialog = new ProgressDialog(SignUp.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(true);
                pDialog.show();

            }

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                String responseString = "";
                try {
                    URL url = new URL("http://52.27.53.102/testapi/user/create");
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

                        String name=resultSet.getString("name");
                        String email=resultSet.getString("email");
                        String mobile=resultSet.getString("mobile");
                        String password=resultSet.getString("password");
                        String createdOn=resultSet.getString("createdOn");
                        String updatedOn=resultSet.getString("updatedOn");
                        String id=resultSet.getString("id");
                        //Log.e(TAG, "json paring :" + name + email+ mobile + password + createdOn + updatedOn +id);
                        if(error_string.equals("success.")) {
                            Toast.makeText(SignUp.this,"SuccessFully Register",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(SignUp.this,NavigationDrawer.class);
                            startActivity(intent);
                            finish();
                            SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user", name);
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.putString("mobile", mobile);
                            editor.putString("id", id);
                            editor.putString("createon", createdOn);
                            editor.commit();

                        }
                        else{
                            Toast.makeText(SignUp.this, "SignUp Failed ", Toast.LENGTH_SHORT).show();
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
    private boolean checkValidations(SignUp signUp, String username, String email, String password, String retypepassword, String Mobile){

        if(username == null || username.length() == 0)
        {
            Toast.makeText(getApplicationContext(),"Please Enter Your UserName",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(email==null||email.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Email Should be in Correct Form",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (password == null || password.length() < 8) {
            Toast.makeText(getApplicationContext(), "Please enter 8 digit password", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (retypepassword == null || retypepassword.length() < 8)
        {
            Toast.makeText(getApplicationContext(), "Please enter 8 digit retype password.", Toast.LENGTH_LONG).show();
            return false;
        }

        else if (!password.equalsIgnoreCase(retypepassword))
        {

            Toast.makeText(getApplicationContext(), "Please enter same Password in both .", Toast.LENGTH_LONG).show();
            return false;

        }

        else if (Mobile == null || Mobile.length() == 0)
        {
            Toast.makeText(getApplicationContext(),"Please enter your Mobile Number",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*Bundle b = data.getExtras();
        bmp = (Bitmap) b.get("data");
        imageView.setImageBitmap(bmp);*/
            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data)
            {
                Bundle extras=data.getExtras();
                Bitmap image=extras.getParcelable("data");
                imageView.setImageBitmap(image);
            }

        super.onActivityResult(requestCode, resultCode, data);
    }
    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If perm ission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
