package com.example.mohitbhoria.demoproject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ListView listView;
    ProgressDialog  progressDialog;
    String TAG="ERROR";
    ArrayList<String> list=new ArrayList<String>();
    CustomAdapter customAdapter;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView=(ListView)view.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                if (ConnectivityCheck.getInstance(getActivity()).isOnline()) {
                    /**
                     * Internet is available, Toast It!
                     */
                    swipeRefreshLayout.setRefreshing(false);
                    getRequest();
                } else {
                    /**
                     * Internet is NOT available, Toast It!
                     */
                    Toast.makeText(getActivity(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        getRequest();
        return view;
    }
    private void getRequest() {

        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                String responseString = "";

                try {
                    URL url = new URL("http://52.27.53.102/testapi/post/view");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(30000);
                    con.setConnectTimeout(30000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setDoInput(true);
                    con.connect();
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
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                progressDialog.dismiss();
                if (data != null) {
                    String response = data;
                    progressDialog.dismiss();
                    Data dataSet = new Gson().fromJson(response, Data.class);
                    ArrayList<Details> dataRecord=dataSet.getData();
                    CustomAdapter adapter=new CustomAdapter(getActivity(),dataRecord);
                    listView.setAdapter(adapter);

                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                }


            }
        }.execute();

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

}



/*
package com.example.mohitbhoria.demoproject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ListView listView;
    ProgressDialog  progressDialog;
    List<RowItem> rowItems=new ArrayList<>();
    ArrayList<String> list=new ArrayList<String>();
    String TAG="ERROR";
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();

            }
        });
        getRequest();
        return view;

    }
    private void getRequest() {

        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                String responseString = "";

                try {
                    URL url = new URL("http://52.27.53.102/testapi/post/view");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(30000);
                    con.setConnectTimeout(30000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setDoInput(true);
                    con.connect();
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
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                progressDialog.dismiss();
                if (data != null) {
                    String response = data;
                    //Parse json from response here
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String error_string = jsonObject.getString("error_string");
                        int error_code = jsonObject.getInt("error_code");
                        String message = jsonObject.getString("message");
                        //Log.e(TAG, "json " + "\t" + error_string + "\t" + error_code + "\t" + message);

                        JSONArray resultSet = jsonObject.getJSONArray("result");
                        for (int i = 0; i < resultSet.length(); i++) {
                            JSONObject jsonObj1 = resultSet.getJSONObject(i);
                            String  postId = jsonObj1.getString("postId");
                            String userId = jsonObj1.getString("userId");
                            String postTitle = jsonObj1.getString("postTitle");
                            String description = jsonObj1.getString("description");
                            String postStatus = jsonObj1.getString("postStatus");
                            String latLng = jsonObj1.getString("latLng");
                            String postedOn = jsonObj1.getString("postedOn");
                            //  Log.i(TAG, "json paring" + postid + "\t" + postmessage);
                            list.add(postId);
                            list.add(userId);
                            list.add(postTitle);
                            list.add(description);
                            list.add(postStatus);
                            list.add(latLng);
                            list.add(postedOn);
                        }
                        for(int k=0;k<list.size();k++){
                            RowItem item=new RowItem(list.get(k));
                            rowItems.add(item);
                        }
                        CustomAdapter adapter=new CustomAdapter(getActivity(),rowItems);
                        listView.setAdapter(adapter);

                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }

                } else {

                }

            }
        }.execute();

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

}*/
