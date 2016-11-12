package com.c301t19.cs.ualberta.seekaride.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Request;

import java.util.ArrayList;

public class RequestsAdapter extends ArrayAdapter<Request> {

    ArrayList<Request> requests;
    LayoutInflater inflater;

    public RequestsAdapter(Context context, int resource, ArrayList<Request> objects, LayoutInflater li) {
        super(context, resource, objects);
        requests = objects;
        inflater = li;
    }

    // code modified from https://code.tutsplus.com/tutorials/android-from-scratch-understanding-adapters-and-adapter-views--cms-26646 Oct 1, 2016
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.main_list, null, false);
        }

        Request current = requests.get(position);

        TextView requestDescription = (TextView)convertView.findViewById(R.id.requestDescription);
        requestDescription.setText(current.getDescription());

        return convertView;
    }
}
