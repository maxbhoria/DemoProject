package com.example.mohitbhoria.demoproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PostDetailsActivity extends AppCompatActivity implements OnMapReadyCallback{
    TextView post, user, posttitle, Description, Address, poststatus, postedon,latlng;
    String postId;
    String userId;
    String postTitle, description, postStatus;
    String address;
    String latLng;
    String postedOn;
    Double latitude,longitude;
    String stringLatitude,stringLongitude;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        postId=getIntent().getStringExtra("postId");
        userId=getIntent().getStringExtra("userId");
        postedOn=getIntent().getStringExtra("postedOn");
        postTitle=getIntent().getStringExtra("postTitle");
        postStatus=getIntent().getStringExtra("postStatus");
        description=getIntent().getStringExtra("description");
        address=getIntent().getStringExtra("address");
        latLng=getIntent().getStringExtra("latLng");


        post= (TextView) findViewById(R.id.textViewPostId);
        user= (TextView) findViewById(R.id.textViewUserId);
        posttitle= (TextView) findViewById(R.id.textViewPostTitle);
        Description= (TextView) findViewById(R.id.textViewDescription);
        Address= (TextView) findViewById(R.id.textViewAddress);
        poststatus= (TextView) findViewById(R.id.textViewPostStatus);
        postedon= (TextView) findViewById(R.id.textViewPostedOn);
        //latlng= (TextView) findViewById(R.id.textViewlatLng);
        String[] seprated=latLng.split(",");
        seprated[0]=stringLatitude;
        seprated[1]=stringLongitude;

        if (latLng != null && latLng.length() > 0 && !latLng.equalsIgnoreCase("")&& latLng==null) {
            String[] latLng1 = latLng.split(",");
            latitude = Double.parseDouble((latLng1[0] != null && !latLng1[0].equalsIgnoreCase("null")) ? latLng1[0] : "");
            longitude = Double.parseDouble((latLng1[1] != null && !latLng1[0].equalsIgnoreCase("null")) ? latLng1[1] : "");
        } else {
            Toast.makeText(this, "Latlng Not find", Toast.LENGTH_SHORT).show();

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        post.setText(postId);
        user.setText(userId);
        posttitle.setText(postTitle);
        Description.setText(description);
        poststatus.setText(postStatus);
        Address.setText(address);
        postedon.setText(postedOn);
        //latlng.setText(latLng);

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (latLng != null && latLng.length() > 0 && !latLng.equalsIgnoreCase("") && latLng!=null) {
            LatLng currentLocation = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .position(currentLocation)
                    .title("Post Location")
                    .draggable(true));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
        }
        else
        {
            Toast.makeText(this, "No location available", Toast.LENGTH_SHORT).show();
        }
    }
}

