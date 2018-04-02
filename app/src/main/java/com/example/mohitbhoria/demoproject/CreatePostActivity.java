package com.example.mohitbhoria.demoproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

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

public class CreatePostActivity extends AppCompatActivity {
    EditText userId, postTitle, postDescription, address;
    Button createPostButton;
    String stringUserId;
    String stringPostTitle;
    String stringDesciption;
    String stringPostedOn;
    String stringAddress;
    String latitude, longitude;
    String LatLng;
    int LOCATION_RESULT = 1;
    Intent intent;
    int LOCATION = 1;
    private int READ_YOUR_LOCATION = 23;
    private FusedLocationProviderClient mFusedLocationClient;
    public static final String MyPREFERENCES = "MyPrefs";
    private String TAG = "hello";
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (isReadLocationAllowed()) {
            //If permission is already having then showing the toast
            Toast.makeText(CreatePostActivity.this, "You already have the permission", Toast.LENGTH_LONG).show();
            //Existing the method with return
            return;
        }

        //If the app has not the permission then asking for the permission
        requestLocationPermission();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                    LatLng = latitude + "," + longitude;
                    //Toast.makeText(getApplicationContext(), "Latitude:-" + latitude + "\n" + "Longitude:-" + longitude, Toast.LENGTH_LONG).show();
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userId= (EditText) findViewById(R.id.editUserId);
        postTitle= (EditText) findViewById(R.id.editTextPostTitle);
        postDescription= (EditText) findViewById(R.id.editTextDescription);
        address= (EditText) findViewById(R.id.editTextAddress);
        createPostButton= (Button) findViewById(R.id.buttonPost);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringUserId = userId.getText().toString();
                stringPostTitle = postTitle.getText().toString();
                stringDesciption = postDescription.getText().toString().trim();
                stringAddress = address.getText().toString().trim();
                if (checkValidations(CreatePostActivity.this, stringUserId, stringPostTitle, stringDesciption, stringAddress)) {


                    HashMap<String, String> params = new HashMap<>();
                    params.put("userId",stringUserId);
                    params.put("postTitle",stringPostTitle);
                    params.put("description",stringDesciption);
                    params.put("latLng", LatLng);
                    params.put("address", stringAddress);
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
                pDialog = new ProgressDialog(CreatePostActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(true);
                pDialog.show();

            }

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                String responseString = "";
                try {
                    URL url = new URL("http://52.27.53.102/testapi/post/create");
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

                        String userId=resultSet.getString("userId");
                        String postTitle=resultSet.getString("postTitle");
                        String description=resultSet.getString("description");
                        String postedOn=resultSet.getString("postedOn");
                        String latLng=resultSet.getString("latLng");
                        String address=resultSet.getString("address");
                        String id=resultSet.getString("id");
                        //Log.e(TAG, "json paring :" + name + email+ mobile + password + createdOn + updatedOn +id);
                        if(error_string.equals("success.")) {
                            Toast.makeText(CreatePostActivity.this,"Post SuccessFully Created",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(CreatePostActivity.this,NavigationDrawer.class);
                            /*SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId", userId);
                            editor.putString("postTitle", postTitle);
                            editor.putString("description", description);
                            editor.putString("postedOn", postedOn);
                            editor.putString("latLng", latLng);
                            editor.putString("address", address);
                            editor.putString("id", id);
                            editor.commit();*/
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(CreatePostActivity.this, "Post is Not Created ", Toast.LENGTH_SHORT).show();
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
    private boolean checkValidations(CreatePostActivity createPostActivity, String stringUserId, String stringPostTitle,
                                     String stringDesciption, String stringAddress){

        if(stringUserId == null || stringUserId.length() == 0)
        {
            Toast.makeText(getApplicationContext(),"Please Enter Your UserId.",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(stringPostTitle==null||stringPostTitle.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Please Post Title.",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (stringDesciption == null || stringDesciption.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Your Post Description.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (stringAddress == null || stringAddress.length() == 0)
        {
            Toast.makeText(getApplicationContext(), "Please enter Your Address.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    private boolean isReadLocationAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
    //Requesting permission
    private void requestLocationPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, READ_YOUR_LOCATION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == READ_YOUR_LOCATION) {

            //If perm ission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the location", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}
