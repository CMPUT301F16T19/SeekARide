package com.c301t19.cs.ualberta.seekaride.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Request;

import java.util.ArrayList;

public class DriversAdapter extends ArrayAdapter<Profile> {

    ArrayList<Profile> drivers;
    LayoutInflater inflater;

    public DriversAdapter(Context context, int resource, ArrayList<Profile> objects, LayoutInflater li) {
        super(context, resource, objects);
        drivers = objects;
        inflater = li;
    }

    // code modified from https://code.tutsplus.com/tutorials/android-from-scratch-understanding-adapters-and-adapter-views--cms-26646 Oct 1, 2016
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.main_list, null, false);
        }

        final Profile current = drivers.get(position);

        TextView driverName = (TextView)convertView.findViewById(R.id.requestDescription);
        driverName.setText(current.getUsername());

        driverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Context c = parent.getContext();
                Intent intent = new Intent(c, ViewProfileActivity.class);
                intent.putExtra("profileId", current.getId());
                intent.putExtra("showAcceptButton", true);
                intent.putExtra("name", current.getUsername());
                c.startActivity(intent);
            }
        });

        return convertView;
    }
}
