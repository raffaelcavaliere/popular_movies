package com.raffaelcavaliere.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by raffaelcavaliere on 2015-09-25.
 */
public class MovieDbArrayAdapter extends ArrayAdapter<FetchMovieDbTask.MovieDbItem> {

    public static final String MOVIE_DB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    public static final String MOVIE_DB_XSMALL_IMAGE_PATH = "w92";
    public static final String MOVIE_DB_SMALL_IMAGE_PATH = "w154";
    public static final String MOVIE_DB_MEDIUM_IMAGE_PATH = "w185";
    public static final String MOVIE_DB_LARGE_IMAGE_PATH = "w342";
    public static final String MOVIE_DB_XLARGE_IMAGE_PATH = "w500";

    private Context context;
    private int layoutResourceId;
    private ArrayList<FetchMovieDbTask.MovieDbItem> items;

    public MovieDbArrayAdapter(Context context, int layoutResourceId, ArrayList<FetchMovieDbTask.MovieDbItem> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MovieDbItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MovieDbItemHolder();
            holder.imgPoster = (ImageView)row.findViewById(R.id.moviegrid_item_poster);

            row.setTag(holder);
        }
        else
        {
            holder = (MovieDbItemHolder)row.getTag();
        }

        FetchMovieDbTask.MovieDbItem item = items.get(position);
        Uri uri = Uri.parse(MOVIE_DB_IMAGE_BASE_URL).buildUpon()
                .appendPath(MOVIE_DB_LARGE_IMAGE_PATH)
                .appendEncodedPath(item.posterPath).build();
        Picasso.with(context).load(uri.toString()).into(holder.imgPoster);

        return row;
    }

    static class MovieDbItemHolder
    {
        ImageView imgPoster;
    }
}