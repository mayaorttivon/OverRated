package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OverRatedSongAdapter extends ArrayAdapter<OverRatedSong> {

    Context context; //The Activity that has created the ListView
    List<OverRatedSong> objects;

    public OverRatedSongAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<OverRatedSong> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //The layout inflater take an xml and turns it into a class of type "View".
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_layout, parent, false);

        //update each object with the data from our arraylist.
        OverRatedSong overRatedSong = objects.get(position);
        TextView tvSong = view.findViewById(R.id.tvSong);
        tvSong.setText(overRatedSong.getName());
        TextView tvArtist = view.findViewById(R.id.tvArtist);
        tvArtist.setText(overRatedSong.getArtist());
        TextView tvGrade = view.findViewById(R.id.tvGrade);
        tvGrade.setText(String.valueOf(overRatedSong.getGrade()));

        return view;
    }
}
