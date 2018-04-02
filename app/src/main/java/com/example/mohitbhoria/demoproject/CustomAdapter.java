package com.example.mohitbhoria.demoproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by MOHIT BHORIA on 04-Aug-17.
 */

class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Details> rowItems;

    public CustomAdapter(Context context, ArrayList<Details> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    private class ViewHolder {
        LinearLayout linearLayout;
        TextView postId, userId, postTitle, description, address, postStatus, postedOn,latLng;

    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int i) {
        return rowItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return rowItems.indexOf(getItem(i));
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.linearLayout= (LinearLayout) view.findViewById(R.id.linearLayout);
            holder.postId = (TextView) view.findViewById(R.id.textViewPostId);
            holder.userId = (TextView) view.findViewById(R.id.textViewUserId);
            holder.description = (TextView) view.findViewById(R.id.textViewDescription);
            holder.postTitle = (TextView) view.findViewById(R.id.textViewPostTitle);
            holder.address = (TextView) view.findViewById(R.id.textViewLocation);
            holder.postStatus = (TextView) view.findViewById(R.id.textViewPostStatus);
            holder.postedOn = (TextView) view.findViewById(R.id.textViewPostedOn);
            //holder.latLng = (TextView) view.findViewById(R.id.textViewlatLng);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Details rowItem = (Details) getItem(i);
        holder.postId.setText(rowItem.getPostId());
        holder.userId.setText(rowItem.getUserId());
        holder.description.setText(rowItem.getDescription());
        holder.postTitle.setText(rowItem.getPostTitle());
        holder.address.setText(rowItem.getAddress());
        holder.postStatus.setText(rowItem.getPostStatus());
        holder.postedOn.setText(rowItem.getPostedOn());
        //holder.latLng.setText(rowItem.getLatLng());


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"hello",Toast.LENGTH_LONG).show();
                if (ConnectivityCheck.getInstance(context).isOnline()) {
                    /**
                     * Internet is available, Toast It!
                     */
                    Details rowItem= (Details) rowItems.get(i);
                    Intent intent=new Intent(context,PostDetailsActivity.class);
                    intent.putExtra("userId",rowItem.getUserId());
                    intent.putExtra("postId",rowItem.getPostId());
                    intent.putExtra("postedOn",rowItem.getPostedOn());
                    intent.putExtra("postTitle",rowItem.getPostTitle());
                    intent.putExtra("postStatus",rowItem.getPostStatus());
                    intent.putExtra("latLng",rowItem.getLatLng());
                    intent.putExtra("description",rowItem.getDescription());
                    intent.putExtra("address",rowItem.getAddress());
                    context.startActivity(intent);
                } else {
                    /**
                     * Internet is NOT available, Toast It!
                     */
                    Toast.makeText(context, "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }

            }


        });


        return view;
    }
}

