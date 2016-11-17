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
import com.c301t19.cs.ualberta.seekaride.core.Review;

import java.util.ArrayList;

public class ReviewsAdapter extends ArrayAdapter<Review> {

    ArrayList<Review> reviews;
    LayoutInflater inflater;

    public ReviewsAdapter(Context context, int resource, ArrayList<Review> objects, LayoutInflater li) {
        super(context, resource, objects);
        reviews = objects;
        inflater = li;
    }

    // code modified from https://code.tutsplus.com/tutorials/android-from-scratch-understanding-adapters-and-adapter-views--cms-26646 Oct 1, 2016
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.main_list, null, false);
        }

        final Review current = reviews.get(position);

        TextView driverName = (TextView)convertView.findViewById(R.id.requestDescription);
        driverName.setText(current.getDescription());

        return convertView;
    }
}
